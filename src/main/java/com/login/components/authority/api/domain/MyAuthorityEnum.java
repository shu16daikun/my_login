package com.login.components.authority.api.domain;

/* ===== [public/protected] START ===== */
public enum MyAuthorityEnum {
	ADMIN("ADMIN", "ROLE_ADMIN"),
	USER("USER", "ROLE_USER");

	private final String systemName; // 例: ADMIN
	private final String dbName; // 例: ROLE_ADMIN

	MyAuthorityEnum(final String systemName, final String dbName) {
		this.systemName = systemName;
		this.dbName = dbName;
	}

	/** システム表記（ROLE_なし） */
	public String getSystemName() {
		return systemName;
	}

	/** DB表記（ROLE_付き） */
	public String getDbName() {
		return dbName;
	}
}
/* ===== [public/protected] END ===== */
