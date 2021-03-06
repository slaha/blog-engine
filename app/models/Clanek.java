package models;

import org.hibernate.annotations.Type;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;
import utils.BlogStringUtils;
import utils.KomentarUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:42 */
@Entity
@Indexed
public class Clanek extends Model {

	public static final String DELIC_ID = "text-delic";
	public static final String P_DELIC = "<p id='" + DELIC_ID + "'>";

	@Required
	@ManyToOne
	public Kategorie kategorie;

	@Required
	@Field
	public String titulek;

	@Required
    public Date datumNapsani;

    @Lob
    @Field
    @Type(type = "org.hibernate.type.TextType")
    public String text;

    @ManyToOne
    @Required
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
		komentar = KomentarUtils.escape(komentar, this);
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

	public String getUrl() {
		return BlogStringUtils.normalize(titulek, true);
	}

	public String getOdstavec() {
		final int delkaP = "</p>".length();

		int index = text.indexOf(P_DELIC);
		if (index < 0) {
			return text;
		}

		return text.substring(0, index);
	}

	public boolean isJeDelsi() {
		return text.contains(P_DELIC);
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

	public int getPosledniKomentarCislo() {

		return komentare.size();
	}
}
