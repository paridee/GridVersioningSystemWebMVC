package it.paridelorenzo.ISSSR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import grid.entities.Practitioner;
import grid.interfaces.services.PractitionerService;

@Controller
public class LoginController {

	private PractitionerService 	practitionerService;
	
	@Autowired(required=true)
	@Qualifier(value="practitionerService")
	public void setPractitionerService(PractitionerService practitionerService) {
		this.practitionerService = practitionerService;
	}

	//Spring Security see this :
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout) {
		System.out.println("provvedo a login");
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}
	
	@RequestMapping(value = "/logout**", method = RequestMethod.GET)
	public ModelAndView logoutPage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName(); //get logged in username
	    Practitioner p	=	this.practitionerService.getPractitionerByEmail(email);
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Grid Versioning System - logout");
		model.addObject("message", "Dear "+p.getName()+" ("+p.getEmail()+")");
		model.setViewName("logout");

		return model;
	}
	
}
