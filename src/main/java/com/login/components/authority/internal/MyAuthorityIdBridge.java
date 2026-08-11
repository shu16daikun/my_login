// com.login.components.authority.internal.MyAuthorityIdBridge
package com.login.components.authority.internal;

import com.login.components.authority.api.exception.MyAuthorityException;
import com.my.util.security.id.IdBridge;
import com.my.util.type.MyType;

/**
 * authority の viewId ⇄ entityId 変換。
 *
 * <p>
 * 目的: 公開IDと内部IDの越境を1箇所に集約（将来のマスキング/署名対応に備える）。
 */
/**
 * @deprecated 署名付きViewId方式は段階廃止予定。
 *             SystemId を使用してください。
 */
@Deprecated(forRemoval = false, since = "0.1.0")
final class MyAuthorityIdBridge {

	private MyAuthorityIdBridge() {
	}

	static String toEntityId(final String viewId) {
		if (MyType.isBlank(viewId)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityErrorCode.MyAuthorityDbgMsg.blankId());
		}
		try {
			return IdBridge.toEntityId(
				viewId,
				MyAuthorityIdConstants.getPrefix(),
				MyAuthorityIdConstants.getTotalLength());
		} catch (final RuntimeException e) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityErrorCode.MyAuthorityDbgMsg.blankId(e.getMessage()),
				e);
		}
	}

	static String toViewId(final String entityId) {
		if (MyType.isBlank(entityId)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityErrorCode.MyAuthorityDbgMsg.blankId());
		}
		try {
			return IdBridge.toViewId(entityId);
		} catch (final RuntimeException e) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_ID,
				MyAuthorityErrorCode.MyAuthorityDbgMsg.blankId(e.getMessage()),
				e);
		}
	}
}
