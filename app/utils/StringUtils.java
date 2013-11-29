package utils;

import java.text.Normalizer;

/** Created with IntelliJ IDEA. User: slaha Date: 28.11.13 Time: 23:30 */
public class StringUtils {

	public static String normalize(final String str) {
		String decomposed = java.text.Normalizer.normalize(str, Normalizer.Form.NFD);
		return  decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace(' ', '-');
	}

	public static boolean isAtLeastOneNotNullAndNotEmpty(String... strings) {

		for (String string : strings) {
			if (string != null && !string.isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
