package coolraw.view;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspResolver extends ModelAndView {
	private String prefix;
	private String suffix;
	
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response) {
		if(!requestModel.isEmpty()){
			
			for(Entry<String, Object> entry:this.requestModel.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				request.setAttribute(key, value);
			}
		}
		
		if(!sessionModel.isEmpty()){
			
			for(Entry<String, Object> entry:this.sessionModel.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				request.getSession().setAttribute(key, value);
			}
		}
			
		if(applicationModel.isEmpty()){
			for(Entry<String, Object> entry:this.applicationModel.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				request.getSession().getServletContext().setAttribute(key, value);
			}
		}
	
			
		
	}

	@Override
	public void parse(Object object) {
		if(object instanceof String){
			if(((String) object).contains(":")){
				this.mode=((String) object).substring(0,((String) object).indexOf(":"));
				this.path=this.prefix+((String) object).substring(((String) object).indexOf(":")+1)+this.suffix;
			}else{
				this.mode="forward";
				this.path=this.prefix+object+this.suffix;
			}
		}else if(object instanceof ModelAndView){
			this.path=this.prefix+((ModelAndView) object).path+this.suffix;
			if(((ModelAndView) object).mode==null){
				this.mode="forward";
			}else{
				this.mode=((ModelAndView) object).mode;				
			}
			this.requestModel=((ModelAndView) object).requestModel;
			this.sessionModel=((ModelAndView) object).sessionModel;
			this.applicationModel=((ModelAndView) object).applicationModel;
		}
		
	}

	@Override
	public void dispatch(HttpServletRequest request,
			HttpServletResponse response) {
		if(mode.equals("forward")){
			try {
				request.getRequestDispatcher(path).forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(mode.equals("redirect")){
			try {
				response.sendRedirect(request.getContextPath()+path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	


	

}
