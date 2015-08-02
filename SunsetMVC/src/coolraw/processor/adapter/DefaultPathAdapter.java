package coolraw.processor.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import collraw.processor.handler.DefaultPathHandler;
import collraw.processor.handler.PathHandler;



public class DefaultPathAdapter implements PathAdapter{
	public boolean isSupport(Object pathHandler) {
		return (pathHandler instanceof DefaultPathHandler);
	}

	public Object process(PathHandler pathHandler,HttpServletRequest request,HttpServletResponse response) {
		Object returnData=pathHandler.process(request,response);
		return returnData;
	}

	

	
	
}
