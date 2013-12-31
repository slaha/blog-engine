package models;

import net.sf.oval.constraint.MaxLength;
import org.hibernate.annotations.Type;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:45 */
@Entity
public class Komentar extends Model {

	@Required
	public String autor;

	@Required
    public Date datumPoslani;

    @Required
    @Lob
    @Field
    @Type(type = "org.hibernate.type.TextType")
    @MaxLength(1500)
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
