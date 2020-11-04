package hu.csapatnev.webshop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

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
}
