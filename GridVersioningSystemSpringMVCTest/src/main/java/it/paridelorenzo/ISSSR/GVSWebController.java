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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import grid.JSONFactory;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Project;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectFieldModification;
import grid.modification.grid.Conflict;
import grid.modification.grid.GridModificationService;
import javassist.bytecode.Descriptor.Iterator;
 
 
@Controller
public class GVSWebController {
     
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ProjectService		projectService;
	private JSONFactory 		jFact;
	private ConflictService		conflictService;
	
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
	
	
	
	@RequestMapping(value = "/element/{type}/{id}")
    public String getElementHistory(@PathVariable("id") String id, @PathVariable("type") String type, Model model) {
		try {
			List <GridElement> temp=this.gridElementService.getElementLog(id, Class.forName(type));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "element";
    }
	/*@RequestMapping(value = "/element/{type}/{id}/{vers}")
    public String getElementVers(@PathVariable("id") String id,@PathVariable("vers") int vers, @PathVariable("type") String type,Model model) {
		GridElement  temp=this.gridElementService.getElementById(id, getClass());
		return "element";
    }*/
	
	
	
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
	
	@RequestMapping(value = "/grids/update", method=RequestMethod.POST)
    public @ResponseBody String updateGrid(@RequestBody String jsonData) {
		//Grid temp=JSONFactory.loadFromJson(jsonData, this.projectService);
		System.out.println(jsonData.toString());
		//this.gridService.addGrid(temp);
		JSONObject response	=	new JSONObject();
		Grid temp;
		try {
			JSONObject anObject	=	new JSONObject(jsonData);
			int refVersion		=	-1;
			String authorEmail	=	null;
			if(anObject.has("refVersion")){
				refVersion	=	anObject.getInt("refVersion");
			}
			if(anObject.has("grid")){
				jsonData	=	anObject.getString("newGrid");
			}
			temp = JSONFactory.loadFromJson(jsonData, this.projectService);
			Grid referenceGrid	=	null;
			Grid latestGrid;	
			referenceGrid	=	this.gridService.getLatestGrid(temp.getProject().getId());
			latestGrid		=	referenceGrid;
			ArrayList<String> modifiedObjectLabels	=	new ArrayList<String>();
			//TODO carciofo nota bene:
			/*
			 * se l'update e' rispetto l'ultima grid disponibile ovviamente non ci sara' nessun tipo di conflitto possibile
			 * in quanto la versione di riferimento e' l'ultima disponibile di default, se invece c'e' una versione di riferimento
			 * va tirata fuori una lista di gridelement modificati dalla versione di riferimento rispetto all'ultima disponibile
			 * e va verificato se l'oggetto che vado a modificare sta li dentro, in caso affermativo DEVO far partire il processo di 
			 * gestione del conflitto //TODO implementare controllo e gestione
			 */
			if(refVersion>-1){
				List<Grid> grids	=	this.gridService.getGridLog(temp.getProject().getId());
				for(int i=0;i<grids.size();i++){
					Grid current	=	grids.get(i);
					if(current.getVersion()==refVersion){
						referenceGrid	=	current;
					}
				}
			}
			if(referenceGrid!=latestGrid){
				List<Modification>	mods		=	GridModificationService.getModification(referenceGrid, latestGrid);
				for(Modification m:mods){
					if(m instanceof GridElementModification){
						String modObjLabel	=	((GridElementModification) m).getSubjectLabel();
						if(!mods.contains(modObjLabel)){
							modifiedObjectLabels.add(modObjLabel);	
						}
					}
				}
			}
			System.out.println("###~~~~VERSIONE GRID CARICATA"+referenceGrid.getVersion());
			if(referenceGrid	==	null){
				return "non esiste grid per questo progetto";
			}
			else{
				//TODO se e' update nel "nostro formato" vai a prendere la grid di riferimento e fai il check per i conflitti
				List<Modification>	mods		=	GridModificationService.getModification(referenceGrid, temp);
				Grid				toBeRemoved	=	null;	//temporary basic grid
				for(int i=0;i<mods.size();i++){
					System.out.println("found modification "+mods.get(i).toString());
				}
				if(mods.size()==0){
					return "non ci sono modifiche";
				}
				else{
					Grid 						newVersion	=	new Grid();
					newVersion.setMainGoals(referenceGrid.getMainGoals());
					ArrayList<Goal> mainGoalsCopy	=	new ArrayList<Goal>();
					List<Goal> gridMainGoals	=	referenceGrid.getMainGoals(); //a direct reference creates errors in hibernate (shared reference to a collection)	
					for(int i=0;i<gridMainGoals.size();i++){
						mainGoalsCopy.add(gridMainGoals.get(i));
					}
					newVersion.setMainGoals(mainGoalsCopy);
					newVersion.setProject(referenceGrid.getProject());
					newVersion.setVersion(referenceGrid.getVersion()+1);
					HashMap<String,GridElement> elements	=	referenceGrid.obtainAllEmbeddedElements();
					for(int i=0;i<mods.size();i++){
						Modification 	aMod	=	mods.get(i);
						GridElement 	subj;
						subj	=	elements.get(((ObjectFieldModification) aMod).getSubjectLabel());
						if(Modification.minorUpdateClass.contains(subj.getClass())){
							aMod.setModificationType(Modification.Type.Minor);
						}
						else{
							aMod.setModificationType(Modification.Type.Major);
						}
						if(aMod instanceof ObjectFieldModification){
							if(elements.containsKey(((ObjectFieldModification) aMod).getSubjectLabel())){
								GridElement cloned	=	subj.clone();
								cloned.setVersion(subj.getVersion()+1);
								((ObjectFieldModification) aMod).apply(cloned, newVersion);
								newVersion	=	this.gridService.updateGridElement(newVersion, cloned,false);
							}
							else return "error";
						}
						else{
							//TODO manage!!!!
							System.out.println("case to be manageD!!!!");
						}
					}
					HashMap<String,GridElement> oldElements	=	referenceGrid.obtainAllEmbeddedElements();
					HashMap<String,GridElement> newElements	=	newVersion.obtainAllEmbeddedElements();
					java.util.Iterator<String> anIt	=	oldElements.keySet().iterator();
					while(anIt.hasNext()){
						String key	=	anIt.next();
						if(newElements.containsKey(key)){
							GridElement oldElement 	=	oldElements.get(key);
							GridElement newElement 	=	newElements.get(key);
							if(newElement.getVersion()>oldElement.getVersion()){
								newElement.setVersion(oldElement.getVersion()+1);
								//if is minorupdate...
								if(Modification.minorUpdateClass.contains(newElement.getClass())){
									if(!this.gridElementService.isAddUpdate(oldElement, newElement)){
										if(modifiedObjectLabels.contains(newElement.getLabel())){
											//is a conflict
											Conflict aConflict	=	new Conflict();
											aConflict.setConflictState(Conflict.State.PENDING);
											aConflict.setConflictType(Conflict.Type.MINOR);
											newElement.setState(GridElement.State.MINOR_UPDATING);
											ArrayList<GridElement> elementList	=	new ArrayList<GridElement>();
											elementList.add(latestGrid.obtainAllEmbeddedElements().get(newElement.getLabel()));
											elementList.add(newElement);
											aConflict.setConflicting(elementList);
											//TODO this.conflictService
										}
									}	
								}
								else{//is major update
									if(!this.gridElementService.isAddUpdate(oldElement, newElement)){
										if(modifiedObjectLabels.contains(newElement.getLabel())){
											//is a conflict
											Conflict aConflict	=	new Conflict();
											aConflict.setConflictState(Conflict.State.PENDING);
											aConflict.setConflictType(Conflict.Type.MINOR);
											newElement.setState(GridElement.State.MINOR_UPDATING);
											ArrayList<GridElement> elementList	=	new ArrayList<GridElement>();
											elementList.add(latestGrid.obtainAllEmbeddedElements().get(newElement.getLabel()));
											elementList.add(newElement);
											aConflict.setConflicting(elementList);
										}
									}
								}
							}
						}
					}
					this.gridService.addGrid(newVersion);
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
