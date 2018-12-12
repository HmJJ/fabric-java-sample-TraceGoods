package com.springboot.basic.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	@Getter @Setter private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		setContext(context);
	}
	
	public static Object getBean(String beanname) {
		return getContext().getBean(beanname);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return getContext().getBean(clazz);
	}
	
	public static <T> T getBean(String beanname, Class<T> clazz) {
		return getContext().getBean(beanname, clazz);
	}
}
