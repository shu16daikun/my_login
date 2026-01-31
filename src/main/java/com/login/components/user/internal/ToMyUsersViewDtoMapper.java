package com.login.components.user.internal;

import org.springframework.stereotype.Component;

import com.login.components.authority.api.dto.MyAuthorityViewDto;
import com.login.components.user.api.dto.MyUsersViewDto;

/**
 * UsersEntity → MyUsersViewDto 変換（純粋変換：DI依存なし）。
 *
 * 目的: 呼び出し側で解決済みの権限ViewDtoを受け取り、公開IDへ変換して返す。
 */
@Component
public class ToMyUsersViewDtoMapper {

	MyUsersViewDto fromEntity(
		final MyUsersEntity e,
		final MyAuthorityViewDto authorityViewDto) {
		return new MyUsersViewDto(
			e.getSystemId(),
			e.getUsername(),
			authorityViewDto,
			e.getIsFirstLogin());
	}
}
