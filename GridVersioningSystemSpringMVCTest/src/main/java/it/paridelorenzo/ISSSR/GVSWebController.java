package it.paridelorenzo.ISSSR;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
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

import com.google.gson.Gson;

import grid.JSONFactory;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.GridElement.State;
import grid.entities.Project;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.grid.GridModificationService;
 
 
@Controller
public class GVSWebController {
     
	private GridElementService 	gridElementService;
	private GridModificationService gridModificationService;
	private GridService			gridService;
	private ProjectService		projectService;
	private JSONFactory 		jFact;
	private int nMenuButtons=3;
	
	@Autowired(required=true)
	@Qualifier(value="gridElementService")
	public void setGridElementService(GridElementService gridElementService) {
		this.gridElementService = gridElementService;
	}
	
	@Autowired(required=true)
	@Qualifier(value="gridModificationService")
	public void setGridModificationService(GridModificationService gridModificationService) {
		this.gridModificationService = gridModificationService;
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
	
	//======================================================================

	
	@RequestMapping(value = "/resolutionDashBoard", method = RequestMethod.GET)   //stati WORKING, MAJOR_UPDATING,MAJOR_CONFLICTING, MINOR_CONFLICTING, FINAL_KO
    public String resolutionDashBoardView(Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		this.setActiveButton(2, model);
		List<String> pendingLabels=new ArrayList<String>();
		int nPendingChanges=0;
		List<Project> projList=this.projectService.listProjects();
		List<Project> projPending=new ArrayList<Project>();
		Map <String, List<Grid>> projectPendingGrids=new HashMap<>();	//map projid, list pending grids
		Map <String, List<GridElement>> projectGridsMajorPendingElements=new HashMap<>();
		Map <String, List<GridElement>> projectGridsMajorConflictElements=new HashMap<>();
		Map <String, List<GridElement>> projectGridsMinorConflictElements=new HashMap<>();
		Map <String, String> projectGridsMainGoalListChanged=new HashMap<>();
		if (projList.size()>0){
			for (int i=0; i<projList.size(); i++){
				List<Grid> projGrids=this.gridService.getGridLog(projList.get(i).getId());
				List<Grid> gridPending=new ArrayList<Grid>();
				boolean addedGrid=false;
				for(Grid g: projGrids){
					if(g.isMainGoalsChanged()){
						addedGrid=true;
						String temp=projList.get(i).getId()+"-"+g.getId();
						projectGridsMainGoalListChanged.put(temp, "changed");
						gridPending.add(g);
						
					}
					if(g.obtainGridState()==Grid.GridState.UPDATING){
						addedGrid=true;
						HashMap<String, GridElement> elements=g.obtainAllEmbeddedElements();
						Set<String>	keySet		=	elements.keySet();
						Iterator<String> anIterator	=	keySet.iterator();
						boolean addedMPElement=false;
						boolean addedMCElement=false;
						boolean addedmCElement=false;
						List<GridElement> majpendingElements=new ArrayList<GridElement>();
						List<GridElement> majconflElements=new ArrayList<GridElement>();
						List<GridElement> minconflElements=new ArrayList<GridElement>();
						while(anIterator.hasNext()){
							String key		=	anIterator.next();
							State aState	=	elements.get(key).getState();
							if(aState==GridElement.State.MAJOR_CONFLICTING){
								majconflElements.add(elements.get(key));
								addedMCElement=true;
							}
							if(aState==GridElement.State.MAJOR_UPDATING){
								majpendingElements.add(elements.get(key));
								addedMPElement=true;
							}
							if(aState==GridElement.State.MINOR_CONFLICTING){
								minconflElements.add(elements.get(key));
								addedmCElement=true;
							}
						}
						String temp=projList.get(i).getId()+"-"+g.getId();
						if(addedMCElement||addedMPElement||addedmCElement){
							addedGrid=true;
							if(!g.isMainGoalsChanged())gridPending.add(g);
						}
						if(addedMCElement){
							projectGridsMajorConflictElements.put(temp, majconflElements);
						}
						if(addedMPElement){
							projectGridsMajorPendingElements.put(temp, majpendingElements);
						}
						if(addedmCElement){
							projectGridsMinorConflictElements.put(temp, minconflElements);
						}
						
					}
				
				}
				if(addedGrid){
					projPending.add(projList.get(i));
					projectPendingGrids.put(projList.get(i).getId()+"", gridPending);
				}
			
			}
		}
		
		System.out.println(projectPendingGrids.toString());
		
		model.addAttribute("PendingProjects", projPending);
		model.addAttribute("PendingProjectsGrids", projectPendingGrids);
		model.addAttribute("MajorPendingProjectsGridsElements", projectGridsMajorPendingElements);
		model.addAttribute("MajorConflictProjectsGridsElements", projectGridsMajorConflictElements);
		model.addAttribute("MinorConflictProjectsGridsElements", projectGridsMinorConflictElements);
		model.addAttribute("GridsMainGoalListChanged", projectGridsMainGoalListChanged);
		model.addAttribute("GridModificationServiceInstance", this.gridModificationService);
		model.addAttribute("GridElementServiceInstance", this.gridElementService);
		return "resolutionDashBoard";
    }
	
	
	
	@RequestMapping(value = "/GEResolution/{type}/{label}", method = RequestMethod.GET)
	public String GEResolution(@PathVariable("type") String type,@PathVariable("label") String label,Model model) {
		model.addAttribute("pageTitle", "Grids Versioning System");
		List <GridElement> geList=this.gridElementService.getElementByLabelAndState(label, type, GridElement.State.MAJOR_UPDATING);
		geList.addAll(this.gridElementService.getElementByLabelAndState(label, type, GridElement.State.MAJOR_CONFLICTING));
		geList.addAll(this.gridElementService.getElementByLabelAndState(label, type, GridElement.State.MINOR_CONFLICTING));
		GridElement workingGE=this.gridElementService.getLatestWorking(label, type);
		if (geList.size()>0&&workingGE!=null){
			model.addAttribute("workingGE", workingGE);
			model.addAttribute("updatingElements", geList);
			
		}
		else if (workingGE!=null&&geList.size()==0){
			model.addAttribute("error", "The requested Grid Element is in a consistent state");
		}
		else{
			model.addAttribute("error", "The requested Grid Element is not available");
		}
		model.addAttribute("GEService", this.gridElementService);
		return "GEResolution";
	}
	
	
	@RequestMapping(value = "/solveUpdate", method=RequestMethod.POST)
    public @ResponseBody String solveUpdate(@RequestBody String jsonData) {
		String type=jsonData.substring(0, jsonData.indexOf(","));
		int nconflict=Integer.parseInt(jsonData.substring(jsonData.indexOf(",")+1,jsonData.indexOf("{") ));
		Gson gson = new Gson();
		String jsonGE=jsonData.substring(jsonData.indexOf("{"), jsonData.length());
		GridElement ge=null;
		try {
			ge = (GridElement)gson.fromJson(jsonGE, Class.forName(type));
			List<GridElement> pending=this.gridElementService.getElementByLabelAndState(ge.getLabel(), Class.forName(type).getSimpleName(), GridElement.State.MAJOR_CONFLICTING);
			pending.addAll(this.gridElementService.getElementByLabelAndState(ge.getLabel(), Class.forName(type).getSimpleName(), GridElement.State.MAJOR_UPDATING));
			pending.addAll(this.gridElementService.getElementByLabelAndState(ge.getLabel(), Class.forName(type).getSimpleName(), GridElement.State.MINOR_CONFLICTING));
			//System.out.println("pendingsize:"+pending.size());
			if(pending.size()==nconflict){
				//if ge is not linked to pending objects
				boolean withPending=false;
				
				for(GridElement currentGE: pending){
					if((!withPending)&&(this.gridModificationService.isEmbeddedPending(currentGE))){
						withPending=true;
					}
				}
				
				//apply modifications to grid element
				if(!withPending)this.gridModificationService.applyAModificationToASingleElement(ge);
				else {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", "error");
					jsonObject.put("msg", "Cannot update: linked to pending elements");
					return jsonObject.toString();
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "success");
				jsonObject.put("msg", "The conflict has been solved");
				return jsonObject.toString();
				
			}
			else if(pending.size()>nconflict){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "Other incoming updates to solve");
				return jsonObject.toString();
				
			}
			else if(pending.size()==0){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "No pending elements to solve");
				return jsonObject.toString();
				
			}
			else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "System Error, please contact the System Administrator");
				return jsonObject.toString();
			}
			
			
			
			
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", "error");
			jsonObject.put("msg", "Generic Exception, please contact the System Administrator");
			e.printStackTrace();
			return jsonObject.toString();
			
		} 
		
    }
	
	@RequestMapping(value = "/MGResolution/{pid}/{gid}", method = RequestMethod.GET)
    public String mainGoalResolution(@PathVariable("gid") int gid,@PathVariable("pid") int pid,Model model) {
		model.addAttribute("pageTitle", "Lista Grids");
		this.setActiveButton(2, model);
		Grid working= this.gridService.getLatestWorkingGrid(pid);
		Grid current=this.gridService.getGridById(gid);
		List<String> mergedGoalLabels=new ArrayList<String>();
		if(working==null) {
			model.addAttribute("error", "The working Grid is not available");
		}
		else if(current==null) {
			model.addAttribute("error", "The requested Grid is not available");
		}
		else if((!current.isMainGoalsChanged())){
			model.addAttribute("error", "The requested Grid is not in pending state");
		}
		else{
			String workingMGList="['"+pid+"', '"+current.getId()+"', ";
			List<Goal> temp=working.getMainGoals();
			for(int i=0; i<temp.size(); i++){
				if(!mergedGoalLabels.contains(temp.get(i).getLabel())){
					mergedGoalLabels.add(temp.get(i).getLabel());
				}
				if(i<temp.size()-1){
					workingMGList=workingMGList+"'"+temp.get(i).getLabel()+"',";
				}
				else{
					workingMGList=workingMGList+"'"+temp.get(i).getLabel()+"'";
				}
			}
			workingMGList=workingMGList+"]";
			
			
			String currentMGList="['"+pid+"', '"+current.getId()+"', ";
			temp=current.getMainGoals();
			for(int i=0; i<temp.size(); i++){
				if(!mergedGoalLabels.contains(temp.get(i).getLabel())){
					mergedGoalLabels.add(temp.get(i).getLabel());
				}
				if(i<temp.size()-1){
					currentMGList=currentMGList+"'"+temp.get(i).getLabel()+"',";
				}
				else{
					currentMGList=currentMGList+"'"+temp.get(i).getLabel()+"'";
				}
			}
			currentMGList=currentMGList+"]";
			model.addAttribute("mergedGoalLabels", mergedGoalLabels);
			model.addAttribute("workingGrid", working);
	        model.addAttribute("currentGrid", current);
	        model.addAttribute("workingMGList", workingMGList);
	        model.addAttribute("currentMGList", currentMGList);
	        model.addAttribute("GEService", this.gridElementService);
		}
        return "MGResolution";
    }
	
	@RequestMapping(value = "/MGListUpdate", method=RequestMethod.POST)
    public @ResponseBody String updateMainGoalList(@RequestBody String jsonData) {
		System.out.println(jsonData);
		//Array format: [projId, gridSolved, {MaingoalList}]
		JSONArray jsonArray = new JSONArray(jsonData);
		int prjId=0;
		int gridToSolveId=0;
		List<Goal> mainGoalList= new ArrayList<Goal>();
		for (int i=0; i<jsonArray.length(); i++) {
		    if(i==0) prjId=Integer.parseInt(jsonArray.getString(i));
		    else if (i==1){
		    	gridToSolveId=Integer.parseInt(jsonArray.getString(i));
		    }
		    else {
		    	Goal current=(Goal)this.gridElementService.getLatestWorking(jsonArray.getString(i), "Goal");
		    	if(current!=null&&!mainGoalList.contains(current)){
		    		mainGoalList.add(current);
		    	}
		    	else if(current==null){
		    		JSONObject jsonObject = new JSONObject();
		    		jsonObject.put("type", "error");
		    		jsonObject.put("msg", "Cannot get working element with label: "+ jsonArray.getString(i));
		    		//System.out.println("error, cannot get working element with label: "+ jsonArray.getString(i));
		    		return jsonObject.toString();
		    		
		    	}
		    	
		    }
		}
		Grid workingGrid=this.gridService.getLatestWorkingGrid(prjId);
		Grid gridToSolve=this.gridService.getGridById(gridToSolveId);
		if(workingGrid==null){
			JSONObject jsonObject = new JSONObject();
    		jsonObject.put("type", "error");
    		jsonObject.put("msg", "Cannot get latest working grid for project: "+ prjId);
    		//System.out.println("error, cannot get latest working grid for project: "+ prjId);
    		return jsonObject.toString();
		}
		else if(gridToSolve==null){
			JSONObject jsonObject = new JSONObject();
    		jsonObject.put("type", "error");
    		jsonObject.put("msg", "error, cannot get grid with id: "+ gridToSolveId);
    		System.out.println("error, cannot get grid with id: "+ gridToSolveId);
    		return jsonObject.toString();
		}
		else if((!gridToSolve.isMainGoalsChanged())){
			JSONObject jsonObject = new JSONObject();
    		jsonObject.put("type", "error");
    		jsonObject.put("msg", "grid with id: "+ gridToSolveId+" has been already solved");
    		System.out.println("error, cannot get grid with id: "+ gridToSolveId);
    		return jsonObject.toString();
		}
		else if(mainGoalList.size()==0){
			JSONObject jsonObject = new JSONObject();
    		jsonObject.put("type", "error");
    		jsonObject.put("msg", "Main goal list can't be empty");
    		System.out.println("error, Main goal list can't be empty");
    		return jsonObject.toString();
		}
		else{
			boolean solvable=true;
			for(Goal ge: mainGoalList){
				if(solvable){
					List<GridElement> pending=this.gridElementService.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MAJOR_CONFLICTING);
					pending.addAll(this.gridElementService.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MAJOR_UPDATING));
					pending.addAll(this.gridElementService.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MINOR_CONFLICTING));
					if(pending.size()>0){
						solvable=false;
					}
					if(solvable){
						if(this.gridModificationService.isEmbeddedPending(ge)) solvable=false;
					}
				}
			}
			if(solvable){
				for(GridElement ge: mainGoalList){
					System.out.println(ge.getLabel()+"-"+ge.getVersion()+"-"+ge.getState());
				}
				Grid newGrid=workingGrid.clone();
				newGrid.setVersion(this.gridService.getLatestGrid(prjId).getVersion()+1);
				newGrid.setMainGoals(mainGoalList);
				newGrid	=	this.gridModificationService.refreshLinks(newGrid);
				this.gridService.addGrid(newGrid);
				gridToSolve.setMainGoalsChanged(false);
				this.gridService.updateGrid(gridToSolve);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "success");
				jsonObject.put("msg", "Main goal list updated");
				return jsonObject.toString();
				
			}
			else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "Other pending elements to solve before");
				return jsonObject.toString();
				
			}
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	//======================================================================
	
	
	
	
	
	@RequestMapping(value = "/grids", method = RequestMethod.GET)
    public String listAllGrids(Model model) {
		model.addAttribute("pageTitle", "Lista Grids");
		this.setActiveButton(1, model);
		List<Grid> temp= this.gridService.listAllGrids();
		Map<String, String> status=new HashMap<String,String>();
		for(Grid g: temp){
			Grid tempWorking= this.gridService.getLatestWorkingGrid(g.getProject().getId());
			String state="";
			if(g.isMainGoalsChanged()) {
				state=state+"MGC";
				if(g.obtainGridState()==Grid.GridState.UPDATING){
					state=state+"-UPDATING";
				}
			}
			else{
				if(g.obtainGridState()==Grid.GridState.WORKING){
					if (g.getVersion()<tempWorking.getVersion()){
						state=state+"ARCHIVED";
					}
					else{state=state+g.obtainGridState().name();}
				}
				else{state=state+g.obtainGridState().name();}
			}
			
			
			
			
			status.put(g.getId()+"", state);
		}
		System.out.println(status);
        model.addAttribute("listGrids", temp);
        model.addAttribute("status", status);
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
			String chart="chart_config = {chart: { connectors: {type: \"bCurve\",style: {\"stroke-width\": 2}}, container: \"#gridChart\", siblingSeparation:70, rootOrientation:'WEST',  subTeeSeparation:70, animateOnInit: true,node: {collapsable: true},animation: {nodeAnimation: \"easeInSine\",nodeSpeed: 500,connectorsAnimation: \"linear\",connectorsSpeed: 500}},";		
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
			desc="<div class='txtElement'><i>"+ge.getState().name()+"</i></div>";
			//TODO this part is not the same in utils?? (obtain HTML)
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
						}
					}
					else{
						//desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+": "+fieldValueStr+"</div>";
						if(fieldValue!=null){
							if(!tempField.getName().equals("logger")){
							String fieldValueStr	=	(String)fieldValue.toString();
							String txt=tempField.getName()+": </i> "+fieldValueStr;
							int maxLength=60;
							if(txt.length()>maxLength) txt=txt.substring(0, maxLength)+"...";
							desc=desc+"<div class='txtElement'><i>"+txt+"</div>";
							}
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
        Map<String, String> status=new HashMap<String,String>();
		for(Grid g: templist){
			Grid tempWorking= this.gridService.getLatestWorkingGrid(g.getProject().getId());
			String state="";
			if(g.isMainGoalsChanged()) {
				state=state+"MGC";
				if(g.obtainGridState()==Grid.GridState.UPDATING){
					state=state+"-UPDATING";
				}
			}
			else{
				if(g.obtainGridState()==Grid.GridState.WORKING){
					if (g.getVersion()<tempWorking.getVersion()){
						state=state+"ARCHIVED";
					}
					else{state=state+g.obtainGridState().name();}
				}
				else{state=state+g.obtainGridState().name();}
			}
			status.put(g.getId()+"", state);
		}
		model.addAttribute("status", status);
        return "projects";
    }
	
	
	
	@RequestMapping(value = "/elementhistory/{type}/{label}")
    public String getElementHistory(@PathVariable("label") String label, @PathVariable("type") String type, Model model) {
		List <GridElement> tempList=this.gridElementService.getElementLog(label, type);
		model.addAttribute("nGridElements", tempList.size());
        model.addAttribute("listGridElements", tempList);
        model.addAttribute("type", type);
        model.addAttribute("label", label);
        //System.out.println(tempList.toString());
		return "elementhistory";
    }
	
	@RequestMapping(value = "/element/{type}/{id}")
    public String getElementVers(@PathVariable("id") int id, @PathVariable("type") String type,Model model) {
		GridElement ge=this.gridElementService.getElementById(id, type);
		if(ge!=null){
			model.addAttribute("element", ge);
			List<Object> newStack=new ArrayList<Object>();
			newStack.add(ge);
			String chart="chart_config = {chart: { connectors: {type: \"bCurve\",style: {\"stroke-width\": 2}}, container: \"#gridChart\", siblingSeparation:70, rootOrientation:'WEST',  subTeeSeparation:70, animateOnInit: true,node: {collapsable: true},animation: {nodeAnimation: \"easeInSine\",nodeSpeed: 500,connectorsAnimation: \"linear\",connectorsSpeed: 500}},";		
			chart=chart+"nodeStructure: "+updateChart(newStack)+"};";
			System.out.println(chart);
			model.addAttribute("gridTreeString",chart);
			model.addAttribute("GEService", this.gridElementService);
		}
		else{
			model.addAttribute("error","The requested Grid Element is not available");
		}
		
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
