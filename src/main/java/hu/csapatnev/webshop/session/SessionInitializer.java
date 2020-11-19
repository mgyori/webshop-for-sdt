package hu.csapatnev.webshop.session;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import hu.csapatnev.webshop.Config;

public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {
	public SessionInitializer() {
		super(Config.class); 
	}
}
