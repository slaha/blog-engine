import models.*;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:08 */
@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        // Check if the database is empty
		/*Komentar.deleteAll();
		Clanek.deleteAll();
		Kategorie.deleteAll();
		Uzivatel.deleteAll();
		Salt.deleteAll();
*/
        if(Clanek.count() == 0) {
	        Uzivatel uzivatel = new Uzivatel("jan@slahora.cz", "janjan", "Jan Šlahora").save();

	        Kategorie kategorie = new Kategorie("kategorie 1").save();
            new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();
            new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();

	        kategorie = new Kategorie("Kategorie 2").save();
            new Clanek(uzivatel, "clanek 1", clanek(), kategorie).save();
        }
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
