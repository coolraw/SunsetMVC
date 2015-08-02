package coolraw.processor.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import collraw.processor.handler.PathHandler;



public interface PathAdapter {
	boolean isSupport(Object pathHandler);
	Object process(PathHandler pathHandler,HttpServletRequest request,HttpServletResponse response);
}
