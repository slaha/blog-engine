package controllers;

/** Created with IntelliJ IDEA. User: slaha Date: 29.11.13 Time: 11:01 */
public class Login extends BlogApplicationBaseController {

	public static void login() {
		renderTemplate("Secure/login.html");
	}
}
