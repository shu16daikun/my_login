// com.login.components.authority.api.service.MyAuthorityService
package com.login.components.authority.api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.login.components.authority.api.domain.MyAuthorityEnum;
import com.login.components.authority.api.dto.MyAuthorityInputDto;
import com.login.components.authority.api.dto.MyAuthorityViewDto;
import com.login.components.user.api.dto.MyUsersViewDto;

public interface MyAuthorityService {

	/* ====================================================================== */
	/* CUD（ADMIN） */
	/* ====================================================================== */

	MyAuthorityViewDto create(MyAuthorityInputDto input);

	MyAuthorityViewDto update(MyAuthorityInputDto input);

	void updateName(String systemId, String newName);

	void delete(String systemId);

	void delete(MyAuthorityInputDto input);

	/* ====================================================================== */
	/* R（推奨：SystemId で取得） */
	/* ====================================================================== */

	MyAuthorityViewDto getViewDtoById(String id);

	String getNameById(String id);

	/**
	 * SystemId から ViewDto 取得（推奨）。
	 */
	MyAuthorityViewDto getViewDtoBySystemId(String systemId);

	/**
	 * SystemId から DBのID 取得（推奨）。
	 */
	String getEntityIdBySystemId(String systemId);

	/**
	 * SystemId 集合 → ViewDtoMap（推奨）。
	 */
	Map<String, MyAuthorityViewDto> getViewDtoMapBySystemIds(Set<String> systemIds);

	Page<MyAuthorityViewDto> searchByName(String likeName, Pageable pageable);

	List<MyAuthorityViewDto> findAll();

	Map<String, MyAuthorityViewDto> getViewDtoMapByIds(Set<String> ids);

	/* ====================================================================== */
	/* 互換（非推奨：viewId で取得） */
	/* ====================================================================== */

	/**
	 * @deprecated SystemId を採用するため、viewId（旧:署名付きViewId）前提の変換は非推奨。
	 *             {@link #getEntityIdBySystemId(String)} を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	String getEntityId(String viewId);

	/**
	 * @deprecated SystemId を採用するため、viewId（旧:署名付きViewId）前提の取得は非推奨。
	 *             {@link #getViewDtoBySystemId(String)} を使用してください。
	 */
	@Deprecated(forRemoval = false, since = "0.1.0")
	MyAuthorityViewDto getViewDtoByViewId(String viewId);

	/* ====================================================================== */
	/* isXxx / 判定系 */
	/* ====================================================================== */

	boolean hasRole(MyAuthorityEnum role, MyUsersViewDto userDto);

	boolean isUse(String authorityId);
}
