package collraw.processor.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.interceptor.Interceptor;




public class NotFondHandler implements PathHandler{

	public boolean isMatch(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object process(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setInterceptors(Interceptor[] interceptors) {
		// TODO Auto-generated method stub
		
	}

	public Interceptor[] getInterceptors() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
