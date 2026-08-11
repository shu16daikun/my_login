// com.login.components.user.internal.MyPasswordSetErrorCode
package com.login.components.user.internal;

import com.my.exception.error_code.ErrorCode;
import com.my.exception.error_code.HttpStatusCode;

import lombok.AllArgsConstructor;

/**
 * パスワード設定に関するエラーコード定義。
 *
 * <ul>
 * <li>画面表示には userCode のみを用い、内部構造は出さない
 * <li>HTTP ステータスは {@link HttpStatusCode} に委譲
 * </ul>
 */
/* ===== [public/protected] START ===== */
@AllArgsConstructor
enum MyPasswordSetErrorCode implements ErrorCode {

	/* 400: 確認用パスワード不一致 */
	MISMATCH("PWD-400-01", "error.SLM.PS1", HttpStatusCode.BAD_REQUEST),

	/* 404: 対象ユーザーが見つからない */
	NOT_FOUND("PWD-404-01", "error.SLM.PS2", HttpStatusCode.NOT_FOUND);

	/* ===== [private] START ===== */
	private final String code;
	private final String messageKey;
	private final HttpStatusCode status;
	/* ===== [private] END ===== */

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessageKey() {
		return this.messageKey;
	}

	@Override
	public HttpStatusCode getHttpStatus() {
		return this.status;
	}

	/* ===== [debug messages: MyPasswordSet] START ===== */
	/**
	 * ルール: ErrorCode 定数名とメソッド名を合わせる（lowerCamel）。
	 */
	public static final class MyPasswordDbgMsg {

		private MyPasswordDbgMsg() {
		}

		/** MISMATCH: "password mismatch: password=***, passwordCheck=***" */
		public static String mismatch(final String password, final String passwordCheck) {
			return String.format("password mismatch: password=%s, passwordCheck=%s",
				safe(password), safe(passwordCheck));
		}

		/** NOT_FOUND: "user not found: entityId={id}" */
		public static String notFound(final String entityId) {
			return String.format("user not found: entityId=%s", safe(entityId));
		}

		private static String safe(final String s) {
			return s == null ? "<null>" : s;
		}
	}
	/* ===== [debug messages: MyPasswordSet] END ===== */
}
/* ===== [public/protected] END ===== */
