package com.login.components.user.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * パスワード設定 入力DTO
 *
 * <p>
 * 用途：初回ログインや再設定時の新パスワード入力。
 *
 * <h4>バリデーション方針</h4>
 *
 * <ul>
 * <li>長さ：8〜100文字（詳細な強度判定はアプリ層で追加可）
 * <li>password と passwordCheck の一致判定はアプリ層で実施
 * </ul>
 *
 * @param password
 *            新パスワード（8〜100）
 * @param passwordCheck
 *            確認用パスワード（8〜100）
 */
/* ===== [public/protected] START ===== */
public record MyPasswordSetInputDto(
	@NotBlank @Size(min = 8, max = 100) String password,
	@NotBlank @Size(min = 8, max = 100) String passwordCheck,
	@NotBlank Long version) {
}
/* ===== [public/protected] END ===== */
