package controllers;

import models.Uzivatel;
import org.mindrot.jbcrypt.BCrypt;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:36 */
public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        return Uzivatel.login(username, password) != null;
    }

	static void onDisconnected() {
        Admin.clanky();
	}

	static void onAuthenticated() {
        Admin.clanky();
	}


}