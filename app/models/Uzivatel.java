package models;

import org.apache.commons.mail.EmailAttachment;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Email;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:40 */
@Entity
public class Uzivatel extends Model {

	@Required
	@Email
	@Unique
	public String email;

	@Required
	@MinSize(6)
    public String heslo;

	@Required
    public String celeJmeno;

	@OneToOne
	private Salt salt;

    public Uzivatel(String email, String heslo, String celeJmeno) {
        this.email = email;
        this.celeJmeno = celeJmeno;

	    this.salt = new Salt().save();

	    nastavHeslo(heslo);
    }

	public static Uzivatel login(String username, String password) {
		Uzivatel u = find("byEmail", username).first();

		if (u == null) {
			return null;
		}
		if (BCrypt.checkpw(password, u.heslo)) {
			return u;
		}
		return null;
	}

	@Override
	public String toString() {
		return celeJmeno;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Uzivatel)) return false;
		if (!super.equals(o)) return false;

		Uzivatel uzivatel = (Uzivatel) o;

		if (celeJmeno != null ? !celeJmeno.equals(uzivatel.celeJmeno) : uzivatel.celeJmeno != null) return false;
		if (email != null ? !email.equals(uzivatel.email) : uzivatel.email != null) return false;
		if (heslo != null ? !heslo.equals(uzivatel.heslo) : uzivatel.heslo != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (heslo != null ? heslo.hashCode() : 0);
		result = 31 * result + (celeJmeno != null ? celeJmeno.hashCode() : 0);
		return result;
	}

	public void nastavHeslo(String noveHeslo) {
		this.heslo = BCrypt.hashpw(noveHeslo, salt.getSalt());
	}

	public String hashPassword(String heslo) {
		return BCrypt.hashpw(heslo, salt.getSalt());
	}
}
