// com.login.components.user.internal.MyUsersIdConstants
package com.login.components.user.internal;

/**
 * Users ID 規約定数。
 *
 * <ul>
 * <li>例: {@code US0001}（PREFIX=US, 数値4桁, 総桁6）
 * <li>正規表現は {@code ^US\\d{4}$}
 * </ul>
 */
final class MyUsersIdConstants {

	private MyUsersIdConstants() {
	}

	/** プレフィックス */
	private static final String PREFIX = "US";

	/** 総桁数 */
	private static final int TOTAL_LENGTH = 6;

	/** 数値部桁数 */
	private static final int NUMERIC_LENGTH = 4;

	/** 検証用正規表現 */
	private static final String REGEX = "^US\\d{4}$";

	public static String getPrefix() {
		return PREFIX;
	}

	public static int getTotalLength() {
		return TOTAL_LENGTH;
	}

	public static int getNumericLength() {
		return NUMERIC_LENGTH;
	}

	public static String getRegex() {
		return REGEX;
	}
}
