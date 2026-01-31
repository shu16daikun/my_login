package com.login.components.user.api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.login.components.user.api.dto.MyUsersInputDto;
import com.login.components.user.api.dto.MyUsersViewDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Users ユースケース（参照/作成/更新）。
 *
 * <p>
 * 外部公開IDは SystemId を使用する（URL / hidden / 画面入力 / 画面遷移）。
 * DBの主キー（EntityId）とは混同しない。
 *
 * <p>
 * 旧: viewId 方式は段階廃止のため deprecated。
 */
/* ===== [public/protected] START ===== */
public interface MyUsersService {

	/* ====================================================================== */
	/* CUD */
	/* ====================================================================== */

	void create(MyUsersInputDto input);

	void update(MyUsersInputDto input);

	void update(String currentSystemId, MyUsersInputDto input);

	void delete(String systemId);

	void delete(MyUsersInputDto input);

	void changePassword(String userSystemId, String rawPassword);

	void resetPassword(String systemId);

	void updateUsername(String systemId, String username);

	void updateAuthority(String userSystemId, String authoritySystemId);

	/* ====================================================================== */
	/* R */
	/* ====================================================================== */

	Page<MyUsersViewDto> findAll(Pageable pageable);

	List<MyUsersViewDto> findAllNoPaging();

	Page<MyUsersViewDto> searchByUsername(String likeUsername, Pageable pageable);

	MyUsersViewDto getLoginUser();

	/**
	 * DB主キー（EntityId）から取得（内部用途）。
	 */
	MyUsersViewDto getViewDtoById(String id);

	/**
	 * SystemId から取得（外部公開IDの基本ルート）。
	 */
	MyUsersViewDto getViewDtoBySystemId(String systemId);

	/**
	 * SystemId集合 → ViewDto の一括取得（IN最適化用）。
	 * 返却Mapのキーは「systemId」想定。
	 */
	Map<String, MyUsersViewDto> getViewDtoMapBySystemIds(Set<String> systemIds);

	/**
	 * SystemId → EntityId 変換（内部向け）。
	 */
	String getEntityIdBySystemId(String systemId);

	/**
	 * EntityId集合 → ViewDto の一括取得（IN最適化用）。
	 * ※既存用途が EntityId 前提なら残す（内部用途）。
	 */
	Map<String, MyUsersViewDto> getViewDtoMapByIds(Set<String> ids);

	/* ====================================================================== */
	/* isXxx / 判定系 */
	/* ====================================================================== */

	boolean isLoggedIn(Authentication authentication);

	boolean isNotLoggedIn(Authentication authentication);

	/**
	 * 利用中判定（どのIDを渡すかブレやすいので、可能なら isUseByEntityId / isUseBySystemId に分離推奨）。
	 */
	boolean isUse(String id);

	/* ====================================================================== */
	/* logout */
	/* ====================================================================== */

	void logout(HttpServletRequest request);

	void logout(HttpServletRequest request, HttpServletResponse response);

	void logout(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication);

	/* ====================================================================== */
	/* Deprecated: viewId 方式 */
	/* ====================================================================== */

	/**
	 * @deprecated SystemId に移行。{@link #getEntityIdBySystemId(String)} を使用。
	 */
	@Deprecated(forRemoval = false, since = "0.1.6")
	String getEntityId(String viewId);

	/**
	 * @deprecated SystemId に移行。{@link #getViewDtoBySystemId(String)} を使用。
	 */
	@Deprecated(forRemoval = false, since = "0.1.6")
	MyUsersViewDto getViewDtoByViewId(String viewId);
}
/* ===== [public/protected] END ===== */
