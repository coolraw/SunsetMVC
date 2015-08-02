package collraw.processor.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.interceptor.Interceptor;


public interface PathHandler {

	boolean isMatch(String uri);
	
	public Object process(HttpServletRequest request,HttpServletResponse response);
	
	public void setInterceptors(Interceptor[] interceptors);
	
	public Interceptor[] getInterceptors();
}
