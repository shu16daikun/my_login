// com.login.components.authority.internal.MyAuthorityServiceImpl
package com.login.components.authority.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.components.authority.api.domain.MyAuthorityEnum;
import com.login.components.authority.api.dto.MyAuthorityInputDto;
import com.login.components.authority.api.dto.MyAuthorityViewDto;
import com.login.components.authority.api.exception.MyAuthorityException;
import com.login.components.authority.api.service.MyAuthorityService;
import com.login.components.authority.internal.MyAuthorityDB.AuthorityIdParam;
import com.login.components.authority.internal.MyAuthorityErrorCode.MyAuthorityDbgMsg;
import com.login.components.user.api.dto.MyUsersViewDto;
import com.login.components.user.api.service.MyUsersRefLookUp;
import com.my.util.security.id.DbIdSequence;
import com.my.util.security.id.SystemIdUtil;
import com.my.util.security.role.RoleUtil;
import com.my.util.type.MyType;

import lombok.AllArgsConstructor;

/**
 * 権限ユースケース実装。
 */
@Service
@AllArgsConstructor
public class MyAuthorityServiceImpl implements MyAuthorityService {

	private final MyAuthorityRepository repository;
	private final MyUsersRefLookUp usersRef;
	private final ToMyAuthorityViewDtoMapper toViewDtoMapper;
	private final DbIdSequence idSeq;

