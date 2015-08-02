package coolraw.processor.factory;

import coolraw.processor.adapter.DefaultPathAdapter;
import coolraw.processor.adapter.PathAdapter;
import collraw.processor.handler.DefaultPathHandler;
import collraw.processor.handler.PathHandler;



public class DefaultPathFactory implements PathFactory {

	

	public PathHandler createPathHandler(String uri,Object controller) {
		DefaultPathHandler pathHandler=new DefaultPathHandler();
		pathHandler.getControllerUri_controller().put(uri,controller);
		pathHandler.setControllerUri(uri);
		return pathHandler;
	}

	public PathAdapter createPathAdapter() {
		return new DefaultPathAdapter();
	}



}
