package coolraw.beanfactory;

import java.util.List;

import javax.servlet.ServletContext;

import coolraw.interceptor.Interceptor;






public interface Ioc {
	void init();
	Object getBean(String name);
	List<Object> getControllers();
	List<Interceptor> getInterceptor();
}
