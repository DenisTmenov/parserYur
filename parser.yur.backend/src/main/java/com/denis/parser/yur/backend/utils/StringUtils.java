package com.denis.parser.yur.backend.utils;

public class StringUtils {

	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}
}
