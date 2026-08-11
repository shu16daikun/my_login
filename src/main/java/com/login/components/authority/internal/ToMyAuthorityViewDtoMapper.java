package com.login.components.authority.internal;

import org.springframework.stereotype.Component;

import com.login.components.authority.api.dto.MyAuthorityViewDto;
import com.my.util.security.role.RoleUtil;

/**
 * AuthorityEntity → MyAuthorityViewDto 変換。
 *
 * <p>
 * 目的: 画面向けに項目を限定して返す。
 */
/* ===== [public/protected] START ===== */
@Component
public class ToMyAuthorityViewDtoMapper {

	MyAuthorityViewDto fromEntity(final MyAuthorityEntity e) {
		return new MyAuthorityViewDto(
			e.getSystemId(),
			RoleUtil.toSystemRole(e.getName()));
	}
}
/* ===== [public/protected] END ===== */
