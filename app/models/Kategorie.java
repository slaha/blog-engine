package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Entity;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:26 */
@Entity
public class Kategorie extends Model {

	@Required
	@Unique
	private String jmeno;

	public Kategorie(String jmeno) {
		this.jmeno = jmeno;
	}

	public String getJmeno() {
		return jmeno;
	}

	public void setJmeno(String jmeno) {
		this.jmeno = jmeno;
	}

	@Override
	public String toString() {
		return jmeno;
	}
}
