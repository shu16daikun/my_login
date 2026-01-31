/*
 * MyUsersRefLookUpImpl.java
 * Project : my_login
 * Package : com.login.components.user.internal
 * Author  : shu-kundeath
 * Created : 2025/11/01 18:16:56
 *
 * 目的:
 * - TODO
 *
 * 注意:
 * - 定数/文字列の扱いは規約に従う（文字列は private static final String）
 */

package com.login.components.user.internal;

import java.util.Collection;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.components.user.api.service.MyUsersRefLookUp;
import com.util.type.MyType;

import lombok.RequiredArgsConstructor;

/**
 * MyUsersRefLookUpImpl
 * 目的: TODO
 *
 * 公開契約:
 * - 例外は userCode のみ外部に出す（内部構造は伏せる）
 *
 * 備考:
 * - DTO は record を用いる
 */

@Service
@RequiredArgsConstructor
public class MyUsersRefLookupImpl implements MyUsersRefLookUp {
	private final MyUsersRepository repository;

	@Transactional(readOnly = true)
	@Override
	public boolean existsByAuthorityId(final String authorityId) {
		if (MyType.isBlank(authorityId)) {
			return false;
		}
		return this.repository.existsByAuthorityId(authorityId); // 派生クエリをRepositoryに追加
	}

	@Transactional(readOnly = true)
	@Override
	public Set<String> findUsedAuthorityIds(final Collection<String> authorityIds) {
		if (authorityIds == null || authorityIds.isEmpty()) {
			return Set.of();
		}
		return this.repository.pickUsedAuthorityIds(authorityIds);
	}
}
