package controllers;

import models.Clanek;
import models.Kategorie;
import models.VysledekVyhledavani;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Codec;
import play.libs.Images;
import play.modules.search.Query;
import play.modules.search.Search;
import play.mvc.Router;
import utils.BlogStringUtils;
import utils.MailSender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.modules.pdf.PDF.Options;
import static play.modules.pdf.PDF.renderPDF;

public class Application extends BlogApplicationBaseController {

	private static final int CLANKU_NA_STRANKU = 3;

	public static void index(Integer aktualniStranka) {
		if (aktualniStranka == null) {
			index(1);
		} else if (aktualniStranka < 1) {
			index(1);
		}

		final long pocetClanku = Clanek.count();
		int pocetStranek = 1;
		if (pocetClanku > 0) {
			pocetStranek = (int) Math.ceil(pocetClanku / (double)CLANKU_NA_STRANKU);
			if (aktualniStranka > pocetStranek) {
				index(pocetStranek);
			}
		}
		int _z = (aktualniStranka - 1) * (int)CLANKU_NA_STRANKU;
		int _do = (int)CLANKU_NA_STRANKU;

		List<Clanek> clanky = Clanek.find("order by datumNapsani desc").from(_z).fetch(_do);


		render(clanky, pocetStranek, aktualniStranka);
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
		String url = null;
		try {
			url = URLEncoder.encode("http://localhost:9000/clanek/9/4.-clanek", "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}
		render(clanek, predchozi, nasledujici, randomID, url);
	}

	public static void hledat(String hledat) {


		if (StringUtils.isBlank(hledat)) {
			Validation.addError("hledani", "Musíte zadat řetězec, který má být vyhledán. Nesmí být prázdný.");
			render();
		}

		Query q = Search.search(hledat, Clanek.class);

		List<Clanek> vysledky = q.fetch();

		Map<Clanek, VysledekVyhledavani> clanky = new HashMap<Clanek, VysledekVyhledavani>();
		for (Clanek clanek : vysledky) {
			clanky.put(clanek, new VysledekVyhledavani(clanek, hledat));
		}
		render(clanky, hledat);

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

		String title = BlogStringUtils.normalize(clanek.titulek, false);
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
		//show(postId, clanek.getUrl());
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("id", postId);
		args.put("titulek", clanek.getUrl());
		Router.ActionDefinition url = Router.reverse("Application.show", args).addRef(String.format("komentar%d", clanek.getPosledniKomentarCislo()));
		redirect(url.url);
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#000");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

}