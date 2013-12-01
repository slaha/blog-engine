package models;

import org.apache.commons.lang.StringUtils;

/** Created with IntelliJ IDEA. User: slaha Date: 1.12.13 Time: 1:00 */
public class VysledekVyhledavani {
	private final String hledany;
	private final String text;

	public VysledekVyhledavani(Clanek clanek, String hledat) {
		this.hledany = " " + hledat + " ";
		this.text = clanek.text;
	}

	@Override
	public String toString() {
		int hledanyLength = hledany.length();
		int startPos = 0;
		int index;
		StringBuilder sb = new StringBuilder();
		int start;
		int poNajdutem;
		int stop = -1;
		while ((index = StringUtils.indexOfIgnoreCase(text, hledany, startPos)) >= 0)
		{
			start = Math.max(index - 30, 0);
			poNajdutem = index + hledanyLength;
			stop = Math.min(poNajdutem + 30, text.length());

			if (sb.length() > 0) {
				sb.append(" <b>&hellip;</b> ");
			}

			String poNajdutemString = text.substring(poNajdutem, stop);
			int moznyStop;
			if ((moznyStop = StringUtils.indexOfIgnoreCase(poNajdutemString, hledany)) >= 0) {
				stop = poNajdutem + moznyStop;
			}

			sb
				.append(text.substring(start, index + 1))
				.append("<mark>")
				.append(text.substring(index + 1, poNajdutem - 1))
				.append("</mark>")
				.append(text.substring(poNajdutem - 1, stop));

			startPos = stop;
		}

		return sb.toString();
	}
}
