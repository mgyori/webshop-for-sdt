package hu.csapatnev.webshop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import hu.csapatnev.webshop.Utils;
import hu.csapatnev.webshop.jpa.model.ShopItem;
import hu.csapatnev.webshop.jpa.service.ShopItemService;
import hu.csapatnev.webshop.session.CartItem;

@Controller
public class DetailsController {
	
	@Autowired
    private ShopItemService shopItems;
	
	
	@RequestMapping(value = "/details")
	public String getDetails(Model model) {
		return "notfound";
	}
	
	@RequestMapping(value = "/details/{link}")
	public String getDetails(@PathVariable String link, Model model) {
		ShopItem item = shopItems.findByLink(link);
		
		if(link.isEmpty() || item == null) {
			return "notfound";
		} else {
			model.addAttribute("itemName", item.getName());
			model.addAttribute("itemImage", item.getImage());
			model.addAttribute("itemStock", item.getInstock());
			model.addAttribute("itemPrice", item.getPrice());
			model.addAttribute("itemShortDesc", item.getShortDescription());
			model.addAttribute("itemDesc", item.getDescription());
			model.addAttribute("itemId", item.getId());
			
			model.addAttribute("recommended", shopItems.getByCategory(item.getCategory(), 3));
			
			model.addAttribute("current", "details");
			return "details";
		}
		
	}
	
	@RequestMapping(value = "/details/{link}", method = RequestMethod.POST)
	public ResponseEntity<List<CartItem>> postDetails(HttpServletRequest request, @PathVariable String link, Model model) {
		ShopItem item = shopItems.findByLink(link);
		
		if(link.isEmpty() || item == null)
			return null;
		
		List<CartItem> items = Utils.getCartItems(request);
		
		String count = request.getParameter("count");
		if (count != null) {
			boolean isNew = true;
			for (CartItem i : items) {
				if (i.getItem().getId().equals(item.getId())) {
					i.setCount(i.getCount() + Integer.parseInt(count));
					model.addAttribute("addedMoreToCart", count);
					isNew = false;
					break;
				}
			}
			
			if (isNew) {
				items.add(new CartItem(item, Integer.parseInt(count)));
				model.addAttribute("addedToCart", count);
			}
			
			request.getSession().setAttribute("cart", items);
		}
		
		return new ResponseEntity<List<CartItem>>(items, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/removeCartItem/{id}", method = RequestMethod.POST)
	public ResponseEntity<List<CartItem>> removeItemFromCart(HttpServletRequest request, @PathVariable Long id, Model model) {
		List<CartItem> cart = Utils.getCartItems(request);
		if (cart.size() == 1)
			cart.clear();
		else
			for (CartItem c : cart)
				if (c.getItem().getId().equals(id))
					cart.remove(c);
		if (cart == null || cart.size() == 0)
			cart = new ArrayList<CartItem>();
		request.getSession().setAttribute("cart", cart);
		return new ResponseEntity<List<CartItem>>(cart, HttpStatus.OK);
	}
}
