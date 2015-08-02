package coolraw.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import coolraw.annotation.Interceptor;
import coolraw.annotation.RequestMapping;
import coolraw.view.ModelAndView;

@Component
@Interceptor
@RequestMapping(name="/test/tt")
public class TestInterceptor implements coolraw.interceptor.Interceptor{

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("我是拦截器2的pre");
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelAndview) {
		System.out.println("我是拦截器2的post");
		
	}

	public void afterHandle(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("我是拦截器2的after");
		
	}


	
}
