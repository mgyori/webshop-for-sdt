package hu.csapatnev.webshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.orders.Order;

import hu.csapatnev.webshop.Utils;
import hu.csapatnev.webshop.jpa.model.OrderDetails;
import hu.csapatnev.webshop.jpa.service.OrderDetailsService;
import hu.csapatnev.webshop.paypal.PaypalOrder;
import hu.csapatnev.webshop.session.CartItem;

@Controller
public class CheckoutController {
	@Autowired
	private OrderDetailsService orders;
	
	@RequestMapping(value = {"/checkout", "/checkout/cancel", "/checkout/return"})
	public String checkoutCrt(HttpServletRequest request, HttpServletResponse resp, Model model) {
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] args = path.split("/");
		
		if (args.length > 2 && args[2].trim().equalsIgnoreCase("return")) {
			String token = request.getParameter("token");
			//String payer = request.getParameter("PayerID");
			OrderDetails det = orders.findByPaymentId(token);
			if (det != null) {
				if (det.isPaid()) {
					boolean state = PaypalOrder.processOrder(token);
					model.addAttribute("state", state);
					orders.setPaid(det, true);
				} else {
					//TODO: Már ki van fizetve
					model.addAttribute("state", false);
				}
			} else {
				//TODO: A rendelés nem létezik!
				model.addAttribute("state", false);
			}
			
			return "checkoutreturn";
		}
		
		return "checkout";
	}
	
	@RequestMapping(value = "/order")
	public RedirectView orderCart(HttpServletRequest request, Model model) {
		OrderDetails details = new OrderDetails();
		
		List<CartItem> cart = Utils.getCartItems(request);
		int price = 0;
		for (CartItem c : cart)
			price += c.getCount() * c.getItem().getPrice();
		
		Order order = PaypalOrder.newOrder(price);
		
		details.setFirstName(request.getParameter("firstName"));
		details.setLastName(request.getParameter("lastName"));
		details.setAddress(request.getParameter("address"));
		
		details.setShip_firstName(request.getParameter("ship_firstName"));
		details.setShip_lastName(request.getParameter("ship_lastName"));
		details.setShip_address(request.getParameter("ship_address"));
		
		details.setPhone(request.getParameter("phone"));
		details.setEmail(request.getParameter("email"));
		
		details.setItems(cart);
		details.setPaymentID(order.id());
		
		orders.create(details);
		return new RedirectView(order.links().get(1).href());
	}
}
