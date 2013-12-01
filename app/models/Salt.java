package models;

import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/** Created with IntelliJ IDEA. User: slaha Date: 1.12.13 Time: 13:51 */
@Entity
public class Salt extends Model {

	@Required
	private String salt;

	public Salt() {
		this.salt = BCrypt.gensalt(10);
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSalt() {
		return salt;
	}
}
