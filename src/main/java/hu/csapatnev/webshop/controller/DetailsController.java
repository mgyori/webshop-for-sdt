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
			
			model.addAttribute("recommended", shopItems.getByCategory(item.getCategory(), 3));
			
			return "details";
		}
		
	}
}
