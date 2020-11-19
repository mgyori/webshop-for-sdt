package hu.csapatnev.webshop.paypal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;

import hu.csapatnev.webshop.Config;

public class PaypalOrder {
	public static Order newOrder(int cost) {

		Order order = null;
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.checkoutPaymentIntent("CAPTURE");

		ApplicationContext applicationContext = new ApplicationContext()
				.brandName(Config.getInstance().getProperties().getProperty("site.name")).landingPage("BILLING")
				.cancelUrl(Config.getInstance().getProperties().getProperty("site.url") + "checkout/cancel")
				.returnUrl(Config.getInstance().getProperties().getProperty("site.url") + "checkout/return")
				.userAction("CONTINUE");
		orderRequest.applicationContext(applicationContext);

		List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
		purchaseUnits.add(new PurchaseUnitRequest()
				.amountWithBreakdown(new AmountWithBreakdown().currencyCode("HUF").value(String.valueOf(cost))));
		orderRequest.purchaseUnits(purchaseUnits);
		OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

		try {
			HttpResponse<Order> response = PaypalClient.getInstance().client().execute(request);
			order = response.result();
		} catch (IOException ioe) {
			if (ioe instanceof HttpException) {
				HttpException he = (HttpException) ioe;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			} else {

			}
		}

		return order;
	}

	public static boolean processOrder(String id) {
		Order order = null;
		try {
			OrdersCaptureRequest request = new OrdersCaptureRequest(id);

			try {
				HttpResponse<Order> response = PaypalClient.getInstance().client().execute(request);
				order = response.result();
				System.out.println(order.status());
				return order.status().equals("COMPLETED");
			} catch (IOException ioe) {
				if (ioe instanceof HttpException) {
					HttpException he = (HttpException) ioe;
					System.out.println(he.getMessage());
					he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
				} else {

				}
			}
		} catch (Exception e) {

		}
		return false;
	}

}
