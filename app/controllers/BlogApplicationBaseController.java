package controllers;

import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:38 */
public abstract class BlogApplicationBaseController extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("blogNazev", Play.configuration.getProperty("blog.title"));
		renderArgs.put("blogPodtitulek", Play.configuration.getProperty("blog.baseline"));
	}


	protected static void logAndDisplayError(String message, Object...args) {
		String str = String.format(message, args);
		Logger.error(str);
		error(str);
	}
}
