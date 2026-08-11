// com.login.components.user.internal.MyUsersIdBridge
package com.login.components.user.internal;

import com.login.components.user.api.exception.MyUsersException;
import com.login.components.user.internal.MyUsersErrorCode.MyUsersDbgMsg;
import com.my.util.security.id.IdBridge;
import com.my.util.type.MyType;

/** users の viewId ⇄ entityId 変換。 */
/**
 * @deprecated 署名付きViewId方式は段階廃止予定。
 *             SystemId を使用してください。
 */
@Deprecated(forRemoval = false, since = "0.1.0")
final class MyUsersIdBridge {

	private MyUsersIdBridge() {
	}

	static String toEntityId(final String viewId) {
		if (MyType.isBlank(viewId)) {
			throw new MyUsersException(MyUsersErrorCode.BLANK_ID, MyUsersDbgMsg.blankId());
		}
		try {
			return IdBridge.toEntityId(
				viewId,
				MyUsersIdConstants.getPrefix(),
				MyUsersIdConstants.getTotalLength());
		} catch (final RuntimeException e) {
			// 形式不正は INVALID_VIEW_ID に分類
			throw new MyUsersException(
				MyUsersErrorCode.INVALID_VIEW_ID,
				MyUsersDbgMsg.invalidViewId(viewId, e.getMessage()),
				e);
		}
	}

	static String toViewId(final String entityId) {
		if (MyType.isBlank(entityId)) {
			throw new MyUsersException(MyUsersErrorCode.BLANK_ID, MyUsersDbgMsg.blankId());
		}
		try {
			return IdBridge.toViewId(entityId);
		} catch (final RuntimeException e) {
			// 形式不正は INVALID_ENTITY_ID に分類
			throw new MyUsersException(
				MyUsersErrorCode.INVALID_ENTITY_ID,
				MyUsersDbgMsg.invalidEntityId(entityId, e.getMessage()),
				e);
		}
	}
}
