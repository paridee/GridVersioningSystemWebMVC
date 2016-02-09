package it.paridelorenzo.ISSSR;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import grid.entities.GridElement.State;
import grid.entities.Project;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
 
 
@Controller
public class GVSWebController {
     
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ProjectService		projectService;
	private JSONFactory 		jFact;
	private ConflictService		conflictService;
	private int nMenuButtons=3;
	
	@Autowired(required=true)
	@Qualifier(value="conflictService")
	public void setConflictService(ConflictService	conflictService) {
		this.conflictService = conflictService;
	}
	
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
     
	@RequestMapping(value = "/GVShome", method = RequestMethod.GET)
    public String GVShome(Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		return "GVShome";
    }
	@RequestMapping(value = "/resolutionDashBoard/{state}", method = RequestMethod.GET)   //stati WORKING, MAJOR_UPDATING,MAJOR_CONFLICTING, MINOR_CONFLICTING, FINAL_KO
    public String majorpendingView(@PathVariable("state") int state,Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		//get all projects
		List<Project> projList=this.projectService.listProjects();
		List<Project> projPending=new ArrayList<Project>();
		Map <String, List<Grid>> projectGrids=new HashMap<>();
		Map <String, List<GridElement>> projectGridsMajorPendingElements=new HashMap<>();
		//for all projects get pending grid elements
		if (projList.size()>0){
			for (int i=0; i<projList.size(); i++){
				List<Grid> projGrids=this.gridService.getGridLog(projList.get(i).getId());
				List<Grid> gridPending=new ArrayList<Grid>();
				boolean addedGrid=false;
				for(Grid g: projGrids){
					if(g.obtainGridState()==Grid.GridState.UPDATING){
						//ottieni elementi in stato major pending
						HashMap<String, GridElement> elements=g.obtainAllEmbeddedElements();
						Set<String>	keySet		=	elements.keySet();
						Iterator<String> anIterator	=	keySet.iterator();
						boolean addedElement=false;
						List<GridElement> pendingElements=new ArrayList<GridElement>();
						while(anIterator.hasNext()){
							String key		=	anIterator.next();
							State aState	=	elements.get(key).getState();
							if(aState.ordinal()	==	state){
								pendingElements.add(elements.get(key));
								addedElement=true;
								
							}
						}
						if(addedElement){
							addedGrid=true;
							gridPending.add(g);
							String temp=projList.get(i).getId()+"-"+g.getId();
							projectGridsMajorPendingElements.put(temp, pendingElements);
						}
					}
					
				}
				if(addedGrid){
					projPending.add(projList.get(i));
					projectGrids.put(projList.get(i).getId()+"", gridPending);
					
					
				}
				
			}
			
			
		}
		model.addAttribute("PendingProjects", projPending);
		model.addAttribute("PendingProjectsGrids", projectGrids);
		model.addAttribute("PendingProjectsGridsElements", projectGridsMajorPendingElements);
		
		System.out.println(projectGridsMajorPendingElements.toString());
		return "resolutionDashBoard";
    }
	@RequestMapping(value = "/majorcolliding", method = RequestMethod.GET)
    public String majorcollidingView(Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		//get lista colliding major updates
		return "majorcolliding";
    }
	@RequestMapping(value = "/minorcolliding", method = RequestMethod.GET)
    public String minorcollidingView(Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		//get lista colliding minor updates
		return "minorcolliding";
    }
	
	@RequestMapping(value = "/grids", method = RequestMethod.GET)
    public String listAllGrids(Model model) {
		model.addAttribute("pageTitle", "Lista Grids");
		this.setActiveButton(1, model);
		List<Grid> temp= this.gridService.listAllGrids();
		model.addAttribute("nGrids", temp.size());
        model.addAttribute("listGrids", temp);
        return "grids";
    }
	
	private void setActiveButton(int i, Model model) {
		for (int j=0;j<this.nMenuButtons; j++){
			if(j==i) model.addAttribute("navClass"+j, "class=\"active\"");
			else model.addAttribute("navClass"+j, "");
		}
		
	}

