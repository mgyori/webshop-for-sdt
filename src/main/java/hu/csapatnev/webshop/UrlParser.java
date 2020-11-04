package hu.csapatnev.webshop;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerMapping;

public class UrlParser {
	private HashMap<String, String> map;
	
	public UrlParser(HttpServletRequest request) {
		this.map = new HashMap<String, String>();
		String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] args = path.split("/");
		for (int i = 2; i < args.length - 1; i+=2)
			if (args.length > i + 1)
				this.map.put(args[i], args[i + 1]);
	}
	
	public int getParamInt(String key, int def) {
		int par;
		try {
			par = Integer.parseInt(this.map.get(key));
		} catch (Exception e) {
			par = def;
		}
		return par;
	}
	
	public int getParamInt(String key) {
		return getParamInt(key, 0);
	}
	
	public String getParamString(String key, String def) {
		return this.map.containsKey(key) ? this.map.get(key) : def;
	}
	
	public String getParamString(String key) {
		return getParamString(key, "");
	}
}
