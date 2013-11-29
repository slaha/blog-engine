package models;

import play.db.jpa.JPABase;

/** Created with IntelliJ IDEA. User: slaha Date: 29.11.13 Time: 10:10 */
public class OptionPlaceholder extends JPABase {

	Long id = null;

	private String text;

	public OptionPlaceholder(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
