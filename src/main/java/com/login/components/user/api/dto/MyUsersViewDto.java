package com.login.components.user.api.dto;

import com.login.components.authority.api.dto.MyAuthorityViewDto;

/** ユーザー表示DTO（公開IDを含む）。 */
/* ===== [public/protected] START ===== */
public record MyUsersViewDto(
	String systemId,
	String username,
	MyAuthorityViewDto authorityViewDto,
	boolean isFirstLogin) {
}
/* ===== [public/protected] END ===== */
