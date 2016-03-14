package it.paridelorenzo.ISSSR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.firebase.client.Firebase;

import grid.entities.Grid;
import grid.entities.Practitioner;
import grid.interfaces.services.GridService;
import grid.interfaces.services.PractitionerService;

@Controller
public class LoginController {

	private PractitionerService 	practitionerService;
	private GridService			gridService;
	
	@Autowired(required=true)
	@Qualifier(value="gridService")
	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	
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
		List<Grid> grids=this.gridService.listAllGrids();
		for(Grid current: grids){
			if(current.obtainGridState().equals(Grid.GridState.UPDATING)){
				return model;
			}
		}
		this.resetfirebaseVariables();
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
	
	public void resetfirebaseVariables(){
	    Firebase myFirebaseRef = new Firebase("https://fiery-torch-6050.firebaseio.com/");
	    myFirebaseRef.removeValue();
	}
	
}
