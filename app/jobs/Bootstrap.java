package jobs;

import models.Uzivatel;
import org.apache.commons.lang.StringUtils;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 15:08 */
@OnApplicationStart
public class Bootstrap extends Job {

	public static final String DEFAULT_EMAIL = "admin@admin.admin";
	public static final String DEFAULT_PASS = "password";

	public void doJob() {

        if (Uzivatel.count() == 0) {
	        new Uzivatel(DEFAULT_EMAIL, DEFAULT_PASS, StringUtils.EMPTY).save();
        }

    }
}
