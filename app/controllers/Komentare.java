package controllers;

import models.Komentar;
import play.mvc.With;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:32 */
@CRUD.For(Komentar.class)
@With(Secure.class)
public class Komentare extends CRUD {
}
