package coolraw.front.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import collraw.processor.handler.NotFondHandler;
import collraw.processor.handler.PathHandler;

import coolraw.annotation.RequestMapping;
import coolraw.beanfactory.Ioc;
import coolraw.interceptor.Interceptor;
import coolraw.processor.adapter.PathAdapter;
import coolraw.processor.factory.DefaultPathFactory;
import coolraw.processor.factory.PathFactory;
import coolraw.view.ModelAndView;






public class DispatcherServlet extends HttpServlet {
	private Ioc ioc;
	private List<String> controllerUris=new ArrayList<String>();
	private List<PathHandler> pathHandlers=new ArrayList<PathHandler>();
	private List<PathAdapter> pathAdapters=new ArrayList<PathAdapter>();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accessUri=request.getRequestURI().substring(request.getContextPath().length());
		PathHandler pathHandler=getPathHandler(accessUri);
		Interceptor[] interceptors=pathHandler.getInterceptors();
		PathAdapter pathAdapter=getPathAdaper(pathHandler);
		
		
		boolean result=processInterceptorPre(interceptors,request,response);//Interceptor前置
		if(result==false){
			return;
		}
	
		Object returnData=pathAdapter.process(pathHandler, request,response);
		ModelAndView modelAndView=(ModelAndView) ioc.getBean("ViewResolver");
		modelAndView.parse(returnData);
		
		processInterceptorPost(interceptors, request, response, modelAndView);//Interceptor提交前
		
		modelAndView.render(request, response);
		modelAndView.dispatch(request, response);
		
		processInterceptorAfter(interceptors, request, response);//Interceptor后置
		
		
	}

	private void processInterceptorAfter(Interceptor[] interceptors,
			HttpServletRequest request, HttpServletResponse response) {
		for(int i=interceptors.length-1;i>=0;i--){
			interceptors[i].afterHandle(request, response);
		}
		
	}

	private PathAdapter getPathAdaper(PathHandler pathHandler) {
		for(PathAdapter pathAdapter:pathAdapters){
			if(pathAdapter.isSupport(pathHandler)){
				return pathAdapter;
			}
		}
		return null;
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() throws ServletException {
		String iocName=this.getInitParameter("Ioc");
			try {
				ioc=(Ioc) Class.forName(iocName).getConstructor(ServletContext.class).newInstance(this.getServletContext());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ioc.init();
		initPathHandlers();
		initPathAdapters();
		initInterceptor();
	
		
		
		
	}
	private void processInterceptorPost(Interceptor[] interceptors,HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelAndView) {
		for(int i=interceptors.length-1;i>=0;i--){
			interceptors[i].postHandle(request, response, modelAndView);
		}
		
	}
	private boolean processInterceptorPre(Interceptor[] interceptors,HttpServletRequest request,HttpServletResponse response) {
		for(int i=0;i<interceptors.length;i++){
			boolean result=interceptors[i].preHandle(request,response);
			if(result==false){
				return false;
			}
		}
		return true;
	}
	

	private void initInterceptor() {
		List<Interceptor> interceptors=ioc.getInterceptor();
		for(String controllerUri:controllerUris){
			String[] controllerUriPart=controllerUri.split("/");//controller地址
			List<Interceptor> controllerInterceptors=new ArrayList<Interceptor>();//存放controller的拦截器
			for(Interceptor interceptor:interceptors){
				String interceptorUri=interceptor.getClass().getAnnotation(RequestMapping.class).name();
				String[] interceptorUriPart=interceptorUri.split("/");//拦截器地址
				if(interceptorUriPart.length<=controllerUriPart.length){
					if(isFit(interceptorUriPart,controllerUriPart)){
						controllerInterceptors.add(interceptor);
					}
				}
			}
			PathHandler pathHandler=getPathHandlerByControllerUri(controllerUri);
			loadInterceptors(pathHandler,controllerInterceptors);
		}
		
	}
	
	

	private void loadInterceptors(PathHandler pathHandler,List<Interceptor> controllerInterceptors) {
		Interceptor[] interceptors=new Interceptor[controllerInterceptors.size()];
		for(int i=0;i<interceptors.length;i++){
			interceptors[i]=controllerInterceptors.get(i);

		}
		interceptors=sortInterceptors(interceptors);//将拦截器按照路径由长到短排序
		pathHandler.setInterceptors(interceptors);
		
		
	}

	private Interceptor[] sortInterceptors(Interceptor[] interceptors) {
		for(int i=0;i<interceptors.length-1;i++){
			for(int j=0;j<interceptors.length-i-1;j++){
				int length1=interceptors[j].getClass().getAnnotation(RequestMapping.class).name().split("/").length;
				int length2=interceptors[j+1].getClass().getAnnotation(RequestMapping.class).name().split("/").length;
				if(length1<length2){
					Interceptor team=interceptors[j];
					interceptors[j]=interceptors[j+1];
					interceptors[j+1]=team;
				}
			}
		}
		return interceptors;
		
	}

	private boolean isFit(String[] interceptorUriPart,String[] controllerUriPart) {
		for(int i=0;i<interceptorUriPart.length;i++){
			if(!interceptorUriPart[i].equals(controllerUriPart[i])){
				return false;
			}
		}
		return true;
	}

	private void initPathAdapters() {
		PathFactory pathFactory=new DefaultPathFactory();
		pathAdapters.add(pathFactory.createPathAdapter());
		
	}

	protected void initPathHandlers(){
		PathFactory pathFactory=new DefaultPathFactory(); 
		
		List<Object> controllers=ioc.getControllers();
		for(Object controller:controllers){
			String controllerUri=controller.getClass().getAnnotation(RequestMapping.class).name();//不用springioc的注解，因为若换为其它容器则无效，所以统一用自定义的注解。不要依赖容器
			PathHandler pathHandler=pathFactory.createPathHandler(controllerUri, controller);
			pathHandlers.add(pathHandler);
			controllerUris.add(controllerUri);
		}
	}
	
	protected PathHandler getPathHandler(String accessUri){
		String[] accessUriPart=accessUri.split("/");
		for(String controllerUri:controllerUris){
			String[] controllerUriPart=controllerUri.split("/");
			if(accessUriPart.length>controllerUriPart.length){
				boolean flag=true;
				for(int i=0;i<controllerUriPart.length;i++){
					if(!accessUriPart[i].equals(controllerUriPart[i])){
						flag=false;
					}
				}
				if(flag){
					return getPathHandlerByControllerUri(controllerUri);
				}
			}
		}
		return new NotFondHandler();//此处可扩展为404
	}
	
	protected PathHandler getPathHandlerByControllerUri(String controllerUri){
		for(PathHandler pathHandler:pathHandlers){
			if(pathHandler.isMatch(controllerUri)){
				return pathHandler;
			}
		}
		return null;
	}
	
}
