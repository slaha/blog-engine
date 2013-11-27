package controllers;

import models.Clanek;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;

import java.util.List;

public class Admin extends BlogApplicationBaseController {


    public static void index() {
	    String user = Security.connected();
        List<Clanek> clanky = Clanek.find("autor.email", user).fetch();
        render(clanky);
    }

}