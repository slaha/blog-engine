package utils;
import models.Clanek;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
/** Created with IntelliJ IDEA. User: slaha Date: 29.11.13 Time: 0:22 */
public class KomentarUtils {


	public static String escape(String source, Clanek clanek) {
		String escaped = escapeHtml(source);

	//	escaped = escaped.replaceAll("\\[([0-9]+)\\]", "<a href='#komentar$1'> ↑ [$1]</a>");

		escaped = vytvoritOdkazyNaJineKomentare(source, clanek);

		escaped = escaped.replaceAll("\\[a ((http|https|ftp)://.+)\\](.+)\\[/a\\]", "<a href='$1'>$3</a>");

		escaped = escaped.replaceAll("\\r?\\n\\r?\\n", "<p>");

		escaped = escaped.replaceAll("\\r?\\n", "<br>");

		return escaped;
	}

	private static String vytvoritOdkazyNaJineKomentare(String source, Clanek clanek) {
		Pattern p = Pattern.compile("\\[([0-9]+)\\]");  // insert your pattern here
		Matcher m = p.matcher(source);
		StringBuilder sb = new StringBuilder();
		int start = 0;
		int konec;
		while (m.find()) {
			sb.append(source.substring(start, m.start()));
			start = m.start();
			konec = m.end();
			String str = source.substring(start+1, konec-1);
			int komentarId = Integer.parseInt(str);
			String autor = clanek.komentare.get(komentarId - 1).autor;
			String odkaz = String.format("<a href='#komentar%d'> ↑ [%d %s]</a>", komentarId, komentarId, autor);
			sb.append(odkaz);
			start = konec;
		}

		sb.append(source.substring(start));

		return sb.toString();
	}
}