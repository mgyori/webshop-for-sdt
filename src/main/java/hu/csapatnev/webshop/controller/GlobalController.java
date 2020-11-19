package hu.csapatnev.webshop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import hu.csapatnev.webshop.Config;
import hu.csapatnev.webshop.session.CartItem;

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
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("cartItems")
	public List<CartItem> cartItems(HttpServletRequest request) {
		List<CartItem> items = (List<CartItem>) request.getSession().getAttribute("cart");
		if (items == null)
			items = new ArrayList<CartItem>();
		return items;
	}
}
