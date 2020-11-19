package hu.csapatnev.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class HomeController {
	@Autowired
    private ShopItemService shopItems;
	
	@GetMapping(value = {"/", "/home"})
	public String greeting(Model model) {
		model.addAttribute("shopItems", shopItems.getBestOf(12));
		
		model.addAttribute("current", "home");
		return "home";
	}
}