	@RequestMapping(value = "/grids/{id}")
    public String getGrid(@PathVariable("id") int id, Model model) {
		model.addAttribute("pageTitle", "Lista Grids");
		this.setActiveButton(1, model);
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
			String chart="chart_config = {chart: { connectors: {type: \"bCurve\",style: {\"stroke-width\": 2}}, container: \"#gridChart\", siblingSeparation:70, rootOrientation:'WEST',  subTeeSeparation:70, animateOnInit: true,node: {collapsable: true},animation: {nodeAnimation: \"easeOutBounce\",nodeSpeed: 700,connectorsAnimation: \"bounce\",connectorsSpeed: 700}},";		
			chart=chart+"nodeStructure: {innerHTML:\"<div class=\'nodeTxt\'><div class='txtProjectTitle'>"+g.getProject().getProjectId()+"</div><div class='txtElement'>"+g.getProject().getDescription()+"</div></div>\",children: [";
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
			List<Object> newStack=new ArrayList<Object>();
			GridElement ge=(GridElement)stack.get(i);
			name=stack.get(i).getClass().getSimpleName()+" "+ge.getLabel()+" - <i>v"+ge.getVersion()+"</i>";
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
							String txt=tempField.getName()+": </i> "+fieldValueStr;
							int maxLength=60;
							if(txt.length()>maxLength) txt=txt.substring(0, maxLength)+"...";
							desc=desc+"<div class='txtElement'><i>"+txt+"</div>";
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
			chart=chart+"{ innerHTML:\"<div class=\'nodeTxt\'><div class='txtElementTitle'><div class='nodeImg' ></div>"+name+"</div>"+desc+"</div>\", ";
			//chart=chart+"{text: { name: \""+name+"\", desc: \""+desc+"\" },innerHTML:\"<div><h1>test</h1></div>\", collapsed: true";
			if(newStack.size()>0){
				chart=chart+" collapsed: true ,children: [";
				chart=chart+updateChart(newStack);
				chart=chart+"]";
			}
			else chart=chart+" collapsed: false";
			chart=chart+"}";
			if (i<stack.size()-1) chart=chart+",";
		}
		return chart;
		
	}
	
	
	
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String listAllProjects(Model model) {
		model.addAttribute("pageTitle", "Lista Progetti");
		this.setActiveButton(0, model);
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
	
	
	
	@RequestMapping(value = "/elementhistory/{type}/{label}")
    public String getElementHistory(@PathVariable("label") String label, @PathVariable("type") String type, Model model) {
		List <GridElement> tempList=this.gridElementService.getElementLog(label, type);
		model.addAttribute("nGridElements", tempList.size());
        model.addAttribute("listGridElements", tempList);
        //System.out.println(tempList.toString());
		return "element";
    }
	
	@RequestMapping(value = "/element/{type}/{id}")
    public String getElementVers(@PathVariable("id") int id, @PathVariable("type") String type,Model model) {
		GridElement  ge=this.gridElementService.getElementById(id, type);
		model.addAttribute("element", ge);
		//System.out.println(this.gridElementToFormattedString(ge));
		return "element";
    }
	
	
	
	@RequestMapping(value = "/grids/add", method=RequestMethod.POST)
    public @ResponseBody String addGrid(@RequestBody String jsonData) {
		System.out.println(jsonData.toString());
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

	
	@RequestMapping(value = "/updategrid", method = RequestMethod.GET)
    public String updateGridPage(Model model) {
		return "updategrid";
    }
	
	public String gridElementToFormattedString(GridElement ge){
		String name=ge.getClass().getSimpleName()+" "+ge.getLabel()+" - <i>v"+ge.getVersion()+"</i><br>";
		String desc="";
		String references="";
		Field[] fields=ge.getClass().getDeclaredFields();
		for(int j=0; j<fields.length;j++){
			Field tempField=fields[j];
			tempField.setAccessible(true);
			try {
				Object fieldValue=tempField.get(ge);
				if(fieldValue instanceof GridElement){
					GridElement currentGE=(GridElement) fieldValue;
					if (references.length()==0){
						references=references+"riferimenti: "+currentGE.getLabel()+" v"+currentGE.getVersion();
					}
					else{
						references=references+", "+currentGE.getLabel()+" v"+currentGE.getVersion();
					}
				}
				else if(fieldValue instanceof List){
					List myList 	=	(List)fieldValue;
					if(myList.size()>0){
						Object	first	=	 myList.get(0);
						if(first instanceof GridElement){
							GridElement currentGE=(GridElement) first;
							if (references.length()==0){
								references=references+"riferimenti: "+currentGE.getLabel()+" v"+currentGE.getVersion();
							}
							else{
								references=references+", "+currentGE.getLabel()+" v"+currentGE.getVersion();
							}
							
							
						}
						//TODO gestire array stringhe
					}
				}
				else{
					//desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+": "+fieldValueStr+"</div>";
					if(fieldValue!=null){
						String fieldValueStr	=	(String)fieldValue.toString();
						String txt=tempField.getName()+": </i> "+fieldValueStr;
						int maxLength=60;
						if(txt.length()>maxLength) txt=txt.substring(0, maxLength)+"...";
						desc=desc+"<div class='txtElement'><i>"+txt+"</div>";
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
		return name+"\n\t"+desc+"\n\t"+references;
	}
	
	
     
}
