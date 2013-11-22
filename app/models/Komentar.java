package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:45 */
@Entity
public class Komentar extends Model {

	public String autor;

    public Date datumPoslani;

    @Lob
    public String komentar;

    @ManyToOne
    public Clanek clanek;

    public Komentar(Clanek clanek, String autor, String komentar) {
        this.clanek = clanek;
        this.autor = autor;
        this.komentar = komentar;
        this.datumPoslani = new Date();
    }
}
