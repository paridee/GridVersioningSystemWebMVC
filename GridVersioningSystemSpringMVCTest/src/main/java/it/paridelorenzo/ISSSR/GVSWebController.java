package it.paridelorenzo.ISSSR;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

	private static final Logger logger = LoggerFactory.getLogger(GVSWebController.class);
     
	@RequestMapping(value = "/grids", method = RequestMethod.GET)
    public String listAllGrids(Model model) {
		List<Grid> temp= this.gridService.listAllGrids();
		model.addAttribute("nGrids", temp.size());
        model.addAttribute("listGrids", temp);
        return "grids";
    }
	
	@RequestMapping(value = "/grids/{id}")
    public String getGrid(@PathVariable("id") int id, Model model) {
		Grid tempGrid= this.gridService.getGridById(id);
		model.addAttribute("grid", tempGrid);
		String chart=createChart(tempGrid);
		System.out.println(chart);
		model.addAttribute("gridTreeString",chart);
        return "grids";
    }
	
	private String createChart(Grid g){
		if(g.getMainGoals().size()!=0){
			List<Object> stack = new ArrayList<Object>();
			stack.addAll(g.getMainGoals());
			String chart="chart_config = {chart: { container: \"#gridChart\", animateOnInit: true,node: {collapsable: true},animation: {nodeAnimation: \"easeOutBounce\",nodeSpeed: 700,connectorsAnimation: \"bounce\",connectorsSpeed: 700}},";		
			chart=chart+"nodeStructure: {image:\"/ISSSR/resources/Treant/cheryl.png\", text: { name: \""+g.getProject().getProjectId()+"\",desc: \""+g.getProject().getDescription()+"\" },children: [";
			chart=chart+updateChart(stack)+"]}};";
			return chart;
		}
		else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", "error");
			jsonObject.put("resp", "grid main goal error");
			return jsonObject.toString();
		}
		
	}
	
	private String updateChart(List<Object> stack){
		String chart="";
		for(int i=0; i<stack.size(); i++){
			String image="";
			String name="";
			String desc="";
			//
			List<Object> newStack=new ArrayList<Object>();
			/*if(stack.get(i).getClass().getSimpleName().equals("Goal")){
				Goal tempGoal=(Goal)stack.get(i);
				name=tempGoal.getLabel();
				newStack.addAll(tempGoal.getStrategyList());
			}
			else if(stack.get(i).getClass().getSimpleName().equals("Strategy")){
				Strategy tempStrategy=(Strategy)stack.get(i);
				name=tempStrategy.getLabel();
				newStack.addAll(tempStrategy.getGoalList());
			}
			else {
				name=stack.get(i).getClass().getSimpleName();
			}*/
			GridElement ge=(GridElement)stack.get(i);
			name=ge.getLabel()+"-v"+ge.getVersion();
			desc="";
			Field[] fields=ge.getClass().getDeclaredFields();
			for(int j=0; j<fields.length;j++){
				Field tempField=fields[j];
				tempField.setAccessible(true);
				try {
					Object fieldValue=tempField.get(ge);
					if(fieldValue instanceof GridElement){
						newStack.add(fieldValue);
					}
					else if(fieldValue instanceof List){
						List myList 	=	(List)fieldValue;
						if(myList.size()>0){
							Object	first	=	 myList.get(0);
							if(first instanceof GridElement){
								newStack.addAll(myList);
							}
							//TODO gestire array stringhe
						}
					}
					else{
						//desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+": "+fieldValueStr+"</div>";
						if(fieldValue!=null){
							String fieldValueStr	=	(String)fieldValue.toString();
							desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+":"+fieldValueStr+"</div>";
						}
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			chart=chart+"{innerHTML:\"<div class=\'nodeTxt\'>"+name+"<br>"+desc+"</div>\", collapsed: true";
			//chart=chart+"{text: { name: \""+name+"\", desc: \""+desc+"\" },innerHTML:\"<div><h1>test</h1></div>\", collapsed: true";
			if(newStack.size()>0){
				chart=chart+",children: [";
				chart=chart+updateChart(newStack);
				chart=chart+"]";
			}
			chart=chart+"}";
			if (i<stack.size()-1) chart=chart+",";
		}
		return chart;
		
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
					HashMap<String,GridElement> elements	=	temp.obtainAllEmbeddedElements();
					Grid intermediate	=	null;
					for(int i=0;i<mods.size();i++){
						Modification 	aMod	=	mods.get(i);
						if(aMod instanceof ObjectFieldModification){
							GridElement 	subj;
							if(elements.containsKey(((ObjectFieldModification) aMod).getSubjectLabel())){
								subj	=	elements.get(((ObjectFieldModification) aMod).getSubjectLabel());
								((ObjectFieldModification) aMod).apply(subj, newVersion);
								newVersion	=	this.gridService.updateGridElement(newVersion, subj,true);
								this.gridService.updateGrid(newVersion);
							}
							else return "error";
						}
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
