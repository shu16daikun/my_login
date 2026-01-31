package com.login.components.authority.api.dto;

/**
 * 権限の表示DTO（公開ID=ViewIdを含む）。
 *
 * <p>
 * 目的: 画面層に返す項目を限定し、内部IDの露出を避ける。
 */
/* ===== [public/protected] START ===== */
public record MyAuthorityViewDto(
	String systemId,
	String systemName) {
}
/* ===== [public/protected] END ===== */
