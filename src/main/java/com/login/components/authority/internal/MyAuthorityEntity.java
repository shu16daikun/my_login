package com.login.components.authority.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * authority エンティティ。
 *
 * <p>
 * 注意: IDはアプリ側で採番（DB自動採番は使わない）。
 */
/* ===== [public/protected] START ===== */
@Entity
@Table(name = MyAuthorityDB.TABLE)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyAuthorityEntity {

	@Id
	@Column(name = MyAuthorityDB.AuthorityColumn.ID, nullable = false, updatable = false)
	private String id;

	@Column(name = MyAuthorityDB.AuthorityColumn.SYSTEM_ID, nullable = false, updatable = false)
	private String systemId;

	@Column(name = MyAuthorityDB.AuthorityColumn.NAME, nullable = false, unique = true)
	private String name;

	/** 楽観ロック用バージョン **/
	@Version
	@Column(name = MyAuthorityDB.AuthorityColumn.VERSION, nullable = false, unique = true)
	private Long version;
}
/* ===== [public/protected] END ===== */
