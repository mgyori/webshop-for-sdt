package hu.csapatnev.webshop;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerMapping;

public class Utils {
	public static HashMap<String, String> parseUrlFromRequest(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] args = path.split("/");
		for (int i = 2; i < args.length - 1; i+=2)
			if (args.length > i + 1)
				map.put(args[i], args[i + 1]);
		return map;
	}
}
