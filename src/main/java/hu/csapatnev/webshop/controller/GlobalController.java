package hu.csapatnev.webshop.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import hu.csapatnev.webshop.Config;

@ControllerAdvice
public class GlobalController {
	@ModelAttribute("baseUrl")
    public String baseUrl() {
        return Config.getInstance().getProperties().getProperty("site.url");
    }
	
	@ModelAttribute("siteName")
    public String siteName() {
        return Config.getInstance().getProperties().getProperty("site.name");
    }
}
