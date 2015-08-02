package coolraw.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import coolraw.annotation.Interceptor;
import coolraw.annotation.RequestMapping;
import coolraw.view.ModelAndView;



@Component
@Interceptor
@RequestMapping(name="/test")
public class TestInterceptor2 implements coolraw.interceptor.Interceptor{

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("我是拦截器1的pre");
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelAndview) {
		modelAndview.addObject("ss", "yea!!");
		System.out.println("我是拦截器1的post");
		
	}

	public void afterHandle(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("我是拦截器1的after");
		
	}


	
}
