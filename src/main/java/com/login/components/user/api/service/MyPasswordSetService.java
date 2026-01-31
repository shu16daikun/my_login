package com.login.components.user.api.service;

import com.login.components.user.api.dto.MyPasswordSetInputDto;
import com.login.components.user.api.dto.MyPasswordSetViewDto;
import com.login.components.user.api.dto.MyUsersViewDto;
import com.login.components.user.internal.MyUsersEntity;

/**
 * パスワード設定ユースケース（初回設定／再設定）
 *
 * <p>
 * ログ出力時は平文パスワードを絶対に出さないこと。
 */
/* ===== [public/protected] START ===== */
public interface MyPasswordSetService {

	/**
	 * パスワードをエンコードして更新する。
	 *
	 * @param inputDto
	 *            新パスワードと確認用パスワード
	 * @return 更新後の UsersEntity（永続化後）
	 * @throws com.login.components.user.api.exception.MyPasswordSetException
	 *             入力不備やビジネスルール違反時
	 */
	MyUsersEntity updatePasswordEncode(MyPasswordSetInputDto inputDto);

	/**
	 * Users 表示DTOから、パスワード設定画面用DTOを組み立てる。
	 *
	 * @param usersViewDto
	 *            ユーザー表示DTO
	 * @return パスワード設定用の表示DTO
	 */
	MyPasswordSetViewDto getViewDtoFromUsersDto(MyUsersViewDto usersViewDto);
}
/* ===== [public/protected] END ===== */
