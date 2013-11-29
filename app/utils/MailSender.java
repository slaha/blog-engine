package utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.libs.Mail;

/** Created with IntelliJ IDEA. User: slaha Date: 29.11.13 Time: 23:04 */
public class MailSender {

	public static void odeslat(String od, String predmet, String zprava, String...komu) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setFrom(od);
		for (String prijemce : komu) {
			email.addTo(prijemce);
		}
		email.setSubject(predmet);
		email.setMsg(zprava);
		Mail.send(email);
	}
}