	/* ====================================================================== */
	/* CUD（ADMIN） */
	/* ====================================================================== */

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public MyAuthorityViewDto create(final MyAuthorityInputDto input) {
		if (MyType.isNull(input) || MyType.isBlank(input.name())) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		if (this.repository.existsByName(input.name())) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.DUPLICATE_NAME,
				MyAuthorityDbgMsg.duplicateName(input.name()));
		}
		if (!input.name().startsWith("ROLE_")) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.INVALID_ROLE_PREFIX,
				MyAuthorityDbgMsg.invalidRolePrefix(input.name(), "ROLE_"));
		}

		final MyAuthorityEntity saved = this.repository.save(this.toEntityNew(input));
		return this.toViewDtoMapper.fromEntity(saved);
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public MyAuthorityViewDto update(final MyAuthorityInputDto input) {
		if (MyType.isNull(input) || MyType.isBlank(input.name())) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		if (this.isOptimisticLockError(input)) {
			final String id = this.getEntityIdBySystemId(input.systemId());
			throw new MyAuthorityException(
				MyAuthorityErrorCode.OPTIMISTIC_LOCK,
				MyAuthorityDbgMsg.optimisticLock(
					id,
					input.version(),
					this.repository.getVersionById(id)
						.orElseThrow(() -> new MyAuthorityException(
							MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
							MyAuthorityDbgMsg.notAuthorityEntity(id)))));
		}
		if (this.repository.existsByName(input.name())) {
			if (MyType.isNotEqual(
				input.systemId(),
				this.getViewDtoByName(input.name()).systemId())) {
				throw new MyAuthorityException(
					MyAuthorityErrorCode.DUPLICATE_NAME,
					MyAuthorityDbgMsg.duplicateName(input.name()));
			}
		}
		if (!input.name().startsWith("ROLE_")) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.INVALID_ROLE_PREFIX,
				MyAuthorityDbgMsg.invalidRolePrefix(input.name(), "ROLE_"));
		}

		final MyAuthorityEntity saved = this.repository.save(this.toEntityUpdate(input));
		return this.toViewDtoMapper.fromEntity(saved);
	}

	@Override
	@Transactional
	public void updateName(final String systemId, final String newName) {
		if (MyType.isBlank(newName)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		if (this.repository.existsByName(newName)) {
			if (MyType.isNotEqual(
				systemId,
				this.getViewDtoByName(newName).systemId())) {
				throw new MyAuthorityException(
					MyAuthorityErrorCode.DUPLICATE_NAME,
					MyAuthorityDbgMsg.duplicateName(newName));
			}
		}
		if (!newName.startsWith("ROLE_")) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.INVALID_ROLE_PREFIX,
				MyAuthorityDbgMsg.invalidRolePrefix(newName, "ROLE_"));
		}

		final String id = this.getEntityIdBySystemId(systemId);
		final int updated = this.repository.updateNameById(id, newName);
		if (this.isNotOne(updated)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(id));
		}
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public void delete(final String systemId) {
		if (MyType.isBlank(systemId)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityDbgMsg.blankId());
		}

		final String id = this.getEntityIdBySystemId(systemId);

		if (!this.repository.existsById(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(id));
		}
		if (this.isUse(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.IN_USE,
				MyAuthorityDbgMsg.inUse(id));
		}

		this.repository.deleteById(id);
	}

	@Override
	@Transactional
	@PreAuthorize(RoleUtil.HAS_ROLE_ADMIN)
	public void delete(final MyAuthorityInputDto input) {
		if (MyType.isBlank(input.systemId())) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityDbgMsg.blankId());
		}

		final String id = this.getEntityIdBySystemId(input.systemId());

		if (this.isOptimisticLockError(input)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.OPTIMISTIC_LOCK,
				MyAuthorityDbgMsg.optimisticLock(
					id,
					input.version(),
					this.repository.getVersionById(id)
						.orElseThrow(() -> new MyAuthorityException(
							MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
							MyAuthorityDbgMsg.notAuthorityEntity(id)))));
		}
		if (!this.repository.existsById(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(id));
		}
		if (this.isUse(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.IN_USE,
				MyAuthorityDbgMsg.inUse(id));
		}

		this.repository.deleteById(id);
	}

	/* ====================================================================== */
	/* R */
	/* ====================================================================== */

	@Override
	@Transactional(readOnly = true)
	public MyAuthorityViewDto getViewDtoById(final String id) {
		if (MyType.isBlank(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityDbgMsg.blankId());
		}
		final MyAuthorityEntity entity = this.repository.findById(id)
			.orElseThrow(() -> new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(id)));

		return this.toViewDtoMapper.fromEntity(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public String getNameById(final String id) {
		if (MyType.isBlank(id)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityDbgMsg.blankId());
		}
		return this.repository.getNameById(id)
			.orElseThrow(() -> new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(id)));
	}

	/**
	 * @deprecated 署名付きViewId方式は段階廃止予定。
	 *             SystemId を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	@Override
	@Transactional(readOnly = true)
	public String getEntityId(final String viewId) {
		return MyAuthorityIdBridge.toEntityId(viewId);
	}

	/**
	 * @deprecated 署名付きViewId方式は段階廃止予定。
	 *             SystemId を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	@Override
	@Transactional(readOnly = true)
	public MyAuthorityViewDto getViewDtoByViewId(final String viewId) {
		final String id = MyAuthorityIdBridge.toEntityId(viewId);
		return this.getViewDtoById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MyAuthorityViewDto> searchByName(final String likeName, final Pageable pageable) {
		final String kw = MyType.isBlank(likeName) ? "" : likeName.trim();
		return this.repository.searchByNameLike(kw, pageable)
			.map(this.toViewDtoMapper::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyAuthorityViewDto> findAll() {
		return this.repository.findAllOrderByNameAsc().stream()
			.map(this.toViewDtoMapper::fromEntity)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, MyAuthorityViewDto> getViewDtoMapByIds(final Set<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return Map.of();
		}
		final List<MyAuthorityEntity> entities = this.repository.findAllByIdIn(ids);
		return entities.stream().collect(Collectors.toMap(
			MyAuthorityEntity::getSystemId,
			this.toViewDtoMapper::fromEntity));
	}

	@Override
	public MyAuthorityViewDto getViewDtoBySystemId(String systemId) {
		return this.repository.findBySystemId(systemId)
			.map(entity -> this.toViewDtoMapper.fromEntity(entity))
			.orElseThrow(() -> new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(systemId)));
	}

	@Override
	public String getEntityIdBySystemId(String systemId) {
		return this.repository.findBySystemId(systemId)
			.map(entity -> entity.getId())
			.orElseThrow(() -> new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(systemId)));
	}

	@Override
	public Map<String, MyAuthorityViewDto> getViewDtoMapBySystemIds(Set<String> systemIds) {
		if (systemIds == null || systemIds.isEmpty()) {
			return Map.of();
		}
		final List<MyAuthorityEntity> entities = this.repository.findAllBySystemIdIn(systemIds);
		return entities.stream().collect(Collectors.toMap(
			MyAuthorityEntity::getSystemId,
			this.toViewDtoMapper::fromEntity));
	}

	/* ====================================================================== */
	/* isXxx / 判定系 */
	/* ====================================================================== */

	@Override
	public boolean hasRole(final MyAuthorityEnum role, final MyUsersViewDto userDto) {
		if (MyType.isNull(role) || MyType.isNull(userDto)
			|| MyType.isNull(userDto.authorityViewDto())) {
			return false;
		}
		final String roleNameDb = role.getDbName(); // ROLE_付き
		final String userRoleNameDb = RoleUtil.toDbRole(userDto.authorityViewDto().systemName());
		return this.isSame(roleNameDb, userRoleNameDb);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUse(final String authorityId) {
		if (MyType.isBlank(authorityId)) {
			return false;
		}
		return this.usersRef.existsByAuthorityId(authorityId);
	}

	/* ====================================================================== */
	/* private helpers */
	/* ====================================================================== */

	private MyAuthorityEntity toEntityNew(final MyAuthorityInputDto dto) {
		final String newId = this.idSeq.nextIdAvoidCollision(
			AuthorityIdParam.SEQUENCE,
			AuthorityIdParam.PREFIX,
			AuthorityIdParam.PAD,
			MyAuthorityDB.TABLE_FQN,
			MyAuthorityDB.AuthorityColumn.ID);

		if (this.isExist(newId)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.DUPLICATE_ID,
				MyAuthorityDbgMsg.duplicateId(newId));
		}

		return MyAuthorityEntity.builder()
			.id(newId)
			.systemId(SystemIdUtil.newSystemId())
			.name(dto.name())
			.version((long) 0)
			.build();
	}

	private MyAuthorityEntity toEntityUpdate(final MyAuthorityInputDto dto) {
		final String id = this.getEntityIdBySystemId(dto.systemId());
		return MyAuthorityEntity.builder()
			.id(id)
			.systemId(dto.systemId())
			.name(dto.name())
			.version(dto.version())
			.build();
	}

	@Transactional(readOnly = true)
	private MyAuthorityViewDto getViewDtoByName(final String name) {
		if (MyType.isBlank(name)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankId());
		}
		final MyAuthorityEntity entity = this.repository.findByName(name)
			.orElseThrow(() -> new MyAuthorityException(
				MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
				MyAuthorityDbgMsg.notAuthorityEntity(name)));

		return this.toViewDtoMapper.fromEntity(entity);
	}

	@Transactional(readOnly = true)
	private final boolean isExist(final String id) {
		return this.repository.existsById(id);
	}

	@Transactional(readOnly = true)
	private final boolean isOptimisticLockError(MyAuthorityInputDto input) {
		final String id = this.getEntityIdBySystemId(input.systemId());
		return MyType.isNotEqual(
			input.version(),
			this.repository.getVersionById(id)
				.orElseThrow(() -> new MyAuthorityException(
					MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
					MyAuthorityDbgMsg.notAuthorityEntity(id))));
	}

	private boolean isNotOne(final int v) {
		return v != 1;
	}

	private boolean isSame(final String a, final String b) {
		return a == null ? b == null : a.equals(b);
	}

}
