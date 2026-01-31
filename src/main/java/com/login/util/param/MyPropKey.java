// com.login.util.param.MyPropKey
package com.login.util.param;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** メッセージプロパティのキー定義（越境参照可） */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MyPropKey {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class ErrorProp {
		private static final String BASE = "error.";

		/* ===== Login (SLM.L*) ===== */
		public static final String SLM_L1 = BASE + "SLM.L1";

		/* ===== Password Set (SLM.PS*) ===== */
		public static final String SLM_PS1 = BASE + "SLM.PS1";
		public static final String SLM_PS2 = BASE + "SLM.PS2";

		/* ===== Users (SLM.U*) ===== */
		public static final String SLM_U1 = BASE + "SLM.U1"; // ユーザー名が空欄です。
		public static final String SLM_U2 = BASE + "SLM.U2"; // パスワードが空欄です。
		public static final String SLM_U3 = BASE + "SLM.U3"; // 同じユーザー名が既に存在します。
		public static final String SLM_U4 = BASE + "SLM.U4"; // 権限が指定されていません。
		public static final String SLM_U5 = BASE + "SLM.U5"; // パスワードの形式が不正です。
		public static final String SLM_U6 = BASE + "SLM.U6"; // IDが既に存在します。

		/* ===== Authority (SLM.A*) ===== */
		public static final String SLM_A1 = BASE + "SLM.A1"; // 該当する権限が存在しません。
		public static final String SLM_A2 = BASE + "SLM.A2"; // 権限が空欄です。
		public static final String SLM_A3 = BASE + "SLM.A3"; // 同じ権限名が既に存在します。
		public static final String SLM_A4 = BASE + "SLM.A4"; // 権限名は「ROLE_」で始まる必要があります。
		public static final String SLM_A5 = BASE + "SLM.A5"; // IDが既に存在します。
		public static final String SLM_A9 = BASE + "SLM.A9"; // 使用中のため削除不可

		/* ===== Common (SLM.common.*) ===== */
		public static final String SLM_COMMON_UNEXPECTED = BASE + "SLM.common.unexpected";
		public static final String SLM_COMMON_BADREQUEST = BASE + "SLM.common.badrequest";
		public static final String SLM_COMMON_FORBIDDEN = BASE + "SLM.common.forbidden";
		public static final String SLM_COMMON_NOTFOUND = BASE + "SLM.common.notfound";
		public static final String SLM_COMMON_UNAUTHORIZED = BASE + "SLM.common.unauthorized";
	}
}
