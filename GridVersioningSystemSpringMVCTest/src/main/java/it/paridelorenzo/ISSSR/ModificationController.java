package it.paridelorenzo.ISSSR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import grid.JSONFactory;
import grid.Utils;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectFieldModification;
import grid.modification.elements.ObjectModificationService;
import grid.modification.grid.Conflict;
import grid.modification.grid.GridElementAdd;
import grid.modification.grid.GridElementRemove;
import grid.modification.grid.GridModification;
import grid.modification.grid.GridModificationService;

@Controller
public class ModificationController {
	private GridElementService 		gridElementService;
	private GridService				gridService;
	private ProjectService			projectService;
	private GridModificationService gridModificationService;
	private static final Logger logger = LoggerFactory.getLogger(ModificationController.class);
	private ConflictService			conflictService;
	
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
	
	@Autowired(required=true)
	@Qualifier(value="gridModificationService")
	public void setGridModificationService(GridModificationService gridModificationService) {
		this.gridModificationService = gridModificationService;
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
			if(anObject.has("newGrid")){
				jsonData	=	anObject.get("newGrid").toString();
			}
			temp = JSONFactory.loadFromJson(jsonData, this.projectService);
			Grid referenceGrid	=	null;
			Grid latestGrid;	
			referenceGrid	=	this.gridService.getLatestGrid(temp.getProject().getId());
			latestGrid		=	referenceGrid;
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
			ArrayList<String> modifiedObjectLabels	=	modifiedObjects(referenceGrid,latestGrid);
			System.out.println("MODIFIED ELEMENTS -> "+modifiedObjectLabels.size());
			for(int t=0;t<modifiedObjectLabels.size();t++){
				System.out.println("MODIFIED ELEMENT -> "+modifiedObjectLabels.get(t));
			}
			System.out.println("###~~~~VERSIONE GRID CARICATA"+referenceGrid.getVersion());
			if(referenceGrid	==	null){
				return "non esiste grid per questo progetto";
			}
			else{
				//TODO se e' update nel "nostro formato" vai a prendere la grid di riferimento e fai il check per i conflitti
				List<Modification>	mods		=	GridModificationService.getModification(latestGrid, temp);
				for(int i=0;i<mods.size();i++){
					System.out.println("found modification "+mods.get(i).toString());
				}
				if(mods.size()==0){
					return "non ci sono modifiche";
				}
				else{
					Grid newVersion	=	this.gridModificationService.applyModifications(mods, latestGrid, modifiedObjectLabels);
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
	
	@RequestMapping(value = "/singlemodificationtest", method = RequestMethod.GET)
	public String hometest(Locale locale, Model model) {
		//TODO get project id from path
		int projID	=	1;
		String everything	=	Utils.loadFile("gridMod.txt");
		JSONObject modification	=	new JSONObject(everything);
		int refVersion	=	-1;
		if(modification.has("refVersion")){
			refVersion	=	modification.getInt("refVersion");
		}
		Grid referenceGrid	=	null;
		referenceGrid		=	this.gridService.getLatestGrid(projID);
		Grid latestGrid		=	referenceGrid;
		if(refVersion>-1){
			List<Grid> grids	=	this.gridService.getGridLog(projID);
			for(Grid g:grids){
				if(g.getVersion()==refVersion){
					referenceGrid	=	g;
				}
			}
		}
		//TODO check "a 3"
		ArrayList<String> modifiedObjectLabels	=	null;
		HashMap<String,Object> loadedObjs		=	new HashMap<String,Object>();	
		try {
			modifiedObjectLabels	=	modifiedObjects(referenceGrid,latestGrid);
			JSONObject 	mods		=	(JSONObject) modification.get("modifiche");
			ArrayList<GridElement> 	modifiedElementsArray	=	new ArrayList<GridElement>();
			if(mods.has("goals")){
				JSONArray goalsArray	=	(JSONArray) mods.get("goals");
				for(int i=0;i<goalsArray.length();i++){
					modifiedElementsArray.add(JSONFactory.loadGoalFromJson(goalsArray.getString(i), loadedObjs));
				}
			}
			if(mods.has("metrics")){
				JSONArray goalsArray	=	(JSONArray) mods.get("metrics");
				for(int i=0;i<goalsArray.length();i++){
					modifiedElementsArray.add(JSONFactory.loadGoalFromJson(goalsArray.getString(i), loadedObjs));
				}
			}
			if(mods.has("questions")){
				JSONArray goalsArray	=	(JSONArray) mods.get("questions");
				for(int i=0;i<goalsArray.length();i++){
					modifiedElementsArray.add(JSONFactory.loadGoalFromJson(goalsArray.getString(i), loadedObjs));
				}
			}
			if(mods.has("strategies")){
				JSONArray goalsArray	=	(JSONArray) mods.get("strategies");
				for(int i=0;i<goalsArray.length();i++){
					modifiedElementsArray.add(JSONFactory.loadGoalFromJson(goalsArray.getString(i), loadedObjs));
				}
			}
			if(mods.has("measurementgoals")){
				JSONArray goalsArray	=	(JSONArray) mods.get("measurementgoals");
				for(int i=0;i<goalsArray.length();i++){
					modifiedElementsArray.add(JSONFactory.loadGoalFromJson(goalsArray.getString(i), loadedObjs));
				}
			}
			//actualize links to other objects
			HashMap<String,GridElement> allElements	=	latestGrid.obtainAllEmbeddedElements();
			Iterator keys	=	allElements.keySet().iterator();
			while(keys.hasNext()){
				GridElement anElement	=	allElements.get(keys.next());
				for(int i=0;i<modifiedElementsArray.size();i++){
					modifiedElementsArray.get(i).updateReferences(anElement, false);
				}
			}
			//get modification on objects on intersection
			ArrayList<Modification> modifications	=	new ArrayList<Modification>();
			for(int i=0;i<modifiedElementsArray.size();i++){
				String key	=	modifiedElementsArray.get(i).getLabel();
				if(allElements.containsKey(key)){	//element is in intersection
					modifications.addAll(ObjectModificationService.getModification(allElements.get(key), modifiedElementsArray.get(i)));
				}
			}
			//TODO check modifications loaded
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "home";
	}
	
	public static ArrayList<String> modifiedObjects(Grid olderGrid, Grid newerGrid) throws Exception{
		ArrayList<String>	modifiedObjectLabels	=	new ArrayList<String>();
		logger.info("checking modified elements label");
		if(olderGrid!=newerGrid){
			logger.info("from different grids v"+olderGrid.getVersion()+" v"+newerGrid.getVersion());
			List<Modification>	mods		=	GridModificationService.getModification(olderGrid, newerGrid);
			logger.info("mods size: "+mods.size());
			for(Modification m:mods){
				if(m instanceof GridElementModification){
					String modObjLabel	=	((GridElementModification) m).getSubjectLabel();
					if(!modifiedObjectLabels.contains(modObjLabel)){
						logger.info("adding label "+modObjLabel);
						modifiedObjectLabels.add(modObjLabel);	
					}
				}
				else if(m instanceof GridElementAdd){
					String modObjLabel	=	((GridElementAdd) m).getGridElementAdded().getLabel();
					if(!mods.contains(modObjLabel)){
						logger.info("adding label "+modObjLabel);
						modifiedObjectLabels.add(modObjLabel);	
					}
				}
				else if(m instanceof GridElementRemove){
					String modObjLabel	=	((GridElementRemove) m).getRemovedObjectLabel();
					if(!mods.contains(modObjLabel)){
						logger.info("adding label "+modObjLabel);
						modifiedObjectLabels.add(modObjLabel);	
					}
				}
				else{
					logger.info("not managed");
				}
			}
		}
		return modifiedObjectLabels;
	}

	@RequestMapping(value = "/modifiche", method = RequestMethod.GET)
	public String hometestmod(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );	
		try {
			logger.info("loadfile");
			BufferedReader reader	=	new BufferedReader(new FileReader("modifiche.txt"));
			String text	=	"";
			String line	=	reader.readLine();
			while(line!=null){
				text	=	text+line;
				line	=	reader.readLine();
			}
			ArrayList<Modification> mods;
			Grid refGrid	=	this.gridService.getLatestGrid(1);
			
			//test
			HashMap<String,GridElement> elements	=	refGrid.obtainAllEmbeddedElements();
			System.out.println("###elementi su grid "+elements.keySet());
			
			JSONFactory testFactory	=	new JSONFactory();
			logger.info("JSON loaded "+text);
			logger.info("loaded grid version "+refGrid.getVersion()+" for project "+refGrid.getProject().getProjectId());
			mods	=	testFactory.loadModificationJson(text, refGrid);
			for(int i=0;i<mods.size();i++){
				System.out.println(mods.get(i).toString());
			}
			Grid newVersion	=	this.gridModificationService.applyModifications(mods, refGrid, modifiedObjectLabels);
			this.gridService.addGrid(newVersion);
			
		} catch (FileNotFoundException e) {
			logger.info("file not found");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("io exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "home";
	}
	
	
}
