package utils;

import models.Clanek;

import java.text.Normalizer;

/** Created with IntelliJ IDEA. User: slaha Date: 28.11.13 Time: 23:30 */
public class StringUtils {

	public static String normalize(final String str, boolean toLowerCase) {
		String decomposed = java.text.Normalizer.normalize(str, Normalizer.Form.NFD);
		decomposed = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace(' ', '-');
		if (toLowerCase) {
			decomposed = decomposed.toLowerCase();
		}
		return decomposed;
	}

	public static boolean isAtLeastOneNotNullAndNotEmpty(String... strings) {

		for (String string : strings) {
			if (string != null && !string.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static String rozdel(String text) {
		final int delkaP = "<p>".length();

		//..druhy odstavec
		int index = org.apache.commons.lang.StringUtils.indexOf(text, "<p>", delkaP);
		if (index < 0) {
			return text;
		}

		//třetí odstavec
		int index2 = org.apache.commons.lang.StringUtils.indexOf(text, "<p>", index + delkaP);
		if (index2 < 0) {
			return text;
		}

		StringBuilder sb = new StringBuilder();
		//..úvod
		sb.append(text.substring(0, index2));
		sb.append(Clanek.P_DELIC);
		//..zbytek
		sb.append(text.substring(index2 + delkaP));
		return sb.toString();
	}
}
