package com.login.components.user.internal;

import org.springframework.stereotype.Component;

import com.login.components.user.api.dto.MyPasswordSetViewDto;
import com.login.components.user.api.dto.MyUsersViewDto;

/**
 * 変換：User表示DTO → パスワード設定表示DTO。
 *
 * <p>
 * “画面に見せるもの”のみを詰める。内部情報は持ち込まない。
 */
/* ===== [public/protected] START ===== */
@Component
public class ToMyPasswordSetViewDtoMapper {

	/**
	 * 変換実行。
	 *
	 * @param userDto
	 *            ユーザー表示DTO（Service層で null チェック済想定）
	 * @return パスワード設定表示DTO
	 */
	MyPasswordSetViewDto fromViewDto(final MyUsersViewDto userDto) {
		return new MyPasswordSetViewDto(
			userDto.username(),
			userDto.authorityViewDto(),
			userDto.isFirstLogin());
	}
}
/* ===== [public/protected] END ===== */
