package collraw.processor.handler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coolraw.annotation.RequestMapping;
import coolraw.interceptor.Interceptor;
import coolraw.processor.switcher.Adapter;
import coolraw.processor.switcher.DoubleAdapter;
import coolraw.processor.switcher.FloatAdapter;
import coolraw.processor.switcher.IntegerAdapter;
import coolraw.view.ModelAndView;







public class DefaultPathHandler implements PathHandler {
	protected String controllerUri;
	protected HashMap<String,Object> controllerUri_controller=new HashMap<String,Object>();
	protected Interceptor[] interceptors;
	
	
	
	public boolean isMatch(String uri) {
		if(controllerUri_controller.get(uri)!=null){
			return true;
		}
		return false;
	}
	public Object process(HttpServletRequest request,HttpServletResponse response) {
		String uri=request.getRequestURI();
		String methodUri=uri.substring(request.getContextPath().length()+this.getControllerUri().length());
		Object controller=this.getControllerUri_controller().get(this.getControllerUri());
		
		Method method=getMethod(controller,methodUri);
		Object returnData=processMethod(method,request);//执行请求处理方法
		
		return returnData;
	}
	
	
	
	private Method getMethod(Object controller,String methodUri) {
		Method[] methods=controller.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			if(methods[i].getAnnotation(RequestMapping.class).name().equals(methodUri)){
				return methods[i];
			}
		}
		
		return null;
	}
	private Object processMethod(Method method,HttpServletRequest request) {
		Object returnData=null;
		
		Map parameterMap=request.getParameterMap();
		Set parameterKey=parameterMap.keySet();//传入的参数名
		String[] methodParameterNames=getMethodParameterNames(method);//方法参数名
		Class[] methodParameterTypes=method.getParameterTypes();
		if(isLegal(parameterKey,methodParameterNames)){
			Object[] parameters=getParameters(parameterMap,methodParameterNames,methodParameterTypes);//将传入的参数进行处理，得到方法可用参数
			try {
				Object controller=this.getControllerUri_controller().get(this.getControllerUri());
				returnData=method.invoke(controller,parameters);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnData;
	}
	
	private Object[] getParameters(Map parameterMap, String[] methodParameterNames,Class[] methodParameterTypes) {
		Object[] parameters=new Object[methodParameterNames.length];
		for(int i=0;i<methodParameterNames.length;i++){
			String[] parameterArray=(String[]) parameterMap.get(methodParameterNames[i]);
			String parameter=parameterArray[0];//得到传入参数
			Class parameterType=methodParameterTypes[i];//得到传入参数的类型
			if(parameterType==String.class){
				parameters[i]=parameter;
			}else{
				Adapter adapter=getAdapter(parameterType);
				parameters[i]=adapter.Switch(parameter);					
			}
		}
		return parameters;
		
	}
	private Adapter getAdapter(Class parameterType) {
		List<Adapter> adapters=new ArrayList<Adapter>();
		adapters.add(new IntegerAdapter());
		adapters.add(new DoubleAdapter());
		adapters.add(new FloatAdapter());
		
		for(Adapter adapter:adapters){
			if(adapter.support(parameterType)){
				return adapter;
			}
		}
		return null;
	}
	private boolean isLegal(Set parameterKey, String[] methodParameterNames) {
		
		//parameterKey.remove(parameterName)会导致主程序中的parameterMap中的键值对失效。
//		for(int i=0;i<methodParameterNames.length;i++){
//			String paramterName=methodParameterNames[i];
//			parameterKey.remove(paramterName);
//		}
//		if(parameterKey.size()==0){
//			return true;
//		}
//		return false;
		if(parameterKey.size()==methodParameterNames.length){
			for(int i=0;i<methodParameterNames.length;i++){
				String parameterName=methodParameterNames[i];
				if(!parameterKey.contains(parameterName)){
					return false; 
				}
				
			}			
		}
		return true;
	}
	
	private String[] getMethodParameterNames(Method method) {
		String[] paramNames=null;
		ClassPool pool=ClassPool.getDefault();
		 
		  
		try {
			Class clazz=method.getDeclaringClass();
			pool.insertClassPath(new ClassClassPath(clazz)); 
			CtClass cc=pool.get(clazz.getName());
			CtMethod cm=cc.getDeclaredMethod(method.getName());
			
			MethodInfo methodInfo=cm.getMethodInfo();
			CodeAttribute codeAttribute=methodInfo.getCodeAttribute();
			LocalVariableAttribute attr=(LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if(attr!=null){
				paramNames=new String[cm.getParameterTypes().length];
				int pos=Modifier.isStatic(cm.getModifiers())?0:1;
				for(int i=0;i<paramNames.length;i++){
					paramNames[i]=attr.variableName(i+pos);
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramNames;
		
	}
	public HashMap<String, Object> getControllerUri_controller() {
		return controllerUri_controller;
	}
	public void setControllerUri_controller(
			HashMap<String, Object> controllerUri_controller) {
		this.controllerUri_controller = controllerUri_controller;
	}
	public Interceptor[] getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(Interceptor[] interceptors) {
		this.interceptors = interceptors;
	}
	public String getControllerUri() {
		return controllerUri;
	}
	public void setControllerUri(String controllerUri) {
		this.controllerUri = controllerUri;
	}
	
	

	
	

	

}
