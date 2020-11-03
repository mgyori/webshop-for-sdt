package hu.csapatnev.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hu.csapatnev.webshop.jpa.model.ShopItem;
import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Controller
public class DetailsController {
	
	@Autowired
    private ShopItemService shopItems;
	
	
	@RequestMapping(value = "/details")
	public String getDetails(Model model) {
		return "notfound";
	}
	
	@RequestMapping(value = "/details/{id}")
	public String getDetails(@PathVariable long id, Model model) {
		ShopItem item = shopItems.findOne(id);
		
		if(id == 0 || item == null) {
			return "notfound";
		} else {
			model.addAttribute("itemName", item.getName());
			model.addAttribute("itemImage", item.getImage());
			return "details";
		}
		
	}
}
