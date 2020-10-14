package hu.csapatnev.webshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.csapatnev.webshop.jpa.dao.model.ShopItem;
import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class HomeController {
	@Autowired
    private ShopItemService shopItems;
	
	@GetMapping("/")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		
		//ShopItem item = new ShopItem(0L, "Gyuri", 10L, 0, 1, null);
		//shopItems.create(item);
		
		List<ShopItem> topItems = shopItems.getBestOf(12);
		model.addAttribute("shopItems", topItems);

		return "home";
	}
}
