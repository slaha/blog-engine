package controllers;

import play.*;
import play.data.validation.Required;
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

	    render(clanek);
	}


	public static void pridatKomentar(Long postId, @Required String autor, @Required String komentar) {
	    Clanek clanek = Clanek.findById(postId);

		if (clanek == null) {
			logAndDisplayError("Článek s id %d nebyl nalezen v databázi", postId);
		}

		if (validation.hasErrors()) {
            render("Application/show.html", clanek);
        }
	    clanek.pridatKomentar(autor, komentar);
		flash.success("Komentář byl uložen.");
		show(postId);
	}



}