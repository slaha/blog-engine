package controllers;

import models.Clanek;
import play.mvc.With;

/** Created with IntelliJ IDEA. User: slaha Date: 27.11.13 Time: 23:32 */
@CRUD.For(Clanek.class)
@With(Secure.class)
public class Clanky extends CRUD {
}
