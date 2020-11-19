package hu.csapatnev.webshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.orders.Order;

import hu.csapatnev.webshop.paypal.PaypalOrder;

@Controller
public class CheckoutController {
	@RequestMapping(value = {"/checkout", "/checkout/cancel", "/checkout/return"})
	public String checkoutCrt(HttpServletRequest request, HttpServletResponse resp, Model model) {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] args = path.split("/");
		
		if (args.length > 2 && args[2].trim().equalsIgnoreCase("return")) {
			String token = request.getParameter("token");
			//String payer = request.getParameter("PayerID");
			
			boolean state = PaypalOrder.processOrder(token);
			model.addAttribute("state", state);
			return "checkoutreturn";
		}
		
		return "checkout";
	}
	
	@RequestMapping(value = "/order")
	public RedirectView orderCart(Model model) {
		Order order = PaypalOrder.newOrder(1000);
		return new RedirectView(order.links().get(1).href());
	}
}
