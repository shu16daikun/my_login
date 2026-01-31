package com.login.components.user.api.exception;

import com.exception.contents.MyRuntimeException;
import com.exception.error_code.ErrorCode;

/** Users ユースケースのドメイン例外。 */
/* ===== [public/protected] START ===== */
public final class MyUsersException extends MyRuntimeException {
	public MyUsersException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public MyUsersException(final ErrorCode errorCode, final String debugMessage) {
		super(errorCode, debugMessage);
	}

	public MyUsersException(final ErrorCode errorCode, final Throwable cause) {
		super(errorCode, cause);
	}

	public MyUsersException(
		final ErrorCode errorCode,
		final String debugMessage,
		final Throwable cause) {
		super(errorCode, debugMessage, cause);
	}
}
/* ===== [public/protected] END ===== */
