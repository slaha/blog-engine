package controllers;

import models.Kategorie;
import models.Uzivatel;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:38 */
public abstract class BlogApplicationBaseController extends Controller {

	@Before
	static void addDefaults() {
		if(Security.isConnected()) {
            Uzivatel uzivatel = Uzivatel.find("byEmail", Security.connected()).first();
            renderArgs.put("uzivatel", uzivatel);
        }
		renderArgs.put("blogNazev", Play.configuration.getProperty("blog.title"));
		renderArgs.put("blogPodtitulek", Play.configuration.getProperty("blog.baseline"));
		renderArgs.put("vsechnyKategorie", Kategorie.findAllSortedByName());
	}


	protected static void logAndDisplayError(String message, Object...args) {
		String str = String.format(message, args);
		Logger.error(str);
		error(str);
	}

	protected static void logAndDisplayJsonError(String message, Object...args) {
		String str = String.format(message, args);
		Logger.error(str);
		response.status = 500;
		renderJSON(str);
	}
}
