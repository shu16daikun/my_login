package com.login.components.user.api.param;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Users 画面の Model 属性名（ビュー間受け渡しのキーを集中管理） */
/* ===== [public/protected] START ===== */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MyUsersObjParam {
	/** 単一DTO：{@code "usersViewDto"} */
	public static final String VIEW_DTO = "usersViewDto";

	/** 複数DTO：{@code "usersViewDtoList"} */
	public static final String VIEW_DTO_LIST = "usersViewDtoList";
}
/* ===== [public/protected] END ===== */
