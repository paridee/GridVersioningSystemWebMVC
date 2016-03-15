package it.paridelorenzo.ISSSR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.firebase.client.Firebase;

import grid.JSONFactory;
import grid.Utils;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.entities.SubscriberPhase;
import grid.interfaces.services.DefaultResponsibleService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.PractitionerService;
import grid.interfaces.services.ProjectService;
import grid.interfaces.services.SubscriberPhaseService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectModificationService;
import grid.modification.grid.GridElementAdd;
import grid.modification.grid.GridElementRemove;
import grid.modification.grid.GridModification;
import grid.modification.grid.GridModificationService;

@Controller
public class ModificationController {
	private GridElementService 			gridElementService;
	private GridService					gridService;
	private ProjectService				projectService;
	private GridModificationService 	gridModificationService;
	private PractitionerService			practitionerService;
	private SubscriberPhaseService		subscriberPhaseService;
	private DefaultResponsibleService	defaultResponsibleService;
	private static final Logger logger = LoggerFactory.getLogger(ModificationController.class);
	private JSONFactory 		aFactory;
	
	
	@Autowired(required=true)
	@Qualifier(value="jsonFactory")
	public void setaFactory(JSONFactory aFactory) {
		this.aFactory = aFactory;
	}
	
	@Autowired(required=true)
	@Qualifier(value="defaultResponsibleService")
	public void setDefaultResponsibleService(DefaultResponsibleService defaultResponsibleService) {
		this.defaultResponsibleService = defaultResponsibleService;
	}

	@Autowired(required=true)
	@Qualifier(value="subscriberPhaseService")
	public void setSubscriberPhaseService(SubscriberPhaseService subscriberPhaseService) {
		this.subscriberPhaseService = subscriberPhaseService;
	}

