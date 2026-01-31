// com.login.components.user.internal.MyUsersDB
package com.login.components.user.internal;

final class MyUsersDB {

	/** スキーマ（必要に応じて変更） */
	static final String SCHEMA = "public";

	/** 物理テーブル名 */
	static final String TABLE = "users";

	/** FQN（schema.table） */
	static final String TABLE_FQN = SCHEMA + "." + TABLE;

	static final class UsersColumn {
		static final String ID = "id";
		static final String SYSTEM_ID = "system_id";
		static final String USERNAME = "username";
		static final String PASSWORD_ENCODE = "password_encode";
		static final String AUTHORITY_ID = "authority_id";
		static final String IS_FIRST_LOGIN = "is_first_login";
		static final String RESET_PASSWORD = "reset_password";
		static final String VERSION = "version";

		private UsersColumn() {
		}
	}

	/**
	 * ID採番（SEQ名はDB、prefix/PADは IdConstants に委譲）
	 * - 例: SEQUENCE = "public.us_id_seq"
	 */
	static final class UsersIdParam {
		private UsersIdParam() {
		}

		static final String SEQUENCE = SCHEMA + ".us_id_seq";
		static final String PREFIX = MyUsersIdConstants.getPrefix();
		static final int PAD = MyUsersIdConstants.getNumericLength();
	}

	private MyUsersDB() {
	}
}
