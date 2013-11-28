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

	/*************************
	 *
	 * STUFF
	 */

	@Override
	public String toString() {
		return titulek;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Clanek)) return false;
		if (!super.equals(o)) return false;

		Clanek clanek = (Clanek) o;

		if (autor != null ? !autor.equals(clanek.autor) : clanek.autor != null) return false;
		if (datumNapsani != null ? !datumNapsani.equals(clanek.datumNapsani) : clanek.datumNapsani != null)
			return false;
		if (kategorie != null ? !kategorie.equals(clanek.kategorie) : clanek.kategorie != null) return false;
		if (komentare != null ? !komentare.equals(clanek.komentare) : clanek.komentare != null) return false;
		if (text != null ? !text.equals(clanek.text) : clanek.text != null) return false;
		if (titulek != null ? !titulek.equals(clanek.titulek) : clanek.titulek != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (kategorie != null ? kategorie.hashCode() : 0);
		result = 31 * result + (titulek != null ? titulek.hashCode() : 0);
		result = 31 * result + (datumNapsani != null ? datumNapsani.hashCode() : 0);
		result = 31 * result + (text != null ? text.hashCode() : 0);
		result = 31 * result + (autor != null ? autor.hashCode() : 0);
		result = 31 * result + (komentare != null ? komentare.hashCode() : 0);
		return result;
	}
}
