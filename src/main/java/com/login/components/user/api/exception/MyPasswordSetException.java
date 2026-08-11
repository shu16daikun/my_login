package com.login.components.user.api.exception;

import com.my.exception.MyRuntimeException;
import com.my.exception.error_code.ErrorCode;

/**
 * パスワード設定ドメイン共通例外（実行時）
 *
 * <p>
 * 画面には userCode（問い合わせ番号）を提示し、内部構造は伏せる。
 */
/* ===== [public/protected] START ===== */
public final class MyPasswordSetException extends MyRuntimeException {
	public MyPasswordSetException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public MyPasswordSetException(final ErrorCode errorCode, final String debugMessage) {
		super(errorCode, debugMessage);
	}

	public MyPasswordSetException(final ErrorCode errorCode, final Throwable cause) {
		super(errorCode, cause);
	}

	public MyPasswordSetException(
		final ErrorCode errorCode,
		final String debugMessage,
		final Throwable cause) {
		super(errorCode, debugMessage, cause);
	}
}
/* ===== [public/protected] END ===== */
