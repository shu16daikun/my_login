/*
 * MyUsers.java
 * Project : my_login
 * Package : com.login.components.user.api.service
 * Author  : shu-kundeath
 * Created : 2025/11/01 18:15:54
 *
 * 目的:
 * - TODO
 *
 * 注意:
 * - 定数/文字列の扱いは規約に従う（文字列は private static final String）
 */

package com.login.components.user.api.service;

import java.util.Collection;
import java.util.Set;

/**
 * MyUsers
 * 目的: TODO
 *
 * 公開契約:
 * - 例外は userCode のみ外部に出す（内部構造は伏せる）
 *
 * 備考:
 * - DTO は record を用いる
 */

public interface MyUsersRefLookUp {
	/* ===== [contract] START ===== */
	/* ===== [contract] END ===== */
	boolean existsByAuthorityId(String authorityId);

	/** 権限ID集合のうち、Users に使用されているIDを返す（IN最適化用） */
	Set<String> findUsedAuthorityIds(Collection<String> authorityIds);

}
