// com.login.components.user.internal.MyUsersErrorCode
package com.login.components.user.internal;

import com.my.exception.error_code.ErrorCode;
import com.my.exception.error_code.HttpStatusCode;

/**
 * Users ユースケースのエラーコード（SLM.* 体系）。
 * 画面文言は error.properties（error.SLM.U* / error.SLM.L1 / error.SLM.common.*）を参照。
 */
enum MyUsersErrorCode implements ErrorCode {

	/* 共通（メッセージは SLM.common.* に統一） */
	BLANK_ID("SLM.U0", "error.SLM.common.badrequest", HttpStatusCode.BAD_REQUEST),
	NOT_USER_ENTITY("SLM.U404", "error.SLM.common.notfound", HttpStatusCode.NOT_FOUND),
	OPTIMISTIC_LOCK("SLM.U409", "error.SLM.common.optimisticlock", HttpStatusCode.CONFLICT),

	/* 形式不正（← 追加） */
	INVALID_VIEW_ID("SLM.U0V", "error.SLM.common.badrequest", HttpStatusCode.BAD_REQUEST),
	INVALID_ENTITY_ID("SLM.U0E", "error.SLM.common.badrequest", HttpStatusCode.BAD_REQUEST),

	/* SLM.U* 本体 */
	BLANK_USERNAME("SLM.U1", "error.SLM.U1", HttpStatusCode.BAD_REQUEST),
	BLANK_PASSWORD("SLM.U2", "error.SLM.U2", HttpStatusCode.BAD_REQUEST),
	DUPLICATE_USERNAME("SLM.U3", "error.SLM.U3", HttpStatusCode.CONFLICT),
	BLANK_AUTHORITY("SLM.U4", "error.SLM.U4", HttpStatusCode.BAD_REQUEST),
	INVALID_PASSWORD_FORMAT("SLM.U5", "error.SLM.U5", HttpStatusCode.BAD_REQUEST),
	DUPLICATE_ID("SLM.U6", "error.SLM.U6", HttpStatusCode.CONFLICT),

	/* Login */
	NOT_LOGIN_USER("SLM.L1", "error.SLM.L1", HttpStatusCode.UNAUTHORIZED);

	private final String code;
	private final String messageKey;
	private final HttpStatusCode status;

	MyUsersErrorCode(final String code, final String messageKey, final HttpStatusCode status) {
		this.code = code;
		this.messageKey = messageKey;
		this.status = status;
	}

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

	/* ===== [debug messages] ===== */
	public static final class MyUsersDbgMsg {
		private MyUsersDbgMsg() {
		}

		public static String blankId() {
			return "userId is blank";
		}

		public static String blankId(final String reason) {
			return String.format("userId is blank: reason=%s", safe(reason));
		}

		public static String invalidViewId(final String viewId, final String reason) {
			return String.format("invalid viewId: viewId=%s, reason=%s", safe(viewId),
				safe(reason));
		}

		public static String invalidEntityId(final String entityId, final String reason) {
			return String.format("invalid entityId: entityId=%s, reason=%s", safe(entityId),
				safe(reason));
		}

		public static String notUserEntity(final String id) {
			return String.format("user not found: id=%s", safe(id));
		}

		public static String notUserEntity(final String key, final String value) {
			return String.format("user not found: %s=%s", safe(key), safe(value));
		}

		public static String blankUsername() {
			return "username is blank";
		}

		public static String blankPassword() {
			return "password is blank";
		}

		public static String duplicateUsername(final String username) {
			return String.format("duplicate username: username=%s", safe(username));
		}

		public static String duplicateUsername(final String username, final String existingId) {
			return String.format("duplicate username: username=%s, existingId=%s",
				safe(username), safe(existingId));
		}

		public static String duplicateId(final String id) {
			return String.format("duplicate id: id=%s", safe(id));
		}

		public static String blankAuthority() {
			return "authorityViewId is blank";
		}

		public static String invalidPasswordFormat() {
			return "invalid password format";
		}

		public static String invalidPasswordFormat(final int minLen, final int actualLen) {
			return String.format("invalid password format: expected length>=%d, actual=%d",
				minLen, actualLen);
		}

		public static String notLoginUser() {
			return "not login user";
		}

		public static String optimisticLock(
			final String id,
			final Long expectedVersion,
			final Long actualVersion) {
			return String.format(
				"optimistic lock conflict: id=%s, expectedVersion=%s, actualVersion=%s",
				safe(id), safeObj(expectedVersion), safeObj(actualVersion));
		}

		private static String safeObj(final Object o) {
			return o == null ? "<null>" : String.valueOf(o);
		}

		private static String safe(final String s) {
			return s == null ? "<null>" : s;
		}
	}
}
