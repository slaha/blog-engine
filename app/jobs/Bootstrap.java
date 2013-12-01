package jobs;

import models.*;
import org.apache.commons.lang.StringUtils;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:08 */
@OnApplicationStart
public class Bootstrap extends Job {

	public static final String DEFAULT_EMAIL = "admin@admin.admin";
	public static final String DEFAULT_PASS = "password";

	public void doJob() {
        // Check if the database is empty
		/*Komentar.deleteAll();
		Clanek.deleteAll();
		Kategorie.deleteAll();
		Uzivatel.deleteAll();
		Salt.deleteAll();
*/
		Uzivatel uzivatel = Uzivatel.find("byEmail", "admin@admin.admin").first();
		if (uzivatel != null) {
			uzivatel.delete();
		}

        if(Uzivatel.count() == 0) {
	        new Uzivatel(DEFAULT_EMAIL, DEFAULT_PASS, StringUtils.EMPTY).save();
        }
        /*
        Kategorie kategorie = new Kategorie("kategorie 1").save();
        new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();
        new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();

        kategorie = new Kategorie("Kategorie 2").save();
        new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();
        */
    }

	private String clanek() {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 50; i++) {
			sb.append("Lorem ipsum dolor sit amet");

			if (i % 10 == 0) {
				sb.append('\n').append('\n');
			}
		}
		return sb.toString();
	}
}
