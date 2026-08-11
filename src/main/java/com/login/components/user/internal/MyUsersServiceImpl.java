// com.login.components.user.internal.MyUsersServiceImpl
package com.login.components.user.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.components.authority.api.dto.MyAuthorityViewDto;
import com.login.components.authority.api.service.MyAuthorityService;
import com.login.components.user.api.dto.MyUsersInputDto;
import com.login.components.user.api.dto.MyUsersViewDto;
import com.login.components.user.api.exception.MyUsersException;
import com.login.components.user.api.service.MyUsersService;
import com.login.components.user.internal.MyUsersErrorCode.MyUsersDbgMsg;
import com.my.util.security.id.DbIdSequence;
import com.my.util.security.role.RoleUtil;
import com.my.util.type.MyType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyUsersServiceImpl implements MyUsersService {

	private final MyUsersRepository repository;
	private final ToMyUsersViewDtoMapper toViewDtoMapper;
	private final PasswordEncoder passEncoder;
	private final MyAuthorityService authService;
	private final DbIdSequence idSeq;

	/* ====================================================================== */
	/* CUD */
	/* ====================================================================== */

	@Override
	@Transactional
	public void create(final MyUsersInputDto input) {
		if (MyType.isNull(input) || MyType.isBlank(input.username())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_USERNAME,
				MyUsersDbgMsg.blankUsername());
		}
		if (MyType.isBlank(input.authorityViewId())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_AUTHORITY,
				MyUsersDbgMsg.blankAuthority());
		}
		if (this.repository.existsByUsername(input.username())) {
			throw new MyUsersException(
				MyUsersErrorCode.DUPLICATE_USERNAME,
				MyUsersDbgMsg.duplicateUsername(input.username()));
		}

		this.repository.save(this.toEntityNew(input));
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public void delete(final String systemId) {
		if (MyType.isBlank(systemId)) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_ID,
				MyUsersDbgMsg.blankId());
		}
		final String id = this.getEntityIdBySystemId(systemId);

		if (!this.repository.existsById(id)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id));
		}
		this.repository.deleteById(id);
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public void delete(final MyUsersInputDto input) {
		if (MyType.isBlank(input.systemId())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_ID,
				MyUsersDbgMsg.blankId());
		}

		final String id = this.getEntityIdBySystemId(input.systemId());

		if (this.isOptimisticLockError(input)) {
			throw new MyUsersException(
				MyUsersErrorCode.OPTIMISTIC_LOCK,
				MyUsersDbgMsg.optimisticLock(
					id,
					input.version(),
					this.repository.getVersionById(id)
						.orElseThrow(() -> new MyUsersException(
							MyUsersErrorCode.NOT_USER_ENTITY,
							MyUsersDbgMsg.notUserEntity(id)))));
		}
		if (!this.repository.existsById(id)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id));
		}

		this.repository.deleteById(id);
	}

	@Override
	@Transactional
	public void update(final MyUsersInputDto input) {
		if (MyType.isNull(input) || MyType.isBlank(input.username())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_USERNAME,
				MyUsersDbgMsg.blankUsername());
		}
		if (MyType.isBlank(input.authorityViewId())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_AUTHORITY,
				MyUsersDbgMsg.blankAuthority());
		}
		if (this.repository.existsByUsername(input.username())) {
			if (MyType.isNotEqual(
				input.systemId(), this.getViewDtoByUsername(input.username()).systemId())) {
				throw new MyUsersException(
					MyUsersErrorCode.DUPLICATE_USERNAME,
					MyUsersDbgMsg.duplicateUsername(input.username()));
			}
		}

		this.repository.save(this.toEntityUpdate(input.systemId(), input));
	}

	@Override
	@Transactional
	public void update(final String currentViewId, final MyUsersInputDto input) {
		if (MyType.isNull(input) || MyType.isBlank(input.username())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_USERNAME,
				MyUsersDbgMsg.blankUsername());
		}
		if (MyType.isBlank(input.authorityViewId())) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_AUTHORITY,
				MyUsersDbgMsg.blankAuthority());
		}
		if (this.repository.existsByUsername(input.username())) {
			if (MyType.isNotEqual(
				input.systemId(), this.getViewDtoByUsername(input.username()).systemId())) {
				throw new MyUsersException(
					MyUsersErrorCode.DUPLICATE_USERNAME,
					MyUsersDbgMsg.duplicateUsername(input.username()));
			}
		}

		this.repository.save(this.toEntityUpdate(currentViewId, input));
	}

	@Override
	@Transactional
	public void changePassword(final String systemId, final String rawPassword) {
		if (this.isNotValidPassword(rawPassword)) {
			final int actual = rawPassword == null ? 0 : rawPassword.length();
			throw new MyUsersException(
				MyUsersErrorCode.INVALID_PASSWORD_FORMAT,
				MyUsersDbgMsg.invalidPasswordFormat(8, actual));
		}
		final String id = this.getEntityIdBySystemId(systemId);
		final String encoded = this.passEncoder.encode(rawPassword);

		final int updated = this.repository.updatePasswordById(id, encoded);
		if (this.isNotOne(updated)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id));
		}
	}

	@Override
	@Transactional
	public void resetPassword(final String systemId) {
		final String id = this.getEntityIdBySystemId(systemId);
		final int updated = this.repository.requestPasswordReset(id);
		if (this.isNotOne(updated)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id));
		}
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public void updateAuthority(final String userSystemId, final String authoritySystemId) {
		if (MyType.isBlank(authoritySystemId)) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_AUTHORITY,
				MyUsersDbgMsg.blankAuthority());
		}
		final String userId = this.getEntityIdBySystemId(userSystemId);
		final String authId = this.authService.getEntityIdBySystemId(authoritySystemId);

		final int updated = this.repository.updateAuthorityById(userId, authId);
		if (this.isNotOne(updated)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(userId));
		}
	}

	@Override
	@Transactional
	public void updateUsername(final String systemId, final String username) {
		if (MyType.isBlank(username)) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_USERNAME,
				MyUsersDbgMsg.blankUsername());
		}
		final String id = this.getEntityIdBySystemId(systemId);

		final int updated = this.repository.updateUsernameById(id, username);
		if (this.isNotOne(updated)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id));
		}
	}

	/* ====================================================================== */
	/* R */
	/* ====================================================================== */

	@Override
	@Transactional(readOnly = true)
	public Page<MyUsersViewDto> findAll(final Pageable pageable) {
		return this.repository.findAll(pageable)
			.map(e -> this.toViewDtoMapper.fromEntity(e, this.getAuthViewDto(e.getAuthorityId())));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyUsersViewDto> findAllNoPaging() {
		return this.repository.findAllOrderByIdAsc().stream()
			.map(e -> this.toViewDtoMapper.fromEntity(e, this.getAuthViewDto(e.getAuthorityId())))
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MyUsersViewDto>
		searchByUsername(final String likeUsername, final Pageable pageable) {
		final String kw = MyType.isBlank(likeUsername) ? "" : likeUsername.trim();
		return this.repository.searchByUsernameLike(kw, pageable)
			.map(e -> this.toViewDtoMapper.fromEntity(e, this.getAuthViewDto(e.getAuthorityId())));
	}

	/**
	 * @deprecated 署名付きViewId方式は段階廃止予定。
	 *             SystemId を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	@Override
	@Transactional(readOnly = true)
	public String getEntityId(final String viewId) {
		return MyUsersIdBridge.toEntityId(viewId);
	}

	@Override
	@Transactional(readOnly = true)
	public MyUsersViewDto getLoginUser() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (this.isUnauthenticated(auth)) {
			throw new MyUsersException(
				MyUsersErrorCode.NOT_LOGIN_USER,
				MyUsersDbgMsg.notLoginUser());
		}

		final Object principal = auth.getPrincipal();
		if (this.isMyUserDetails(principal)) {
			final MyUsersEntity entity = ((MyUserDetails) principal).getLoginUser();
			return this.toViewDtoMapper.fromEntity(entity,
				this.getAuthViewDto(entity.getAuthorityId()));
		}

		throw new MyUsersException(MyUsersErrorCode.NOT_LOGIN_USER, MyUsersDbgMsg.notLoginUser());
	}

	@Override
	@Transactional(readOnly = true)
	public MyUsersViewDto getViewDtoById(final String id) {
		final MyUsersEntity entity = this.repository.findById(id)
			.orElseThrow(() -> new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity(id)));

		return this.toViewDtoMapper.fromEntity(entity,
			this.getAuthViewDto(entity.getAuthorityId()));
	}

	/**
	 * @deprecated 署名付きViewId方式は段階廃止予定。
	 *             SystemId を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	@Override
	@Transactional(readOnly = true)
	public MyUsersViewDto getViewDtoByViewId(final String viewId) {
		return this.getViewDtoById(MyUsersIdBridge.toEntityId(viewId));
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, MyUsersViewDto> getViewDtoMapByIds(final Set<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return Map.of();
		}
		final List<MyUsersEntity> users = this.repository.findAllByIdIn(ids);

		return users.stream().collect(Collectors.toMap(
			MyUsersEntity::getSystemId,
			u -> this.toViewDtoMapper.fromEntity(u, this.getAuthViewDto(u.getAuthorityId()))));
	}

	/**
	 * 内部取得（UserDetailsService 用）
	 */
	@Transactional(readOnly = true)
	public MyUsersEntity getEntityByUsername(final String username) {
		if (MyType.isBlank(username)) {
			throw new MyUsersException(
				MyUsersErrorCode.BLANK_USERNAME,
				MyUsersDbgMsg.blankUsername());
		}
		return this.repository.findByUsername(username)
			.orElseThrow(() -> new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity("username", username)));
	}

	@Override
	public MyUsersViewDto getViewDtoBySystemId(String systemId) {
		return this.repository.findBySystemId(systemId)
			.map(entity -> this.toViewDtoMapper.fromEntity(entity,
				this.getAuthViewDto(entity.getAuthorityId())))
			.orElseThrow(() -> new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity("systemId", systemId)));
	}

	@Override
	public Map<String, MyUsersViewDto> getViewDtoMapBySystemIds(Set<String> systemIds) {
		if (systemIds == null || systemIds.isEmpty()) {
			return Map.of();
		}
		final List<MyUsersEntity> users = this.repository.findAllBySystemIdIn(systemIds);

		return users.stream().collect(Collectors.toMap(
			MyUsersEntity::getSystemId,
			u -> this.toViewDtoMapper.fromEntity(u, this.getAuthViewDto(u.getAuthorityId()))));
	}

	@Override
	public String getEntityIdBySystemId(String systemId) {
		return this.repository.findBySystemId(systemId)
			.map(entity -> entity.getId())
			.orElseThrow(() -> new MyUsersException(
				MyUsersErrorCode.NOT_USER_ENTITY,
				MyUsersDbgMsg.notUserEntity("systemId", systemId)));
	}

	/* ====================================================================== */
	/* isXxx 系（public） */
	/* ====================================================================== */

	@Override
	public boolean isLoggedIn(final Authentication authentication) {
		return !(authentication == null
			|| !authentication.isAuthenticated()
			|| (authentication instanceof AnonymousAuthenticationToken));
	}

	@Override
	public boolean isNotLoggedIn(final Authentication authentication) {
		return (authentication == null
			|| !authentication.isAuthenticated()
			|| (authentication instanceof AnonymousAuthenticationToken));
	}

	@Override
	@Transactional(readOnly = true)
	public final boolean isUse(final String id) {
		return this.repository.existsById(id);
	}

	/* ====================================================================== */
	/* logout */
	/* ====================================================================== */

	@Override
	public void logout(final HttpServletRequest request) {
		if (request == null) {
			return;
		}
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		SecurityContextHolder.clearContext();
	}

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response) {
		if (request == null || response == null) {
			return;
		}
		final Authentication authentication = SecurityContextHolder.getContext()
			.getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

	@Override
	public void logout(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Authentication authentication) {

		if (request == null || response == null) {
			return;
		}
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
	}

	/* ====================================================================== */
	/* private helpers */
	/* ====================================================================== */

	private MyAuthorityViewDto getAuthViewDto(final String id) {
		return this.authService.getViewDtoById(id);
	}

	@Transactional(readOnly = true)
	private MyUsersEntity getEntityBySystemId(final String systemId) {
		final String id = this.getEntityIdBySystemId(systemId);
		return this.repository.findById(id)
			.orElseThrow(() -> new MyUsersException(MyUsersErrorCode.NOT_USER_ENTITY));
	}

	@Transactional(readOnly = true)
	private MyUsersViewDto getViewDtoByUsername(final String username) {
		return this.repository.findByUsername(username)
			.map(entity -> {
				final MyAuthorityViewDto authViewDto = authService
					.getViewDtoById(entity.getAuthorityId());
				return toViewDtoMapper.fromEntity(entity, authViewDto);
			})
			.orElseThrow(() -> new MyUsersException(MyUsersErrorCode.NOT_USER_ENTITY));
	}

	@Transactional(readOnly = true)
	private boolean isExist(final String id) {
		return this.repository.existsById(id);
	}

	private boolean isMyUserDetails(final Object p) {
		return p instanceof MyUserDetails;
	}

	private boolean isNotOne(final int v) {
		return v != 1;
	}

	private boolean isNotValidPassword(final String pw) {
		return MyType.isBlank(pw) || pw.length() < 8;
	}

	private boolean isUnauthenticated(final Authentication auth) {
		if (MyType.isNull(auth)) {
			return true;
		}
		final Object p = auth.getPrincipal();
		if (p instanceof String) {
			return "anonymousUser".equals(p);
		}
		return !auth.isAuthenticated();
	}

	@Transactional(readOnly = true)
	private final boolean isOptimisticLockError(MyUsersInputDto input) {
		final String id = this.getEntityIdBySystemId(input.systemId());
		return MyType.isNotEqual(
			input.version(),
			this.repository.getVersionById(id)
				.orElseThrow(() -> new MyUsersException(
					MyUsersErrorCode.NOT_USER_ENTITY,
					MyUsersDbgMsg.notUserEntity(id))));
	}

	private MyUsersEntity toEntityNew(final MyUsersInputDto dto) {
		final String newId = this.idSeq.nextIdAvoidCollision(
			MyUsersDB.UsersIdParam.SEQUENCE,
			MyUsersDB.UsersIdParam.PREFIX,
			MyUsersDB.UsersIdParam.PAD,
			MyUsersDB.TABLE_FQN,
			MyUsersDB.UsersColumn.ID);

		if (this.isExist(newId)) {
			throw new MyUsersException(
				MyUsersErrorCode.DUPLICATE_ID,
				MyUsersDbgMsg.duplicateId(newId));
		}

		final String authorityId = this.authService.getEntityIdBySystemId(dto.systemId());
		return MyUsersEntity.builder()
			.id(newId)
			.username(dto.username().trim())
			.passwordEncode("")
			.authorityId(authorityId)
			.isFirstLogin(true)
			.resetPassword(true)
			.version((long) 0)
			.build();
	}

	private MyUsersEntity toEntityUpdate(final String currentSystemId, final MyUsersInputDto dto) {
		final String authorityId = this.authService.getEntityIdBySystemId(dto.systemId());
		final MyUsersEntity current = this.getEntityBySystemId(currentSystemId);
		if (MyType.isNotEqual(dto.version(), current.getVersion())) {
			throw new MyUsersException(
				MyUsersErrorCode.OPTIMISTIC_LOCK,
				MyUsersDbgMsg.optimisticLock(
					current.getId(),
					dto.version(),
					current.getVersion()));
		}

		return MyUsersEntity.builder()
			.id(current.getId())
			.username(dto.username().trim())
			.passwordEncode(current.getPasswordEncode())
			.authorityId(authorityId)
			.isFirstLogin(current.getIsFirstLogin())
			.resetPassword(current.isResetPassword())
			.version(dto.version())
			.build();
	}
}