	@Autowired(required=true)
	@Qualifier(value="practitionerService")
	public void setPractitionerService(PractitionerService practitionerService) {
		this.practitionerService = practitionerService;
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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView redToHome(Locale locale, Model model) {
		return new ModelAndView("redirect:/GVShome");
	}
	
	@RequestMapping(value = "/modifications_new", method = RequestMethod.GET)
	public String modNew(Locale locale, Model model) {
		
		//TODO this is a test, remove in release...
		
		logger.info("loadfile");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("modifiche_new.txt"));
			String text	=	"";
			String line	=	reader.readLine();
			while(line!=null){
				text	=	text+line;
				line	=	reader.readLine();
			}
			logger.info(text);
			JSONObject anObject	=	new JSONObject(text);
			//JSONFactory jsonFactory	=	new JSONFactory();
			int refVersion		=	-1;
			//String authorEmail	=	null;
			String aProjectId		=	null;		//label of the project
			//JSONFactory aFactory	=	new JSONFactory();
			JSONArray 	jsonData	=	null;
			JSONArray	mainGoalsJSONArray	=	null;
			if(anObject.has("refVersion")){
				refVersion	=	anObject.getInt("refVersion");
			}
			if(anObject.has("projectId")){
				aProjectId	=	anObject.get("projectId").toString();
			}
			if(anObject.has("changedObjects")){
				jsonData	=	anObject.getJSONArray("changedObjects");
			}
			if(anObject.has("mainGoalsList")){
				mainGoalsJSONArray	=	anObject.getJSONArray("mainGoalsList");
			}
			Project thisProject	=	this.projectService.getProjectByProjectId(aProjectId);
			Grid latestGrid							=	this.gridService.getLatestWorkingGrid(thisProject.getId());
			Grid refGrid	=	latestGrid;
			if(refVersion>-1){
				List<Grid> grids	=	this.gridService.getGridLog(thisProject.getId());
				for(int i=0;i<grids.size();i++){
					Grid current	=	grids.get(i);
					if(current.getVersion()==refVersion){
						refGrid	=	current;
					}
				}
			}
			logger.info("got latest working grid, id "+latestGrid.getId()+" state "+latestGrid.obtainGridState());
			ArrayList<String> modifiedObjectLabels	=	modifiedObjects(refGrid,latestGrid);
			HashMap<String,GridElement> latestEl	=	latestGrid.obtainAllEmbeddedElements();
			HashMap<String,Object>		modifiedEl	=	new HashMap<String,Object>();
			ArrayList<GridElement>		loadedElem	=	new ArrayList<GridElement>();	//redundant but maps elements on json
			logger.info("modded elements array sizev- "+jsonData.length());
			for(int i=0;i<jsonData.length();i++){
				logger.info("obj - "+jsonData.get(i).toString());
				loadedElem.add(aFactory.loadGridObj(jsonData.get(i).toString(), modifiedEl));
			}
			ArrayList<Modification> mods	=	new ArrayList<Modification>();
			for(int i=0;i<loadedElem.size();i++){
				ArrayList<GridElementModification> elMods	=	ObjectModificationService.getModification(latestEl.get(loadedElem.get(i).getLabel()), loadedElem.get(i));
				mods.addAll(elMods);
			}
			if(mods.size()>0||mainGoalsJSONArray!=null){
				logger.info("found "+mods.size()+" modifications");
				Grid newVersion	=	this.gridModificationService.applyModifications(mods, latestGrid, modifiedObjectLabels);
				if(mainGoalsJSONArray!=null){
					if(newVersion==null||newVersion.obtainGridState()==Grid.GridState.WORKING){
						newVersion	=	this.gridService.createStubUpgrade(latestGrid);
					}
					List<Goal> newMainGoals	=	new ArrayList<Goal>();
					for(int r=0;r<mainGoalsJSONArray.length();r++){
						String 	s		= 	(mainGoalsJSONArray.get(r)).toString();
						Goal	aGoal	=	(Goal)aFactory.loadGridObj(s,Utils.convertHashMap(newVersion.obtainAllEmbeddedElements()));
						newMainGoals.add(aGoal);
					}
					List<Modification> mgMods	=	GridModificationService.getMainGoalsModification(newVersion.getMainGoals(), newMainGoals);
					for(int r=0;r<mgMods.size();r++){
						GridModification mod	=	(GridModification) mgMods.get(r);
						mod.apply(newVersion);
					}
				}
				this.gridService.addGrid(newVersion);
			}
			else{
				logger.info("no modifications");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "home";
	}
	

	@RequestMapping(value = "/confEditor/{className}/{label}/{prjId}") //prjId needed to get the PM!!!
    public String getConfEditor(@PathVariable String className,@PathVariable String label,@PathVariable String prjId,Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName(); //get logged in username
	    Practitioner p	=	this.practitionerService.getPractitionerByEmail(email);
	    logger.info("classname "+className+" label "+label);
	    model.addAttribute("email", p.getEmail());
	    model.addAttribute("name", p.getName());
	    List<GridElement> 	confElements	=	this.gridElementService.getElementByLabelAndState(label, className, GridElement.State.MINOR_CONFLICTING);
	    @SuppressWarnings("rawtypes")
		Class classByName;
		try {
			classByName = Class.forName("grid.entities."+className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info("class not found "+"grid.entities."+className);
			return "home";
		}
	    if(!Modification.minorUpdateClass.contains(classByName)){
	    	logger.info("major class");
	    	confElements	=	this.gridElementService.getElementByLabelAndState(label, className, GridElement.State.MAJOR_CONFLICTING);
	    }
	    else{
	    	logger.info("minor class");
	    }
		GridElement 		working			=	this.gridElementService.getLatestWorking(label, className);
		confElements.add(0,working);
		if(working==null){
			return "home";
		}
		if(confElements.size()==0){
			return "home";
		}
		ArrayList<Practitioner> authorsL	=	new ArrayList<Practitioner>();
		if(Modification.minorUpdateClass.contains(classByName)){
			for(GridElement ge : confElements){
				List<Practitioner> authElement	=	ge.getAuthors();
				for(Practitioner pr: authElement){
					if(!authorsL.contains(pr)){
						authorsL.add(pr);
					}
				}
			}
		}
		else{
			Project aPrj	=	this.projectService.getProjectById(Integer.parseInt(prjId));
			if(aPrj!=null){
				Practitioner pm	=	aPrj.getProjectManager();
				if(pm!=null){
					authorsL.add(pm);
				}
			}
		}
		if(authorsL.size()>0){
			if(!authorsL.contains(p)){
				return "home";
			}
		}
		else{
			Practitioner defaultP;
			if(!Modification.minorUpdateClass.contains(classByName)){
				defaultP	=	this.defaultResponsibleService.getResponsibleByClassName("pm").getPractitioner();
			}
			else{
				defaultP	=	this.defaultResponsibleService.getResponsibleByClassName(confElements.get(0).getClass().getSimpleName()).getPractitioner();
			}
			authorsL.add(defaultP);
		}
		String pad	=	Utils.generateEditor(confElements,authorsL,p);
		model.addAttribute("pad", pad);
		return "editorGE";
	}
	
	@RequestMapping(value = "/registerToGVSNotification/{phase}/{prjId}", method=RequestMethod.POST)
    public @ResponseBody String registerToNotif(@PathVariable String phase,@PathVariable String prjId,@RequestBody String data){
		logger.info("registered phase");
		logger.info("data on post "+data);
		JSONObject response	=	new JSONObject();
		SubscriberPhase registered	=	new SubscriberPhase();
		try{
			registered.setPhase(Integer.parseInt(phase));
		}
		catch(Exception e){
			response.put("error", "wrong phase number");
			return response.toString();
		}
		registered.setUrl(data);
		try {
			prjId = URLDecoder.decode(StringEscapeUtils.unescapeHtml4(prjId),"UTF-8");
			logger.info("project id "+prjId);
		} catch (UnsupportedEncodingException e) {
			response.put("error", "project format error");
			e.printStackTrace();
			return response.toString();
		}
		Project aPrj	=	this.projectService.getProjectByProjectId(prjId);
		if(aPrj==null){
			response.put("error", "project not found");
			return response.toString();
		}
		registered.setaProject(aPrj);
		this.subscriberPhaseService.add(registered);
		response.put("result", "registered");
		return response.toString();
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getConflictResolution", method=RequestMethod.POST)
    public @ResponseBody String  confRes(@RequestBody String data){
		logger.info("arrived a resolution request");
		logger.info("data "+data);
		String escaped;
		try {
			escaped = URLDecoder.decode(StringEscapeUtils.unescapeHtml4(data),"UTF-8");
			String[] datas	=	escaped.split("#");
			HashMap<String,String> parsed	=	new HashMap<String,String>();
			for(int i=0;i<datas.length;i++){
				String[] 	innerData	=	datas[i].split("~");
				String 		label		=	innerData[0];
				String		value		=	innerData[1];
				parsed.put(label, value);
				logger.info("data read "+label+" "+value);
			}
			GridElement subj			=	this.gridElementService.getElementById(Integer.parseInt(parsed.get("id")),parsed.get("class")).clone();
			List<GridElement> pending	=	this.gridElementService.getElementByLabelAndState(subj.getLabel(), subj.getClass().getSimpleName(), GridElement.State.MAJOR_CONFLICTING);
			pending.addAll(this.gridElementService.getElementByLabelAndState(subj.getLabel(), subj.getClass().getSimpleName(), GridElement.State.MAJOR_UPDATING));
			pending.addAll(this.gridElementService.getElementByLabelAndState(subj.getLabel(), subj.getClass().getSimpleName(), GridElement.State.MINOR_CONFLICTING));
			if(pending.size()==0){
				return "Gia salvato";
			}
			subj.setState(GridElement.State.WORKING);
			Field[] fields		=	subj.getClass().getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				Field aField	=	fields[i];
				aField.setAccessible(true);
				if(parsed.containsKey(aField.getName())){
					logger.info("found field in parsed elements: "+aField.getName()+" old value "+aField.get(subj)+" new value "+parsed.get(aField.getName()));
					if(aField.getType().isAssignableFrom(List.class)){
						logger.info(aField.getType().getSimpleName()+" "+List.class.getSimpleName()+" "+aField.getType().isAssignableFrom(List.class));
						ParameterizedType aType	=	(ParameterizedType) aField.getGenericType();
				        Class<?> listClass = (Class<?>) aType.getActualTypeArguments()[0];
				        logger.info("list class" +listClass); // class java.lang.String.
						@SuppressWarnings("rawtypes")
						List aList	=	(List) aField.get(subj);
						aList.clear();
						String[] labelArray	=	parsed.get(aField.getName()).split(",");
						for(int j=0;j<labelArray.length;j++){
							String aLabel	=	labelArray[j];
							aLabel	=	aLabel.replace(",", "");
							aLabel	=	aLabel.replace("=", "");
							GridElement mostUp	=	this.gridElementService.getLatestWorking(aLabel, listClass.getSimpleName());
							if(mostUp!=null){
								aList.add(mostUp);	
							}
						}
					}
					else if(aField.getType().isAssignableFrom(String.class)){
						aField.set(subj, parsed.get(aField.getName()));
					}
					else if(aField.getType().isAssignableFrom(Integer.class)){
						aField.set(subj, Integer.parseInt(parsed.get(aField.getName())));
					}
					else if(aField.getType().isAssignableFrom(GridElement.class)){
						String aLabel	=	parsed.get(aField.getName());
						aLabel	=	aLabel.replace(",", "");
						aLabel	=	aLabel.replace("=", "");
						GridElement mostUp	=	this.gridElementService.getLatestWorking(aLabel, aField.getType().getSimpleName());
						aField.set(subj, mostUp);
					}
				}
			}
			logger.info("data escaped "+escaped);
			this.gridModificationService.applyAModificationToASingleElement(subj);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	@RequestMapping(value = "/grids/update", method=RequestMethod.POST)
    public @ResponseBody String updateGrid(@RequestBody String jsonData) {
		//Grid temp=JSONFactory.loadFromJson(jsonData, this.projectService);
		System.out.println(jsonData.toString());
		//this.gridService.addGrid(temp);
		Grid temp;
		try {
			JSONObject anObject	=	new JSONObject(jsonData);
			int refVersion		=	-1;
			if(anObject.has("refVersion")){
				refVersion	=	anObject.getInt("refVersion");
			}
			if(anObject.has("newGrid")){
				jsonData	=	anObject.get("newGrid").toString();
			}
			temp = aFactory.loadFromJson(jsonData, this.projectService);
			Grid referenceGrid	=	null;
			Grid latestGrid;
			//TODO get latest grid deve tornare ultima working grid
			String projectID	=	temp.getProject().getProjectId();
			Project	aProject	=	this.projectService.getProjectByProjectId(projectID);
			if(aProject==null){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "project "+projectID+" not found in DB");
				return jsonObject.toString();
			}
			referenceGrid	=	this.gridService.getLatestWorkingGrid(aProject.getId());
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
			if(referenceGrid	==	null){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "no grid available for this project, please upload a first one through the phase 2 entry point");
				return jsonObject.toString();
			}
			else{
				ArrayList<String> modifiedObjectLabels	=	modifiedObjects(referenceGrid,latestGrid);
				System.out.println("MODIFIED ELEMENTS -> "+modifiedObjectLabels.size());
				for(int t=0;t<modifiedObjectLabels.size();t++){
					System.out.println("MODIFIED ELEMENT -> "+modifiedObjectLabels.get(t));
				}
				//TODO se e' update nel "nostro formato" vai a prendere la grid di riferimento e fai il check per i conflitti
				List<Modification>	mods		=	GridModificationService.getModification(latestGrid, temp);
				for(int i=0;i<mods.size();i++){
					System.out.println("found modification "+mods.get(i).toString());
				}
				if(mods.size()==0){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", "success");
					jsonObject.put("msg", "Grid updated without modifications");
					return jsonObject.toString();
				}
				else{
					Grid newVersion	=	this.gridModificationService.applyModifications(mods, latestGrid, modifiedObjectLabels);
					this.gridService.addGrid(newVersion);
					Firebase myFirebaseRef = new Firebase("https://fiery-torch-6050.firebaseio.com/");
					Calendar calendar = Calendar.getInstance();
					long timestamp=calendar.getTime().getTime();
					myFirebaseRef.child("ISSSR/"+temp.getProject().getProjectId()).setValue(timestamp);
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", "success");
					jsonObject.put("msg", "Grid updated with modifications");
					return jsonObject.toString();
				}
			}
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", "error");
			jsonObject.put("msg", "Generic Exception");
			return jsonObject.toString();
		}
		
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
					logger.info("not managed "+m.getClass()+m+" <-");
				}
			}
		}
		return modifiedObjectLabels;
	}

	/**
	 * legacy support for modification, implies NO conflicts (reference version is latest)
	 * @param locale
	 * @param model
	 * @return
	 */
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
			reader.close();
			JSONObject 	modJson	=	new JSONObject(text);
			String 		prjId	=	"";
			if(modJson.has("projectId")){
				prjId	=	modJson.getString("projectId");
			}
			else{
				return	"invalid JSON, missing project";
			}
			Project aProject	=	this.projectService.getProjectByProjectId(prjId);
			if(aProject	==	null){
				return "project not found in DB";
			}
			ArrayList<Modification> mods;
			//TODO change with the right project
			Grid refGrid	=	this.gridService.getLatestWorkingGrid(aProject.getId());
			
			//test
			HashMap<String,GridElement> elements	=	refGrid.obtainAllEmbeddedElements();
			System.out.println("###elementi su grid "+elements.keySet());
			logger.info("JSON loaded "+text);
			logger.info("loaded grid version "+refGrid.getVersion()+" for project "+refGrid.getProject().getProjectId());
			mods	=	aFactory.loadModificationJson(text, refGrid);
			logger.info("Final modification list:");
			for(int i=0;i<mods.size();i++){
				System.out.println(mods.get(i).toString());
			}
			Grid newVersion	=	this.gridModificationService.applyModifications(mods, refGrid, new ArrayList<String>()); //no conflicts
			this.gridService.addGrid(newVersion);
			reader.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "exception";
		}
		return "home";
	}
	
	
	
}
