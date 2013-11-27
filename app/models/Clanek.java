package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:42 */
@Entity
public class Clanek extends Model {

	@Required
	@ManyToOne
	public Kategorie kategorie;

	@Required
	public String titulek;

    public Date datumNapsani;

    @Lob
    public String text;

    @ManyToOne
    public Uzivatel autor;

	@OneToMany(mappedBy="clanek", cascade= CascadeType.ALL)
	public List<Komentar> komentare;

    public Clanek(Uzivatel autor, String titulek, String text, Kategorie kategorie) {
        this.autor = autor;
        this.titulek = titulek;
        this.text = text;
	    this.kategorie = kategorie;
        this.datumNapsani = new Date();
	    this.komentare = new ArrayList<Komentar>();
    }

	public Clanek pridatKomentar(String autor, String komentar) {
	    Komentar newComment = new Komentar(this, autor, komentar).save();
	    this.komentare.add(newComment);
	    this.save();
	    return this;
	}

	public Clanek predchozi() {
	    return Clanek.find("datumNapsani < ? order by datumNapsani desc", datumNapsani).first();
	}

	public Clanek nasledujici() {
        return Clanek.find("datumNapsani > ? order by datumNapsani asc", datumNapsani).first();
	}
}
