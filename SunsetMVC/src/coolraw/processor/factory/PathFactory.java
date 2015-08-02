package coolraw.processor.factory;

import coolraw.processor.adapter.PathAdapter;
import collraw.processor.handler.PathHandler;



public interface PathFactory {
	PathHandler createPathHandler(String uri,Object controller);
	PathAdapter createPathAdapter();
}
