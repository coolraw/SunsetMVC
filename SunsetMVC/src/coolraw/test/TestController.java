package coolraw.test;

import org.springframework.stereotype.Controller;

import coolraw.annotation.RequestMapping;
import coolraw.view.ModelAndView;



@Controller
@RequestMapping(name="/test/tt/ee")
public class TestController {
	
	@RequestMapping(name="/String")
	public String test(String a){
		System.out.println(a+"成功！！！！");
		return "redirect:test";
	}
	
	@RequestMapping(name="/Float")
	public ModelAndView testst(Double a){
		System.out.println(a+"成功！！！！");
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("ss", "fdfd");
		modelAndView.setPath("test");
		
		return modelAndView;
		
	}
	
	
	@RequestMapping(name="/wei")
	public void testssss(Integer a){
		System.out.println(a+"成功！！！！真开心");
	}
}
