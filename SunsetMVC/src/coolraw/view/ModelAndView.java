package coolraw.view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public  class ModelAndView {
	protected String path;
	protected Map<String,Object> requestModel=new HashMap<String,Object>();
	protected Map<String,Object> sessionModel=new HashMap<String,Object>();
	protected Map<String,Object> applicationModel=new HashMap<String,Object>();
	protected String mode;
	
	
	public void addObject(String key,Object value){
		this.requestModel.put(key, value);
	}
	public void addObject(String key,Object value,String scope){
		if(scope.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope.equals("application")){
			this.applicationModel.put(key, value);
		}
	}
	public void addObject(String key,Object value,String scope1,String scope2){
		if(scope1.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope1.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope1.equals("application")){
			this.applicationModel.put(key, value);
		}
		
		if(scope2.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope2.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope2.equals("application")){
			this.applicationModel.put(key, value);
		}
	}
	
	public void addObject(String key,Object value,String scope1,String scope2,String scope3){
		if(scope1.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope1.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope1.equals("application")){
			this.applicationModel.put(key, value);
		}
		
		if(scope2.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope2.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope2.equals("application")){
			this.applicationModel.put(key, value);
		}
		
		if(scope3.equals("request")){
			this.requestModel.put(key, value);
		}else if(scope3.equals("session")){
			this.sessionModel.put(key, value);
		}else if(scope3.equals("application")){
			this.applicationModel.put(key, value);
		}
		
	}
	public String getPath(){
		return this.path;
	}
	public void setPath(String path){
		this.path=path;
	}
	
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	
	public Map<String, Object> getRequestModel() {
		return requestModel;
	}
	public void setRequestModel(Map<String, Object> requestModel) {
		this.requestModel = requestModel;
	}
	public Map<String, Object> getSessionModel() {
		return sessionModel;
	}
	public void setSessionModel(Map<String, Object> sessionModel) {
		this.sessionModel = sessionModel;
	}
	public Map<String, Object> getApplicationModel() {
		return applicationModel;
	}
	public void setApplicationModel(Map<String, Object> applicatinoModel) {
		this.applicationModel = applicatinoModel;
	}
	
	
	public  void parse(Object object){};
	public  void render(HttpServletRequest request,HttpServletResponse response){};
	public  void dispatch(HttpServletRequest request,HttpServletResponse response){};
}
