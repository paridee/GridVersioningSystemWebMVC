package it.paridelorenzo.ISSSR;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import grid.JSONFactory;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Project;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectFieldModification;
import grid.modification.grid.GridModificationService;
 
 
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
	
	@RequestMapping(value = "/grids/add", method=RequestMethod.POST)
    public @ResponseBody String addGrid(@RequestBody String jsonData) {
		//Grid temp=JSONFactory.loadFromJson(jsonData, this.projectService);
		System.out.println(jsonData.toString());
		//this.gridService.addGrid(temp);
		Grid temp;
		try {
			temp = JSONFactory.loadFromJson(jsonData, this.projectService);
			Grid latest	=	this.gridService.getLatestGrid(temp.getProject().getId());
			if(latest	==	null){
				this.gridService.addGrid(temp);
			}
			else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("msg", "error");
				jsonObject.put("resp", "grid esistente");
				return jsonObject.toString();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", "error");
			jsonObject.put("resp", "eccezione generica");
			return jsonObject.toString();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "result");
		jsonObject.put("resp", "ok");
		return jsonObject.toString();
    }

	
	
	
	@RequestMapping(value = "/addgrid", method = RequestMethod.GET)
    public String addGridPage(Model model) {
		return "addgrid";
    }
	
	@RequestMapping(value = "/grids/update", method=RequestMethod.POST)
    public @ResponseBody String updateGrid(@RequestBody String jsonData) {
		//Grid temp=JSONFactory.loadFromJson(jsonData, this.projectService);
		System.out.println(jsonData.toString());
		//this.gridService.addGrid(temp);
		JSONObject response	=	new JSONObject();
		Grid temp;
		try {
			temp = JSONFactory.loadFromJson(jsonData, this.projectService);
			Grid latest	=	this.gridService.getLatestGrid(temp.getProject().getId());
			System.out.println("###~~~~VERSIONE GRID CARICATA"+latest.getVersion());
			if(latest	==	null){
				return "non esiste grid per questo progetto";
			}
			else{
				//TODO se e' update nel "nostro formato" vai a prendere la grid di riferimento e fai il check per i conflitti
				List<Modification>	mods		=	GridModificationService.getModification(latest, temp);
				Grid				toBeRemoved	=	null;	//temporary basic grid
				for(int i=0;i<mods.size();i++){
					System.out.println("found modification "+mods.get(i).toString());
				}
				if(mods.size()==0){
					return "non ci sono modifiche";
				}
				else{
					Grid 						newVersion	=	this.gridService.upgradeGrid(latest);
					newVersion.setVersion(newVersion.getVersion()-1);
					toBeRemoved								=	newVersion;		//i will delete this temporary version, further change will require an upgrade
					HashMap<String,GridElement> elements	=	temp.obtainAllEmbeddedElements();
					for(int i=0;i<mods.size();i++){
						Modification 	aMod	=	mods.get(i);
						if(aMod instanceof ObjectFieldModification){
							GridElement 	subj;
							if(elements.containsKey(((ObjectFieldModification) aMod).getSubjectLabel())){
								subj	=	elements.get(((ObjectFieldModification) aMod).getSubjectLabel());
								subj	=	this.gridElementService.upgradeGridElement(subj);
								((ObjectFieldModification) aMod).apply(subj, newVersion);
								newVersion	=	this.gridService.updateGridElement(newVersion, subj);
								this.gridService.updateGrid(newVersion);
							}
							else return "error";
						}
					}
					if(toBeRemoved!=null){
						System.out.println("Removing Grid with ID "+toBeRemoved.getId());
						this.gridService.removeGrid(toBeRemoved.getId());
					}
					return "modifiche";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error: generic exception";
		}
		
    }

	
	
	
	@RequestMapping(value = "/updategrid", method = RequestMethod.GET)
    public String updateGridPage(Model model) {
		return "updategrid";
    }
     
}
