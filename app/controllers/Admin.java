package controllers;

import models.*;
import play.Logger;
import play.data.validation.Validation;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import play.mvc.With;
import utils.StringUtils;

import java.util.ArrayList;
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
		List<Clanek> clanky = Clanek.find("order by datumNapsani desc").fetch();
		render(clanky);
	}


	public static void formClanek(Long id) {
	    if(id != null) {
	        Clanek clanek = Clanek.findById(id);
	        render(clanek);
	    }
	    render();
	}

	public static void ulozitClanek(Long id, String titulek, String text, Long kategorieId) {
	    Clanek clanek;
		Kategorie kategorie = Kategorie.findById(kategorieId);

		text = StringUtils.rozdel(text);

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
	        render("@formClanek", clanek);
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
	public static void komentare(Long kategorieId, Long clanekId) {
		Kategorie kategorie = null;
		Clanek clanek = null;
		if (kategorieId != null) {
			kategorie = Kategorie.findById(kategorieId);
		}
		if (clanekId != null) {
			clanek = Clanek.findById(clanekId);
			if (kategorie != null && clanek.kategorie.id != kategorieId) {
				clanek = null;
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("select kom from Komentar kom");
		if (kategorie != null) {
			sb
				.append("   join kom.clanek as clanek")
				.append("   where clanek.kategorie = ?");
		}

		if (clanek != null) {

			if (kategorie == null) {
				sb.append(" where ");
			} else {
				sb.append("and");
			}
			sb.append(" clanek = ?");
		}

		sb.append(" order by kom.datumPoslani desc kom.clanek.titulek asc");

		GenericModel.JPAQuery komentareQuery = null;


		if (kategorie != null && clanek != null) {
			komentareQuery = Komentar.find(sb.toString(), kategorie, clanek);

		} else if (kategorie  == null && clanek != null) {
			komentareQuery = Komentar.find(sb.toString(), clanek);

		} else if (kategorie  != null && clanek == null) {
			komentareQuery = Komentar.find(sb.toString(), kategorie);

		} else {
			komentareQuery = Komentar.find(sb.toString());
		}
		List<JPABase> clanky;
		if (kategorie == null) {
			clanky = Clanek.findAll();
		} else  {
			clanky = Clanek.find("byKategorie", kategorie).fetch();
		}

		clanky.add(0, new OptionPlaceholder("Všechny články"));
		List<Komentar> komentare = komentareQuery.fetch();

		List<JPABase> kategorieOptions = new ArrayList<JPABase>((List<Kategorie>) renderArgs.get("vsechnyKategorie"));
		kategorieOptions.add(0, new OptionPlaceholder("Všechny kategorie"));

		render(komentare, kategorie, kategorieOptions, clanky, kategorieId, clanekId);
	}


	public static void formKomentar(Long id) {
	    if(id != null) {
	        Komentar komentar = Komentar.findById(id);
	        render(komentar);
	    }
	    render();
	}

	public static void smazatKomentar(Long id, Long kategorieId, Long clanekId) {
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
	    komentare(kategorieId, clanekId);
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

	/***************************************
	 *
	 * PROFIL
	 *
	 ***************************************/

	public static void profil() {
		render();
	}

	public static void ulozitProfil(String jmeno,
	                                String email,
	                                String heslo,
	                                String noveHeslo,
	                                String noveHeslo2) {


		System.out.println(jmeno +" -- " +  email +" -- " +  heslo +" -- " +  noveHeslo +" -- " +  noveHeslo2);

		Uzivatel uzivatel = Uzivatel.find("byEmail", Security.connected()).first();
		uzivatel.celeJmeno = jmeno;

		boolean zmenaEmail = false;
		if (!uzivatel.email.equals(email)) {
			//..musi se promitnout i do Security
			zmenaEmail = true;
		}
		uzivatel.email = email;

		if (StringUtils.isAtLeastOneNotNullAndNotEmpty(heslo, noveHeslo, noveHeslo2)) {
			validation.equals(heslo, uzivatel.heslo);
			validation.equals(noveHeslo2, noveHeslo );

			if (!Validation.hasErrors()) {
				uzivatel.heslo = noveHeslo;
			}
		}

		validation.valid(uzivatel);
		if (Validation.hasErrors()) {
			flash.error("Chyba při ukládání profilu");
			render("@profil", uzivatel);
		}

		uzivatel.save();

		flash.success("Uživatelský profil \"%s\" byl úspěšně změněn", jmeno);
		if (zmenaEmail) {
			session.put("username", email);
		}

		profil();
	}
}