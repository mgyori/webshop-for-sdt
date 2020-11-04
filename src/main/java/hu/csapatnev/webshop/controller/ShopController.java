package hu.csapatnev.webshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.csapatnev.webshop.Utils;
import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class ShopController {
	private int limit = 20;

	@Autowired
	private ShopItemService shopItems;

	private String getValidSortOption(String sort) {
		switch(sort) {
			case "id":
			case "name":
			case "price":
				return sort;
			default:
				return "id";
		}
	}

	@GetMapping(value = {"/shop", "/shop/**"})
	public String getDefault(HttpServletRequest request, Model model) {
		HashMap<String, String> args = Utils.parseUrlFromRequest(request);
		int page;
		try {
			page = args.containsKey("page") ? Integer.parseInt(args.get("page")) : 0;
		} catch(Exception e ) {
			page = 0;
		}
		String sort = getValidSortOption(args.containsKey("sort") ? args.get("sort") : "id");
		
		model.addAttribute("items", shopItems.getItems(page, limit, sort));
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		
		return "shop";
	}
}
