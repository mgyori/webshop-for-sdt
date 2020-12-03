package hu.csapatnev.webshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import hu.csapatnev.webshop.Config;
import hu.csapatnev.webshop.Utils;
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
	
	@ModelAttribute("cartItems")
	public List<CartItem> cartItems(HttpServletRequest request) {
		return Utils.getCartItems(request);
	}
	
	@ModelAttribute("cartCount")
	public int cartCount(HttpServletRequest request) {
		List<CartItem> cart = Utils.getCartItems(request);
		int count = 0;
		for (CartItem c : cart)
			count += c.getCount();
		return count;
	}
	
	@ModelAttribute("cartPrice")
	public int cartPrice(HttpServletRequest request) {
		List<CartItem> cart = Utils.getCartItems(request);
		int price = 0;
		for (CartItem c : cart)
			price += c.getCount() * c.getItem().getPrice();
		return price;
	}
}
