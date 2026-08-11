package com.login.components.authority.api.exception;

import com.my.exception.MyRuntimeException;
import com.my.exception.error_code.ErrorCode;

/**
 * 権限ユースケースのドメイン例外。
 *
 * <p>
 * 目的: 画面には userCode（短い問い合わせ番号）のみ出す。内部情報はログ側で扱う。
 */
/* ===== [public/protected] START ===== */
public final class MyAuthorityException extends MyRuntimeException {
	public MyAuthorityException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public MyAuthorityException(final ErrorCode errorCode, final String debugMessage) {
		super(errorCode, debugMessage);
	}

	public MyAuthorityException(final ErrorCode errorCode, final Throwable cause) {
		super(errorCode, cause);
	}

	public MyAuthorityException(
		final ErrorCode errorCode,
		final String debugMessage,
		final Throwable cause) {
		super(errorCode, debugMessage, cause);
	}
}
/* ===== [public/protected] END ===== */
