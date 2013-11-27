import models.Clanek;
import models.Kategorie;
import models.Komentar;
import models.Uzivatel;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:08 */
@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        // Check if the database is empty
	/*	Komentar.deleteAll();
		Kategorie.deleteAll();
		Clanek.deleteAll();
		Uzivatel.deleteAll();
*/
        if(Clanek.count() == 0) {
	        Uzivatel uzivatel = new Uzivatel("jan@slahora.cz", "jan", "Jan Šlahora").save();

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
