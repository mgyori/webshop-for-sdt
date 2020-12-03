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
import hu.csapatnev.webshop.mail.SendMail;
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
				if (!det.isPaid()) {
					StringBuilder itemSb = new StringBuilder();
					itemSb.append("<table>");
					itemSb.append("<thead>");
					itemSb.append("<tr>");
					itemSb.append("<td>Tárgy neve</td>");
					itemSb.append("<td>Darab</td>");
					itemSb.append("<td>Ár</td>");
					itemSb.append("</tr>");
					itemSb.append("</thead>");
					itemSb.append("<tbody>");
					for (CartItem item : det.getItems()) {
						itemSb.append("<tr>");
						itemSb.append("<td>" + item.getItem().getName() + "</td>");
						itemSb.append("<td>" + item.getCount() + "</td>");
						itemSb.append("<td>" + (item.getCount() * item.getItem().getPrice()) + " Ft</td>");
						itemSb.append("</tr>");
					}
					itemSb.append("</tbody>");
					itemSb.append("</table>");
					
					StringBuilder sb = new StringBuilder();
					sb.append("<b>Rendelö adatai:</b><br>");
					sb.append("Rendelö email címe: " + det.getEmail() + "<br>");
					sb.append("Rendelö telefon száma: " + det.getPhone() + "<br>");
					sb.append("<br>");
					sb.append("<b>Szállítási adatok:</b><br>");
					sb.append(det.getShip_firstName() + " " + det.getShip_lastName() + "<br>");
					sb.append(det.getShip_address() + "<br>");
					sb.append("<br>");
					sb.append("<b>Számlázási adatok:</b><br>");
					sb.append(det.getFirstName() + " " + det.getLastName() + "<br>");
					sb.append(det.getAddress() + "<br>");
					sb.append("<br>");
					sb.append("<b>Vásárolt tárgyak:</b><br>");
					sb.append(itemSb);
					
					SendMail.sendTemplate("018Mark018@gmail.com", "Új rendelés " + det.getId(), "order", sb.toString(), "Új rendelés érkezett", "Ne válaszolj, köszi!", "");
					SendMail.sendTemplate(det.getEmail(), "Új rendelés " + det.getId(), "order", sb.toString(), "Új rendelés érkezett", "Ne válaszolj, köszi!", "");
					
					request.getSession().setAttribute("cart", null);
					
					try {
						Order ord = PaypalOrder.processOrder(token);
						if (ord != null) {
							if (ord.status().equalsIgnoreCase("completed")) {
								model.addAttribute("state", true);
								orders.setPaid(det, true);
							} else {
								model.addAttribute("state", false);
								System.out.println("Nem sikerult a sizetes");
							}
						} else {
							model.addAttribute("state", false);
							System.out.println("Nincs order");
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					//TODO: Már ki van fizetve
					model.addAttribute("state", false);
					System.out.println("Fizetett");
				}
			} else {
				//TODO: A rendelés nem létezik!
				model.addAttribute("state", false);
				System.out.println("Nem letezik");
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
		
		Order order = PaypalOrder.newOrder(price + 890);
		
		details.setFirstName(Utils.urlDecode(request.getParameter("firstName")));
		details.setLastName(Utils.urlDecode(request.getParameter("lastName")));
		details.setAddress(Utils.urlDecode(request.getParameter("address")));
		
		details.setShip_firstName(Utils.urlDecode(request.getParameter("ship_firstName")));
		details.setShip_lastName(Utils.urlDecode(request.getParameter("ship_lastName")));
		details.setShip_address(Utils.urlDecode(request.getParameter("ship_address")));
		
		details.setPhone(Utils.urlDecode(request.getParameter("phone")));
		details.setEmail(Utils.urlDecode(request.getParameter("email")));
		
		details.setItems(cart);
		details.setPaymentID(order.id());
		
		details.setPaymentMethod(Integer.parseInt(request.getParameter("paymentMethod")));
		
		orders.create(details);
		return new RedirectView(order.links().get(1).href());
	}
}
