package hu.csapatnev.webshop.paypal;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PaypalClient {
	private PayPalEnvironment environment;
	private PayPalHttpClient client;
	
	private static PaypalClient instance;
	
	public PaypalClient(String id, String secret) {
		this.environment = new PayPalEnvironment.Sandbox(id, secret);
		this.client = new PayPalHttpClient(environment);
		
		instance = this;
	}
	
	public PayPalHttpClient client() {
		return this.client;
	}
	
	public static PaypalClient initInstance(String id, String secret) {
		if (instance == null)
			instance = new PaypalClient(id, secret);
		return instance;
	}
	
	public static PaypalClient getInstance() {
		return instance;
	}
}
