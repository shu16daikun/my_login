package com.login.components.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** ユーザー作成の入力DTO。 */
/* ===== [public/protected] START ===== */
public record MyUsersInputDto(
	String systemId,
	@NotBlank @Size(max = 30) String username,
	@NotBlank String authorityViewId,
	@NotBlank Long version) {
}
/* ===== [public/protected] END ===== */
