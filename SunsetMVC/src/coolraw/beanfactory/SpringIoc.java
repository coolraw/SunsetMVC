package coolraw.beanfactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import coolraw.interceptor.Interceptor;







public class SpringIoc implements Ioc {
	private ApplicationContext applicationContext;
	private ServletContext servletContext;
	public void init() {
		applicationContext=WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	public Object getBean(String name) {
		Object bean=applicationContext.getBean(name);
		return bean;
	}

	public SpringIoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SpringIoc(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}
	
	public List<Object> getControllers(){
		List<Object> controllers = new ArrayList<Object>();
		String[] beansName=applicationContext.getBeanDefinitionNames();
		for(String name:beansName){
			Object bean=getBean(name);
			if(bean.getClass().isAnnotationPresent(Controller.class)){
				controllers.add(bean);
			}
		}
		return controllers;
	}

	public List<Interceptor> getInterceptor() {
		List<Interceptor> interceptors=new ArrayList<Interceptor>();
		String[] beansName=applicationContext.getBeanDefinitionNames();
		for(String name:beansName){
			Object bean=getBean(name);
			if(bean.getClass().isAnnotationPresent(coolraw.annotation.Interceptor.class)){
				interceptors.add((Interceptor) bean);
			}
		}
		return interceptors;
	}
	

}
