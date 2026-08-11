// com.login.components.authority.internal.MyAuthorityErrorCode
package com.login.components.authority.internal;

import com.my.exception.error_code.ErrorCode;
import com.my.exception.error_code.HttpStatusCode;

/**
 * 権限ユースケースのエラーコード（SLM.* 体系）。
 * 画面文言は error.properties（error.SLM.A* / error.SLM.common.*）を参照。
 */
enum MyAuthorityErrorCode implements ErrorCode {

	/* 共通（メッセージは SLM.common.* に統一） */
	BLANK_ID("SLM.A0", "error.SLM.common.badrequest", HttpStatusCode.BAD_REQUEST),
	OPTIMISTIC_LOCK("SLM.A409", "error.SLM.common.optimisticlock", HttpStatusCode.CONFLICT),

	/* SLM.A* 本体 */
	NOT_AUTHORITY_ENTITY("SLM.A1", "error.SLM.A1", HttpStatusCode.NOT_FOUND),
	BLANK_NAME("SLM.A2", "error.SLM.A2", HttpStatusCode.BAD_REQUEST),
	DUPLICATE_NAME("SLM.A3", "error.SLM.A3", HttpStatusCode.CONFLICT),
	INVALID_ROLE_PREFIX("SLM.A4", "error.SLM.A4", HttpStatusCode.UNPROCESSABLE_ENTITY),
	DUPLICATE_ID("SLM.A5", "error.SLM.A5", HttpStatusCode.CONFLICT),

	/* 参照中（削除不可） */
	IN_USE("SLM.A9", "error.SLM.A9", HttpStatusCode.CONFLICT);

	private final String code;
	private final String messageKey;
	private final HttpStatusCode status;

	MyAuthorityErrorCode(final String code, final String messageKey, final HttpStatusCode status) {
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

	public static final class MyAuthorityDbgMsg {
		private MyAuthorityDbgMsg() {
		}

		public static String blankId() {
			return "authorityId is blank";
		}

		public static String blankId(final String reason) {
			return String.format("authorityId is blank: reason=%s", safe(reason));
		}

		public static String notAuthorityEntity(final String id) {
			return String.format("authority not found: id=%s", safe(id));
		}

		public static String blankName() {
			return "name is blank";
		}

		public static String duplicateName(final String name) {
			return String.format("duplicate authority name: name=%s", safe(name));
		}

		public static String duplicateName(final String name, final String existingId) {
			return String.format("duplicate authority name: name=%s, existingId=%s", safe(name),
				safe(existingId));
		}

		public static String duplicateId(final String id) {
			return String.format("duplicate id: id=%s", safe(id));
		}

		public static String invalidRolePrefix(final String name, final String expectedPrefix) {
			return String.format("invalid role prefix: name=%s, expectedPrefix=%s", safe(name),
				safe(expectedPrefix));
		}

		public static String inUse(final String id) {
			return String.format("authority is in use: id=%s", safe(id));
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
