package controllers;

import jobs.Bootstrap;
import models.Uzivatel;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:36 */
public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        return Uzivatel.login(username, password) != null;
    }

	static void onDisconnected() {
        Admin.clanky();
	}

	static void onAuthenticated() {
		if (Bootstrap.DEFAULT_EMAIL.equals(Security.connected())) {
			flash.error("Nastavte si prosím svůj uživatelský profil.");
			Admin.profil(true);
		}

        Admin.clanky();
	}


}