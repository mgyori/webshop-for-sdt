package hu.csapatnev.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class HomeController {
	@Autowired
    private ShopItemService shopItems;
	
	@GetMapping(value = {"/", "/home"})
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		model.addAttribute("shopItems", shopItems.getBestOf(12));
		
		return "home";
	}
}
