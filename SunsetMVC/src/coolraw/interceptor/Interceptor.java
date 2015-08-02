package coolraw.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.view.ModelAndView;


public interface Interceptor {
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response);
	public void postHandle(HttpServletRequest request,HttpServletResponse response,ModelAndView modelAndView);
	public void afterHandle(HttpServletRequest request,HttpServletResponse response);
}
