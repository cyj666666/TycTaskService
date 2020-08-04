package amarsoft.com.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationListener<ContextRefreshedEvent> {
	
	private static volatile ApplicationContext ctx;
	
	private static volatile boolean isInit = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (isInit) {
			return;
		}
		
		ctx = event.getApplicationContext();
	}
	
	public static String getProperty(String key) {
		return ctx.getEnvironment().getProperty(key);
	}
	
	public static String getProperty(String key,String defaultValue) {
		String value = getProperty(key);
		
		return StringUtils.isBlank(value) ? defaultValue : value;
	}
	
	public static Integer getIntProperty(String key) {
		String value = getProperty(key);
		if (value == null) {
			return null;
		}
		
		return Integer.parseInt(value);
	}
	
	public static Integer getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			return defaultValue;
		}
		
		return Integer.parseInt(value);
	}
	
	public static Object getBean(String name) {
		return ctx.getBean(name);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return ctx.getBean(requiredType);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType) {
		return ctx.getBean(name, requiredType);
	}

}
