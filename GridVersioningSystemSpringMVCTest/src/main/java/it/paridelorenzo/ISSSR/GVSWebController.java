package it.paridelorenzo.ISSSR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import grid.JSONFactory;
import grid.entities.Grid;
import grid.entities.Project;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
 
 
@Controller
public class GVSWebController {
     
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ProjectService		projectService;
	private JSONFactory 		jFact;
	
	@Autowired(required=true)
	@Qualifier(value="gridElementService")
	public void setGridElementService(GridElementService gridElementService) {
		this.gridElementService = gridElementService;
	}

	@Autowired(required=true)
	@Qualifier(value="gridService")
	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	
	@Autowired(required=true)
	@Qualifier(value="projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
     
	@RequestMapping(value = "/grids", method = RequestMethod.GET)
    public String listAllGrids(Model model) {
		List<Grid> temp= this.gridService.listAllGrids();
		model.addAttribute("nGrids", temp.size());
        model.addAttribute("listGrids", temp);
        return "grids";
    }
	
	@RequestMapping(value = "/grids/{id}")
    public String getGrid(@PathVariable("id") int id, Model model) {
		Grid temp= this.gridService.getGridById(id);
		model.addAttribute("grid", temp);
        return "grids";
    }
	
	
	
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String listAllProjects(Model model) {
		List<Project> temp = this.projectService.listProjects();
			model.addAttribute("nProjects", temp.size());
			model.addAttribute("listProjects", temp);
			return "projects";
		
		
    }
	@RequestMapping(value = "/projects/{id}")
    public String getProject(@PathVariable("id") int id, Model model) {
		Project temp= this.projectService.getProjectById(id);
		model.addAttribute("reqproject", temp);
		List<Grid> templist= this.gridService.getGridLog(id);
		model.addAttribute("nProjectGrids", templist.size());
        model.addAttribute("listProjectGrids", templist);
        return "projects";
    }
	
/*
	@RequestMapping(value = "/grids/add")
    public String addGrid(@RequestParam("jsonData") String jsonData, Model model) {
		model.addAttribute("addedGrid", "true");
		System.out.println(jsonData);
		return "addgrid";
    }
	*/
	
	@RequestMapping(value = "/grids/add")
    public @ResponseBody String addGrid(@ModelAttribute(value="jsonData") String jsonData, BindingResult result) {
		Grid temp;
		try {
			temp = JSONFactory.loadFromJson(jsonData, this.projectService);
			System.out.println(temp.toString());
			this.gridService.addGrid(temp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "ok";
    }
	
	
	
	@RequestMapping(value = "/addgrid", method = RequestMethod.GET)
    public String addGridPage(Model model) {
		return "addgrid";
    }
     
}
