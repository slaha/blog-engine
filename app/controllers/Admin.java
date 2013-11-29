package controllers;

import models.Clanek;
import models.Kategorie;
import models.Komentar;
import models.Uzivatel;
import play.Logger;
import play.mvc.With;

import java.util.List;
import java.util.Map;
@With(Secure.class)
public class Admin extends BlogApplicationBaseController {


	public static void index() {
		render();
	}

	/***************************************
	 *
	 * CLANKY
	 *
	 ***************************************/
	public static void clanky() {
		List<Clanek> clanky = Clanek.findAll();
		render(clanky);
	}


	public static void formClanek(Long id) {
		List<Kategorie> kategories = Kategorie.findAll();
	    if(id != null) {
	        Clanek clanek = Clanek.findById(id);
	        render(clanek, kategories);
	    }
	    render(kategories);
	}

	public static void ulozitClanek(Long id, String titulek, String text, Long kategorieId) {
	    Clanek clanek;
		Kategorie kategorie = Kategorie.findById(kategorieId);
	    if(id == null) {
	        Uzivatel autor = Uzivatel.find("byEmail", Security.connected()).first();
	        clanek = new Clanek(autor, titulek, text, kategorie);
	    } else {
	        clanek = Clanek.findById(id);
	        clanek.titulek = titulek;
	        clanek.text = text;
	        clanek.kategorie = kategorie;
	    }

	    // Validate
	    validation.valid(clanek);
	    if(validation.hasErrors()) {
		    List<Kategorie> kategories = Kategorie.findAll();
	        render("@formClanek", clanek, kategories);
	    }

	    clanek.save();
		flash.success("Článek \"%s\" byl úspěšně uložen", clanek.titulek);
		clanky();
	}

	public static void smazatClanek(Long id) {
        Clanek clanek = Clanek.findById(id);
		if (clanek == null) {
			logAndDisplayError("Clánek s ID %d nebyl nalezen", id);
			return;
		}

		String message;
		try {
            clanek.delete();
			message = String.format("Článek \"%s\" byl úspěšně smazán", clanek.titulek);
			flash.success(message);
		} catch (Exception ex) {
			message = String.format("Nepodařilo se smazat článek \"%s\" (ID: %d)", clanek.titulek, clanek.id);
			Logger.error(ex, message);
			flash.error(message);
		}
	    clanky();
	}

	/***************************************
	 *
	 * KOMENTARE
	 *
	 ***************************************/
	public static void komentare() {
		List<Komentar> komentare = Komentar.findAll();
		render(komentare);
	}


	public static void formKomentar(Long id) {
	    if(id != null) {
	        Komentar komentar = Komentar.findById(id);
	        render(komentar);
	    }
	    render();
	}

	public static void smazatKomentar(Long id) {
        Komentar komentar = Komentar.findById(id);
		if (komentar == null) {
			logAndDisplayError("Komentář s ID %d nebyl nalezen", id);
			return;
		}

		String message;
		try {
            komentar.delete();
			message = String.format("Komentář od uživatele \"%s\" k článku \"%s\" byl úspěšně smazán", komentar.autor, komentar.clanek.titulek);
			flash.success(message);
		} catch (Exception ex) {
			message = String.format("Nepodařilo se smazat komentář k článku \"%s\" ze dne %s od uživatele %s (ID: %d)",
				komentar.clanek.titulek, komentar.datumPoslani, komentar.autor, komentar.id);
			Logger.error(ex, message);
			flash.error(message);
		}
	    komentare();
	}

	/***************************************
	 *
	 * KATEGORIE
	 *
	 ***************************************/
	public static void kategorie() {
		//List<Kategorie> kategorie = Kategorie.findAll();
		List<Map> kategorie = Kategorie.find(
            "select new map(k.jmeno as jmeno, count(c.id) as pocet, k.id as id, k.url as url) from Clanek c right join c.kategorie as k group by k.jmeno order by pocet"
            ).fetch();
		render(kategorie);
	}


	public static void formKategorie(Long id) {
	    if(id != null) {
	        Kategorie kategorie = Kategorie.findById(id);
	        render(kategorie);
	    }
	    render();
	}

	public static void ulozitKategorii(Long id, String nazev) {
		Kategorie kategorie;
	    if(id == null) {
	        kategorie = new Kategorie(nazev);
	    } else {
	        kategorie = Kategorie.findById(id);
	        kategorie.setJmeno(nazev);
	    }

	    // Validate
	    validation.valid(kategorie);
	    if(validation.hasErrors()) {
	        render("@formKategorie", kategorie);
	    }

	    kategorie.save();
		flash.success("Kategorie \"%s\" byla úspěšně uložena", kategorie.getJmeno());
		kategorie();
	}

	public static void smazatKategorii(Long id) {
        Kategorie kategorie = Kategorie.findById(id);
		if (kategorie == null) {
			logAndDisplayError("Kategorie s ID %d nebyla nalezena", id);
			return;
		}

		String message;
		try {
            kategorie.delete();
			message = String.format("Kategorie \"%s\" byla úspěšně smazána", kategorie.getJmeno());
			flash.success(message);
		} catch (Exception ex) {
			message = String.format("Nepodařilo se smazat kategorii \"%s\" (ID: %d)", kategorie.getJmeno(), kategorie.id);
			Logger.error(ex, message);
			flash.error(message);
		}
	    kategorie();
	}
}