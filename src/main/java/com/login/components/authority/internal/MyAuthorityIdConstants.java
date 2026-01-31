// com.login.components.authority.internal.MyAuthorityIdConstants
package com.login.components.authority.internal;

/**
 * Authority のID規約定数を集約。
 *
 * <p>
 * 表記ルール：AU + 4桁数字（例：AU0001）
 * </p>
 */
final class MyAuthorityIdConstants {

	private MyAuthorityIdConstants() {
	}

	private static final String PREFIX = "AU";
	private static final int TOTAL_LENGTH = 6;
	private static final int NUMERIC_LENGTH = 4;
	private static final String REGEX = "^AU\\d{4}$";

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
