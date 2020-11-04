package hu.csapatnev.webshop;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static UrlParser parseUrlFromRequest(HttpServletRequest request) {
		return new UrlParser(request);
	}
}
