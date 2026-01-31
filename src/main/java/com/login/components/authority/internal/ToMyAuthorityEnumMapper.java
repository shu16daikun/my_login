package com.login.components.authority.internal;

import org.springframework.stereotype.Component;

import com.login.components.authority.api.domain.MyAuthorityEnum;
import com.login.components.authority.api.exception.MyAuthorityException;
import com.login.components.authority.internal.MyAuthorityErrorCode.MyAuthorityDbgMsg;
import com.util.type.MyType;

/** AuthorityEntity／名称 と {@link MyAuthorityEnum} の相互変換。 */
@Component
public class ToMyAuthorityEnumMapper {

	/** Entity（DB表記=ROLE_付き）→ 列挙 */
	MyAuthorityEnum fromEntity(final MyAuthorityEntity entity) {
		if (MyType.isNull(entity) || MyType.isBlank(entity.getName())) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		return this.fromDbName(entity.getName());
	}

	/** システム表記（例: ADMIN）→ 列挙 */
	MyAuthorityEnum fromName(final String systemName) {
		if (MyType.isBlank(systemName)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		for (final MyAuthorityEnum v : MyAuthorityEnum.values()) {
			if (isSame(v.getSystemName(), systemName)) {
				return v;
			}
		}
		throw new MyAuthorityException(
			MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
			MyAuthorityDbgMsg.notAuthorityEntity(systemName));
	}

	/** DB表記（例: ROLE_ADMIN）→ 列挙 */
	MyAuthorityEnum fromDbName(final String dbName) {
		if (MyType.isBlank(dbName)) {
			throw new MyAuthorityException(
				MyAuthorityErrorCode.BLANK_NAME,
				MyAuthorityDbgMsg.blankName());
		}
		for (final MyAuthorityEnum v : MyAuthorityEnum.values()) {
			if (isSame(v.getDbName(), dbName)) {
				return v;
			}
		}
		throw new MyAuthorityException(
			MyAuthorityErrorCode.NOT_AUTHORITY_ENTITY,
			MyAuthorityDbgMsg.notAuthorityEntity(dbName));
	}

	/* ===== [private] START ===== */
	private static boolean isSame(final String a, final String b) {
		return a == null ? b == null : a.equals(b);
	}
	/* ===== [private] END ===== */
}
