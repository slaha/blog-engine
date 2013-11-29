package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/** Created with IntelliJ IDEA. User: slaha Date: 22.11.13 Time: 14:40 */
@Entity
public class Uzivatel extends Model {

	public String email;
    public String heslo;
    public String celeJmeno;

    public Uzivatel(String email, String heslo, String celeJmeno) {
        this.email = email;
        this.heslo = heslo;
        this.celeJmeno = celeJmeno;
    }

	public static Uzivatel login(String username, String password) {
		return find("byEmailAndHeslo", username, password).first();
	}
}
