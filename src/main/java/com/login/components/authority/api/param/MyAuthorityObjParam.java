package com.login.components.authority.api.param;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 画面／テンプレート間で受け渡す属性キーを集約。
 *
 * <p>
 * “画面に見せるもの”のみを列挙し、ログ専用は別管理。
 */
/* ===== [public/protected] START ===== */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MyAuthorityObjParam {

	/** 1件表示用：{@code MyAuthorityViewDto} */
	public static final String VIEW_DTO = "authorityViewDto";

	/** 一覧表示用：{@code List<MyAuthorityViewDto>} */
	public static final String VIEW_DTO_LIST = "authorityViewDtoList";
}
/* ===== [public/protected] END ===== */
