package hu.csapatnev.webshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.csapatnev.webshop.UrlParser;
import hu.csapatnev.webshop.Utils;
import hu.csapatnev.webshop.jpa.model.ShopItem;
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
		int page = Math.max(1, args.getParamInt("page"));
		String sort = getValidSortOption(args.getParamString("sort", "id"));
		int category = args.getParamInt("category");
		String search = args.getParamString("search");
		
		Page<ShopItem> items = shopItems.getItems(page - 1, limit, sort, category, search);
		List<Integer> pagination = Utils.buildPagination(items, 5);
		args.removeParam("page");
		
		model.addAttribute("url", args.buildUrl());
		model.addAttribute("search", search);
		model.addAttribute("categories", shopCategories.findAll());
		model.addAttribute("items", items.getContent());
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		model.addAttribute("pagination", pagination);
		model.addAttribute("maxPage", items.getTotalPages() - 1);
		model.addAttribute("category", category);
		
		model.addAttribute("current", "shop");
		return "shop";
	}
}
