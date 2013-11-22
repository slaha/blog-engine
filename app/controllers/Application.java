package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends BlogApplicationBaseController {


    public static void index() {
	    List<Clanek> starsiClanky = Clanek.find("order by datumNapsani desc").from(0).fetch(10);
	    Clanek nejnovejsiClanek = null;
	    if (!starsiClanky.isEmpty()) {
	        nejnovejsiClanek = starsiClanky.remove(0);
	    }

	    render(nejnovejsiClanek, starsiClanky);
    }

	public static void show(Long id) {
	    Clanek clanek = Clanek.findById(id);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", id);
		}

		String randomID = Codec.UUID();

	    render(clanek, randomID);
	}


	public static void pridatKomentar(Long postId,
	                                  @Required String autor,
	                                  @Required String komentar,
	                                  @Required String code,
	                                  String randomID)  {

	    Clanek clanek = Clanek.findById(postId);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", postId);
		}

		validation.equals(code, Cache.get(randomID)).message("Kód z obrázku byl špatně opsán.");
		if (validation.hasErrors()) {
            render("Application/show.html", clanek);
        }
	    clanek.pridatKomentar(autor, komentar);
		flash.success("Komentář byl uložen.");
		show(postId);
	}


	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#000");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

}