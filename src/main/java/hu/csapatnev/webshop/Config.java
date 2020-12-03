package hu.csapatnev.webshop;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import hu.csapatnev.webshop.paypal.PaypalClient;

@EnableWebMvc
@EnableJdbcHttpSession
@Configuration
public class Config implements WebMvcConfigurer {
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
			"classpath:/resources/", "classpath:/static/", "classpath:/public/" };

	private Properties properties;
	private static Config config;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ApplicationContext appContext;

	public Config() {
		Properties prop = getProperties();
		PaypalClient.initInstance(prop.getProperty("paypal.id"), prop.getProperty("paypal.secret"));
		
		config = this;
	}
	
	@Bean
	public ITemplateResolver templateResolver() {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);
		return resolver;
	}

	@Bean
	public DataSource dataSource() {
		Properties prop = getProperties();
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUsername(prop.getProperty("db.user"));
		dataSource.setPassword(prop.getProperty("db.pass"));
		dataSource.setUrl("jdbc:mysql://" + prop.getProperty("db.host") + ":" + prop.getProperty("db.port") + "/"
				+ prop.getProperty("db.base")
				+ "?createDatabaseIfNotExist=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8_general_ci&characterSetResults=utf8");
		return dataSource;
	}
	
	@Bean
    public PlatformTransactionManager transactionManager () {
        EntityManagerFactory factory = (EntityManagerFactory) entityManagerFactory();
        return new JpaTransactionManager(factory);
    }

	@Bean
    public EntityManagerFactory entityManagerFactory () {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.TRUE);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("hu.csapatnev.webshop.jpa.model");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory.getObject();
    }
	
	public Properties getProperties() {
		if (this.properties == null) {
			this.properties = new Properties();
			InputStream inputStream = App.class.getClassLoader().getResourceAsStream("application.properties");
			try {
				this.properties.load(inputStream);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return this.properties;
	}

	final Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
		hibernateProperties.setProperty("hibernate.cache.use_query_cache", "false");
		hibernateProperties.setProperty("hibernate.show_sql", "false");

		return hibernateProperties;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}

	@Bean
	public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		Map<String, Object> map = new LinkedHashMap<>();
		ResourceHttpRequestHandler resourceHttpRequestHandler = new ResourceHttpRequestHandler();
		List<Resource> locations = new ArrayList<>();
		locations.add(new ServletContextResource(servletContext, "/"));
		for (String s : CLASSPATH_RESOURCE_LOCATIONS)
			locations.add(new ClassPathResource(s.substring("classpath:/".length())));
		resourceHttpRequestHandler.setLocations(locations);
		resourceHttpRequestHandler.setApplicationContext(appContext);

		List<ResourceResolver> resourceResolvers = new ArrayList<>();
		PathResourceResolver resourceResolver = new PathResourceResolver();
		resourceResolver.setAllowedLocations(locations.toArray(new Resource[locations.size()]));
		resourceResolvers.add(resourceResolver);

		resourceHttpRequestHandler.setResourceResolvers(resourceResolvers);
		map.put("/**", resourceHttpRequestHandler);
		simpleUrlHandlerMapping.setUrlMap(map);
		ResourceUrlProvider resourceUrlProvider = new ResourceUrlProvider();
		Map<String, ResourceHttpRequestHandler> handlerMap = new LinkedHashMap<>();
		handlerMap.put("/**", resourceHttpRequestHandler);
		resourceUrlProvider.setHandlerMap(handlerMap);
		ResourceUrlProviderExposingInterceptor interceptor = new ResourceUrlProviderExposingInterceptor(
				resourceUrlProvider);
		simpleUrlHandlerMapping.setInterceptors(new Object[] { interceptor });
		return simpleUrlHandlerMapping;
	}

	@Bean
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		r.setDefaultErrorView("error");
		r.setExceptionAttribute("ex");
		return r;
	}
	
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
	    return new StringHttpMessageConverter(Charset.forName("UTF-8"));
	}
	
	public static Config getInstance() {
		if (config == null)
			config = new Config();
		return config;
	}
}