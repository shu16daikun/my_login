package com.login.components.user.api.dto;

import com.login.components.authority.api.dto.MyAuthorityViewDto;

/**
 * パスワード設定 画面表示DTO（読み取り専用）
 *
 * <p>
 * 用途：パスワード設定画面に必要なユーザー情報の提示。
 *
 * @param username
 *            ユーザー名（表示用）
 * @param authorityDto
 *            権限の表示DTO（システム表記）
 * @param firstLogin
 *            初回ログインフラグ（true=初回）
 */
/* ===== [public/protected] START ===== */
public record MyPasswordSetViewDto(
	String username,
	MyAuthorityViewDto authorityDto,
	boolean firstLogin) {
}
/* ===== [public/protected] END ===== */
