package controllers;

import models.Clanek;
import models.Kategorie;
import org.apache.commons.mail.EmailException;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Http;
import utils.MailSender;
import utils.StringUtils;

import java.util.List;

import static play.modules.pdf.PDF.Options;
import static play.modules.pdf.PDF.renderPDF;

public class Application extends BlogApplicationBaseController {

	public static void index() {
		List<Clanek> starsiClanky = Clanek.find("order by datumNapsani desc").from(0).fetch(10);
		Clanek nejnovejsiClanek = null;
		if (!starsiClanky.isEmpty()) {
			nejnovejsiClanek = starsiClanky.remove(0);
		}

		render(nejnovejsiClanek, starsiClanky);
	}

	public static void kontakt() {
		String randomID = Codec.UUID();
		render(randomID);
	}

	public static void kontaktOdeslat(@Required String od, @Required String zprava, Boolean kopie,
			@Required String code, @Required String randomID) {

		validation.equals(code, Cache.get(randomID)).message("Kód z obrázku byl špatně opsán.");
		validation.email(od);

		if (Validation.hasErrors()) {
			flash.error("E-mail nebyl odeslán, protože formulář nebyl správně vyplněn.");
			render("Application/kontakt.html", od, zprava, kopie, randomID, kopie);
		}

		String predmet = "Mail z webu " + renderArgs.get("blogNazev");
		String defaultEmail = Play.configuration.getProperty("blog.email");

		String[] emailsTo;
		if (Boolean.TRUE.equals(kopie)) {
			emailsTo = new String[]{ defaultEmail, od };
		} else {
			emailsTo = new String[]{ defaultEmail };
		}

		try {
			MailSender.odeslat(od, predmet, zprava, emailsTo);
			flash.success("E-mail byl úspěšně odeslán");
		} catch (EmailException e) {
			flash.error("E-mail se nepodařilo odeslat. Zkuste to prosím později.");
		}

		kontakt();
	}

	public static void show(Long id, String titulek) {
		Clanek clanek = Clanek.findById(id);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", id);
		}

		String randomID = Codec.UUID();

		Clanek predchozi = clanek.predchozi();
		Clanek nasledujici = clanek.nasledujici();
		render(clanek, predchozi, nasledujici, randomID);
	}

	public static void tisk(Long id) {
		Clanek clanek = Clanek.findById(id);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", id);
		}

		renderTemplate("Application/pdf.html", clanek);
	}

	public static void pdf(Long id) {
		Clanek clanek = Clanek.findById(id);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", id);
		}

		String title = StringUtils.normalize(clanek.titulek, false);
		Options options = new Options();
		options.filename = title + ".pdf";

		renderPDF(clanek, options, title);
	}

	public static void kategorie(String kategorieUrl) {
		Kategorie kategorie = Kategorie.find("byUrl", kategorieUrl).first();
		if (kategorie == null) {
			logAndDisplayError("Kategorie %s nebyla nalezena", kategorieUrl);
			return;
		}
		List<Clanek> clanky = Clanek.find("byKategorie", kategorie).fetch();

		render(clanky, kategorie);
	}

	public static void pridatKomentar(Long postId, @Required String autor, @Required String komentar, String code,
			String randomID) {

		Clanek clanek = Clanek.findById(postId);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", postId);
		}

		if (Security.connected() == null) {
			validation.equals(code, Cache.get(randomID)).message("Kód z obrázku byl špatně opsán.");
		}

		if (validation.hasErrors()) {
			Clanek predchozi = clanek.predchozi();
			Clanek nasledujici = clanek.nasledujici();
			render("Application/show.html", clanek, randomID, predchozi, nasledujici);
		}
		clanek.pridatKomentar(autor, komentar);
		flash.success("Komentář byl uložen.");
		show(postId, clanek.titulek);
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#000");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

}