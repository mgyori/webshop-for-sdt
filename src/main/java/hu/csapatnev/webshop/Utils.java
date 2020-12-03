package hu.csapatnev.webshop;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import hu.csapatnev.webshop.session.CartItem;

public class Utils {
	public static UrlParser parseUrlFromRequest(HttpServletRequest request) {
		return new UrlParser(request);
	}
	
	public static List<Integer> buildPagination(Page<?> items, int maxPagi){
		ArrayList<Integer> pagination = new ArrayList<Integer>();
		
		int total = items.getTotalPages() - 1;
		if (maxPagi > total)
			maxPagi = total;
		
		int page = items.getNumber();
		int center = (int)Math.ceil(maxPagi / 2);
		int start = page;
		if (page > center)
			start = page - center;
		
		if (start + maxPagi > total)
			start = total - maxPagi;
		
		if (start < 0) {
			maxPagi = maxPagi + start;
			start = 0;
		}
		
		for (int i = 0; i < maxPagi; i++)
			pagination.add(i + start + 1);
	
		return pagination;
	}
	
	public static List<CartItem> getCartItems(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		List<CartItem> items = (List<CartItem>) request.getSession().getAttribute("cart");
		if (items == null)
			items = new ArrayList<CartItem>();
		return items;
	}
	
	public static String toUTF8(String in) {
		return new String(in.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
	}
}
