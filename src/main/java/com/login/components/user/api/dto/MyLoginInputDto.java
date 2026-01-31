package com.login.components.user.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * ログイン入力DTO（フォーム受領専用／ログ出力禁止）
 *
 * <p>
 * 用途：ユーザー名とパスワードでの認証。
 *
 * <h4>画面に見せるもの／ログ専用の線引き</h4>
 *
 * <ul>
 * <li>画面：フォーム入力の受領のみ。サーバ側でのマスキングは別責務。
 * <li>ログ：パスワードは絶対に出力しないこと。
 * </ul>
 *
 * @param username
 *            ユーザー名（必須）
 * @param password
 *            パスワード（必須・ログ出力禁止）
 */
/* ===== [public/protected] START ===== */
public record MyLoginInputDto(
	@NotBlank String username,
	@NotBlank String password,
	@NotBlank Long version) {
}
/* ===== [public/protected] END ===== */
