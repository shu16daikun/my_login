// com.login.components.authority.internal.MyAuthorityDB
package com.login.components.authority.internal;

/** authority テーブルのメタ定義（列/採番パラメータ）。 */
final class MyAuthorityDB {

	/** スキーマ（必要に応じて変更） */
	static final String SCHEMA = "public";

	/** 物理テーブル名 */
	static final String TABLE = "authority";

	/** FQN（schema.table） */
	static final String TABLE_FQN = SCHEMA + "." + TABLE;

	static final class AuthorityColumn {
		static final String ID = "id";
		static final String SYSTEM_ID = "system_id";
		static final String NAME = "name";
		static final String VERSION = "version";

		private AuthorityColumn() {
		}
	}

	/** ID採番（SEQ名はDB、prefix/PADは IdConstants に委譲） */
	static final class AuthorityIdParam {
		private AuthorityIdParam() {
		}

		static final String SEQUENCE = SCHEMA + ".au_id_seq";
		static final String PREFIX = MyAuthorityIdConstants.getPrefix(); // "AU"
		static final int PAD = MyAuthorityIdConstants.getNumericLength(); // 4
	}

	private MyAuthorityDB() {
	}
}
