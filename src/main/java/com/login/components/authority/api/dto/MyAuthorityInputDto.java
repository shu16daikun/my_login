package com.login.components.authority.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 権限の作成入力DTO。
 *
 * <p>
 * 目的: 画面からの入力値を最小限で受け、検証と重複確認をサービス層で行う。
 */
/* ===== [public/protected] START ===== */
public record MyAuthorityInputDto(
	String systemId,
	@NotBlank @Size(max = 20) String name,
	@NotBlank Long version) {
}
/* ===== [public/protected] END ===== */
