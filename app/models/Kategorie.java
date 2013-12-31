package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import utils.BlogStringUtils;

import javax.persistence.Entity;
import java.util.List;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:26 */
@Entity
public class Kategorie extends Model {

	@Required
	@Unique
	private String jmeno;

	@Required
	@Unique
	private String url;

	public Kategorie(String jmeno) {
		setJmeno(jmeno);
	}

	public String getJmeno() {
		return jmeno;
	}

	public void setJmeno(String jmeno) {
		this.jmeno = jmeno;
		final String url = BlogStringUtils.normalize(jmeno, true);
		this.url = url;
		boolean exists;
		int id = 0;
		do {
			Kategorie k = find("byUrl", this.url).first();
			exists = ( k != null );
			if (exists) {
				this.url = url + "_" + (++id);
			}
		} while (exists);
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return jmeno;
	}

	public static List<Kategorie> findAllSortedByName() {
		return find("order by jmeno").fetch();
	}
}
