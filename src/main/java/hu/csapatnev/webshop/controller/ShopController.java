package hu.csapatnev.webshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.csapatnev.webshop.UrlParser;
import hu.csapatnev.webshop.Utils;
import hu.csapatnev.webshop.jpa.service.ShopCategoryService;
import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class ShopController {
	private int limit = 20;

	@Autowired
	private ShopItemService shopItems;
	
	@Autowired
	private ShopCategoryService shopCategories;

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
		UrlParser args = Utils.parseUrlFromRequest(request);
		int page = args.getParamInt("page");
		String sort = getValidSortOption(args.getParamString("sort", "id"));
		int category = args.getParamInt("category");
		
		model.addAttribute("categories", shopCategories.findAll());
		model.addAttribute("items", shopItems.getItems(page, limit, sort, category));
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		
		return "shop";
	}
}
