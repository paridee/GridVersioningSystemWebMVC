package grid;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
import grid.entities.UserRole;
import grid.interfaces.services.PractitionerService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectFieldModification;
import grid.modification.elements.ObjectModificationService;
import grid.modification.grid.GridElementAdd;

/**
 * This class provides a method for parsing a Grid from JSON Representation 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public class JSONFactory {
	
	/**
	 * Enumeration with all the JSON format supported by this project
	 * @author Paride Casulli
	 * @author Lorenzo La Banca
	 *
	 */
	public enum JSONType{
		FIRST,SECOND
	}
	private PractitionerService practitionerService;
	private static final Logger logger	=	LoggerFactory.getLogger(JSONFactory.class);
	@SuppressWarnings("rawtypes")
	public HashMap<Class,HashMap<String,String>> attributesMap	=	new HashMap<Class,HashMap<String,String>>();
	
	
	@Autowired(required=true)
	@Qualifier(value="practitionerService")
	public void setPractitionerService(PractitionerService practitionerService) {
		this.practitionerService = practitionerService;
	}

	/**
	 * Default constructor, needed to initialize some dictionaries
	 */
	public JSONFactory(){
		HashMap<String,String> goalMap	=	new HashMap<String,String>();	//first column json attr, second column my name, only differences
		goalMap.put("goalId", "label");
		goalMap.put("descrizione","description");
		attributesMap.put(Goal.class, goalMap);
		HashMap<String,String> measurementGoalMap	=	new HashMap<String,String>();
		measurementGoalMap.put("mgId", "label");
		measurementGoalMap.put("descrizione", "description");
		attributesMap.put(MeasurementGoal.class, measurementGoalMap);
		HashMap<String,String> metricMap	=	new HashMap<String,String>();
		metricMap.put("metricId", "label");
		attributesMap.put(Metric.class, metricMap);
		HashMap<String,String> questionMap	=	new HashMap<String,String>();
		questionMap.put("questionId", "label");
		attributesMap.put(Question.class, questionMap);
		HashMap<String,String> strategyMap	=	new HashMap<String,String>();
		strategyMap.put("strategyId", "label");
		strategyMap.put("descrizione", "description");
		attributesMap.put(Strategy.class, strategyMap);
	}

	/**
	 * This method takes a JSON string as parameter and parses and loads the contents in a Grid entity
	 * @param json grid to be parsed
	 * @return parsed Grid object
	 * @throws Exception notifies error while loading a JSON
	 */
	public Grid loadFromJson(String json,ProjectService projService) throws Exception{
		Grid returnGrid					=	new Grid();	//new Grid to be loaded
		HashMap<String, Object> objects	=	new HashMap<String, Object>();
		JSONArray 	metricList	=	new JSONArray();
		JSONObject 	obj			=	null;
		System.out.println(json);
		try{
			obj					=	new JSONObject(json);
			if(!obj.has("project")){
				
				return null;
			}
			
			if(obj.has("metricList")){
				metricList			=	(JSONArray)obj.get("metricList");
			}
		}
		catch(Exception e){
			e.printStackTrace();
			JSONObject	errObject	=	new JSONObject();
			errObject.put("errorType", "Error while loading project from the given JSON");
			errObject.put("faultyJson", json);
			throw new Exception(errObject.toString());
		}
		ArrayList<Metric> metrics		=	new ArrayList<Metric>();
		for(int i=0;i<metricList.length();i++){			//loads the Grid metrics, makes it Before other objects optimizing reading
			Metric aMetric	=	loadMetricFromJson(metricList.get(i).toString(), objects);
			metrics.add(aMetric);
			logger.info("Metric loaded "+aMetric.getLabel());
		}
		logger.info("Number of loaded metrics "+metrics.size());
		ArrayList<MeasurementGoal> measGoals	=	new ArrayList<MeasurementGoal>();	//loads measurement goals
		if(obj.has("measGoalList")){
			JSONArray measGoalList					=	(JSONArray)obj.get("measGoalList");
			for(int i=0;i<measGoalList.length();i++){
				MeasurementGoal aMG					=	loadMeasurementGoalFromJson(measGoalList.get(i).toString(), objects);
				measGoals.add(aMG);
			}
		}
		logger.info("Number of Measurement Goal loaded "+measGoals.size());
		ArrayList<Goal> goals					=	new ArrayList<Goal>();
		if(obj.has("goalList")){
			JSONArray goalList						=	(JSONArray)obj.get("goalList");
			for(int i=0;i<goalList.length();i++){
				goals.add(loadGoalFromJson(goalList.get(i).toString(),objects));
			}
		}
		logger.info("MainClass.java goals loaded "+goals.size());
		JSONObject projectj						=	(JSONObject) obj.get("project");	//loads project
		String 	prjlabel						=	projectj.getString("projectId");
		Project project							=	projService.getProjectByProjectId(prjlabel);
		if(project==null){
			try {
				project								=	JSONFactory.loadProjectFromJson(projectj.toString(), objects);
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject	errObject	=	new JSONObject();
				errObject.put("errorType", "Error while loading project from the given JSON");
				errObject.put("faultyJson", projectj.toString());
				throw new Exception(errObject.toString());
			}	
		}
		ArrayList<Question> questionList		=	new ArrayList<Question>();
		if(obj.has("questionList")){
			JSONArray qList							=	(JSONArray) obj.get("questionList");//loads questions
			for(int i=0;i<qList.length();i++){
				Question aQ							=	loadQuestionFromJson(qList.get(i).toString(),objects);
				questionList.add(aQ);
			}
		}
		ArrayList<Strategy> strategyList		=	new ArrayList<Strategy>();
		logger.info("MainClass.java questions loaded "+questionList.size());
		if(obj.has("strategyList")){
			JSONArray strategies					=	(JSONArray) obj.get("strategyList");//loads strategies
			for(int i=0;i<strategies.length();i++){
				Strategy aStr						=	loadStrategyFromJson(strategies.get(i).toString(), objects);
				strategyList.add(aStr);
			}	
		}
		logger.info("MainClass.java strategy loaded "+strategyList.size());
		//sets loaded objects to grid
		returnGrid.setMainGoals(goals);
		returnGrid.setProject(project);
		return returnGrid;
	}
	
	/**
 	 * Returns all the modifications defined in this json
	 * @param json json string to be parsed
	 * @param latestGrid reference grid
	 * @return Modification array
	 * @throws JSONException in case of wrong format
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Modification> loadModificationJson(String json,Grid latestGrid) throws JSONException{
		ArrayList<Modification> response	=	new ArrayList<Modification>();
		JSONObject	obj;
		JSONObject	mods;
		obj											=	new JSONObject(json);
		mods										=	(JSONObject)obj.get("modifiche");
		HashMap<String,GridElement> gridElements	=	latestGrid.obtainAllEmbeddedElements();
		ArrayList<JSONArray>	changesArray		=	new ArrayList<JSONArray>();
		changesArray.add((JSONArray)mods.get("goals"));
		changesArray.add((JSONArray)mods.get("metrics"));
		changesArray.add((JSONArray)mods.get("questions"));
		changesArray.add((JSONArray)mods.get("strategies"));
		for(int t=0;t<changesArray.size();t++){
			JSONArray currentArray	=	changesArray.get(t);
			for(int i=0;i<currentArray.length();i++){
				JSONObject	currentObj					=	currentArray.getJSONObject(i);
				String objLabel="";
				Class  objClass	=	GridElement.class;
				GridElement		oldObj=null;
				if(currentObj.has("goalId")){
					objLabel	=	currentObj.getString("goalId");
					oldObj		=	gridElements.get(objLabel);
					objClass	=	Goal.class;
					if(oldObj!=null){
						if(oldObj.getClass().isInstance(Goal.class)){
							throw new JSONException("class mismatch Goal - "+oldObj.getClass());
						}
					}
				}
				else if(currentObj.has("mgId")){
					objLabel	=	currentObj.getString("mgId");
					oldObj		=	gridElements.get(objLabel);
					objClass	=	MeasurementGoal.class;
					if(oldObj!=null){
						if(oldObj.getClass().isInstance(MeasurementGoal.class)){
							throw new JSONException("class mismatch MeasurementGoal - "+oldObj.getClass());
						}
					}
				}
				else if(currentObj.has("strategyId")){
					objLabel	=	currentObj.getString("strategyId");
					oldObj		=	gridElements.get(objLabel);
					objClass	=	Strategy.class;
					if(oldObj!=null){
						if(oldObj.getClass().isInstance(Strategy.class)){
							throw new JSONException("class mismatch MeasurementGoal - "+oldObj.getClass());
						}
					}
				}
				else if(currentObj.has("metricId")){
					objLabel	=	currentObj.getString("metricId");
					oldObj		=	gridElements.get(objLabel);
					objClass	=	Metric.class;
					if(oldObj!=null){
						if(oldObj.getClass().isInstance(Metric.class)){
							throw new JSONException("class mismatch MeasurementGoal - "+oldObj.getClass());
						}
					}
				}
				else if(currentObj.has("questionId")){
					objLabel	=	currentObj.get("questionId")+"";
					oldObj	=	gridElements.get(objLabel);
					objClass	=	Question.class;
					if(oldObj!=null){
						if(oldObj.getClass().isInstance(Question.class)){
							throw new JSONException("class mismatch MeasurementGoal - "+oldObj.getClass());
						}
					}
				}
				if(oldObj	==	null){
					Constructor cons	=	null;
					GridElement gEl		=	null;
					try {
						cons = objClass.getConstructor();
						gEl		=	(GridElement) cons.newInstance();
					} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					gEl.setLabel(objLabel);
					logger.info(" created new object type "+objClass+" label "+objLabel);
					oldObj	=	gEl;
					GridElementAdd	add	=	new GridElementAdd();
					add.setAppendedObjectLabel(objLabel);
					add.setGridElementAdded(gEl);
					response.add(add);
				}
				logger.info("old element label "+objLabel);
				Iterator<String> anIterator	=	currentObj.keys();
				while(anIterator.hasNext()){
					String attrName			=	(String) anIterator.next();
					String JSONname			=	attrName;
					logger.info("old element class "+objClass);
					HashMap<String,String> attrNameMap	=	this.attributesMap.get(objClass);
					if(attrNameMap.containsKey(attrName)){
						attrName			=	attrNameMap.get(attrName);
					}
					if(attrName!="label"){
						try{
							Field aField		=	objClass.getDeclaredField(attrName);
							aField.setAccessible(true);
							Object value	=	aField.get(oldObj);
							if(List.class.isAssignableFrom(aField.getType())){
								ArrayList<GridElement> 	newElements	=	new ArrayList<GridElement>();
								JSONArray	anArray					=	currentObj.getJSONArray(JSONname);
								for(int j=0;j<anArray.length();j++){
									GridElement anElement			=	loadGridObj(anArray.getString(j),Utils.convertHashMap(gridElements));
									newElements.add(anElement);
								}
								logger.info("list value "+value+" on field "+aField.getName()+" on object "+objLabel);
								response.addAll(ObjectModificationService.getListModification((List)value, newElements, aField.getName(), objLabel));
							}
							else if(GridElement.class.isAssignableFrom(aField.getType())){
								if(attrName.equals("measurementGoal")){
									//logger.info("measurement goal id "+currentObj.get(JSONname).toString());
									
									//label of measurement goal has changed... 
									if(!((Goal)oldObj).getMeasurementGoal().getLabel().equals(currentObj.get(JSONname).toString())){
										MeasurementGoal aMg	=	(MeasurementGoal)gridElements.get(currentObj.get(JSONname).toString());
										if(aMg!=null){
											logger.info("cannot change reference to a non-existing object");
											ObjectFieldModification aModification	=	new ObjectFieldModification();
											aModification.setFieldToBeChanged(attrName);
											aModification.setSubjectLabel(objLabel);
											aModification.setNewValue(aMg);
											response.add(aModification);
										}
									}
								}
							}
							else{
								if(gridElements.containsKey(objLabel)){
									logger.info("modification value "+currentObj.get(JSONname)+" "+aField.getName()+" "+aField.getType().isAssignableFrom(GridElement.class)+" "+aField.getType()+GridElement.class.isAssignableFrom(aField.getType()));
									if(!(value.equals(currentObj.get(JSONname)))){
										ObjectFieldModification aModification	=	new ObjectFieldModification();
										aModification.setFieldToBeChanged(attrName);
										aModification.setSubjectLabel(objLabel);
										aModification.setNewValue(currentObj.get(JSONname));
										response.add(aModification);
									}
								}
							}
						}
						catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		return response;
	}
	
	/**
	 * Switch function, returns an of a proper type from a JSON
	 * @param objectStr JSON string
	 * @param loaded objects already loaded
	 * @return proper object
	 */
	public GridElement loadGridObj(String objectStr,HashMap<String,Object> loaded) {
		JSONObject object	=	new JSONObject(objectStr);
		GridElement loadedGE	=	null;
		if(object.has("goalId")){
			loadedGE	=	 loadGoalFromJson(objectStr, loaded);
		}
		else if(object.has("mgId")){
			loadedGE	=	 loadMeasurementGoalFromJson(objectStr, loaded);
		}
		else if(object.has("strategyId")){
			loadedGE	=	 loadStrategyFromJson(objectStr, loaded);
		}
		else if(object.has("metricId")){
			loadedGE	=	 loadMetricFromJson(objectStr, loaded);
		}
		else if(object.has("questionId")){
			loadedGE	=	 loadQuestionFromJson(objectStr, loaded);
		}
		return loadedGE;
	}

	/**
	 * loads authors
	 * @param object with authors
	 * @return authors
	 */
	private ArrayList<Practitioner> loadAuthors(JSONArray authors) {
		ArrayList<Practitioner> practitioners	=	new ArrayList<Practitioner>();
		for(int i=0;i<authors.length();i++){
			logger.info(authors.get(i).getClass()+" "+authors.get(i).toString());
			JSONObject	thisObj	=	((JSONObject)authors.get(i));
			String email		=	thisObj.getString("email");
			logger.info(thisObj.getClass()+" "+thisObj.toString()+" "+email);
			//String email		=	((JSONObject)authors.get(i)).getString("email");
			Practitioner aPract	=	practitionerService.getPractitionerByEmail(email);
			if(aPract==null){
				logger.info("author not found in DB, creating a new practitioner "+((JSONObject)authors.get(i)).getString("email"));
				aPract	=	new Practitioner();
				aPract.setEmail(((JSONObject)authors.get(i)).getString("email"));
				aPract.setName(((JSONObject)authors.get(i)).getString("name"));
				aPract.setPassword("$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y");
				UserRole aUserRole	=	new UserRole();
				aUserRole.setUser(aPract);
				aUserRole.setRole("ROLE_USER");
				HashSet<UserRole> roles	=	new HashSet<UserRole>();
				roles.add(aUserRole);
				aPract.setUserRole(roles);
				practitionerService.add(aPract);
			}
			else{
				logger.info("author found in DB "+aPract.getEmail());
			}
			practitioners.add(aPract);
		}
		return practitioners;
	}

	/**
	 * Loads a Project from a JSON representation
	 * @param string json
	 * @param loaded maps with objects already loaded
	 * @return loaded object
	 * @throws Exception in case of error
	 */
	private static Project loadProjectFromJson(String string, HashMap<String, Object> loaded) throws Exception{
		JSONObject	obj		=	new JSONObject(string);
		String projectId	=	obj.getString("projectId");
		if(loaded.containsKey(projectId)){
			logger.info("Project.java Project "+projectId+" already exists");
			return (Project)loaded.get(projectId);
		}
		Project aProject	=	new Project();
		loaded.put(projectId, aProject);
		aProject.setCreationDate(obj.getString("creationDate"));
		aProject.setProjectId(projectId);
		aProject.setDescription(obj.getString("description"));
		if(obj.has("availableMeasUnits")){
			JSONArray meas	=	(JSONArray) obj.get("availableMeasUnits");
			ArrayList<String>measUnits	=	new ArrayList<String>();
			for(int i=0;i<meas.length();i++){
				measUnits.add(meas.get(i).toString());
			}
			aProject.setAvailableMeasUnits(measUnits);
		}
		loaded.put(projectId, aProject);
		return aProject;
	}

	/**
	 * Load a goal from a given Json
	 * @param json json to be parsed
	 * @param loaded objects already loaded
	 * @return loaded object
	 */
	public Goal loadGoalFromJson(String json,HashMap<String, Object> loaded){
		JSONObject obj		=	new JSONObject(json);
		String assumption	=	"";
		if(obj.has("assumptions")){
			assumption	=	obj.getString("assumption");
		}
		String context		=	"";
		if(obj.has("context")){
			context	=	obj.getString("context");
		}
		String description	=	"";
		if(obj.has("descrizione")){
			description	=	obj.getString("descrizione");
		}
		String goalID		=	obj.getString("goalId");
		if(loaded.containsKey(goalID)){	//if already exists return
			logger.info("Goal.java goal "+goalID+" already exists");
			return (Goal)loaded.get(goalID);
		}
		ArrayList<Strategy> strategiesIDList	=	new ArrayList<Strategy>();
		if(obj.has("strategyList")){
			JSONArray strategies=	(JSONArray)obj.get("strategyList");
			for(int i=0;i<strategies.length();i++){
				JSONObject innerObj	=	(JSONObject)strategies.get(i);
				logger.info("\n\n"+innerObj.toString()+"\n\n");
				strategiesIDList.add(loadStrategyFromJson(innerObj.toString(),loaded));
				logger.info("Goal.java added strategy "+innerObj.getString("strategyId"));
			}
			logger.info("Goal.java elementi caricati in array: "+strategies.length());
		}
		Goal newGoal		=	new Goal();
		if(obj.has("authors")){
			newGoal.setAuthors(loadAuthors((JSONArray)(obj.get("authors"))));
		}
		newGoal.setAssumption(assumption);
		newGoal.setContext(context);
		newGoal.setDescription(description);
		newGoal.setLabel(goalID);
		loaded.put(goalID, newGoal);	//added in loaded objects list
		if(obj.has("measurementGoal")){
			newGoal.setMeasurementGoal(loadMeasurementGoalFromJson(obj.get("measurementGoal").toString(),loaded));
		}
		newGoal.setStrategyList(strategiesIDList);
		return newGoal;
	}

	/**
	 * Load a strategy from a given JSON
	 * @param string json string
	 * @param loaded loaded objects
	 * @return loaded strategy
	 */
	public Strategy loadStrategyFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String type	=	"";
		if(obj.has("strategyType")){
			type	=	obj.getString("strategyType");
		}
		String 	description	=	"";
		if(obj.has("descrizione")){
			description	=		obj.getString("descrizione");
		}
		String	strategyID			=	obj.getString("strategyId");
		String	strategicProjectId	=	"";
		if(obj.has("strategicProjectId")){
			strategicProjectId	=	obj.get("strategicProjectId").toString();
		}
		if(loaded.containsKey(strategyID)){
			logger.info("Strategy.java strategy "+strategyID+" already exists");
			return (Strategy)loaded.get(strategyID);	//if already loaded returns
		}
		Strategy aStrategy	= new Strategy();
		if(obj.has("authors")){
			aStrategy.setAuthors(loadAuthors((JSONArray)(obj.get("authors"))));
		}
		aStrategy.setDescription(description);
		aStrategy.setLabel(strategyID);
		aStrategy.setStrategyType(type);
		aStrategy.setStrategicProjectId(strategicProjectId);
		loaded.put(strategyID, aStrategy);	//added in loaded objects list
		if(obj.has("goalList")){
			JSONArray goals	=	(JSONArray)obj.get("goalList");
			ArrayList<Goal> goalList	=	new ArrayList<Goal>();
			for(int i=0;i<goals.length();i++){
				goalList.add(loadGoalFromJson(goals.get(i).toString(), loaded));
			}
			aStrategy.setGoalList(goalList);
		}
		return aStrategy;
	}

	/**
	 * Load a measurement goal from a given json 
	 * @param string json to be loaded
	 * @param loaded objects already loaded
	 * @return loaded measurement goal
	 */
	public MeasurementGoal loadMeasurementGoalFromJson(String string, HashMap<String, Object> loaded) {
		String first	=	string.substring(0,1);
		if(!first.equals("{")){
			if(loaded.containsKey(string)){
				logger.info("MeasurementGoal.java MeasurementGoal "+string+" already exists");
				return (MeasurementGoal)loaded.get(string);
			}
			else throw new JSONException("MeasurementGoal.java object not found or wrong format String "+string+" found "+loaded.containsKey(string));
		}
		JSONObject obj	=	new JSONObject(string);
		String mgId		=	obj.getString("mgId");
		if(loaded.containsKey(mgId)){
			logger.info("MeasurementGoal.java MeasurementGoal "+mgId+" already exists");
			return (MeasurementGoal)loaded.get(mgId);
		}
		String description	=	"";
		if(obj.has("descrizione")){
			description	=	obj.getString("descrizione");
		}
		String interpretationModel	=	"";		
		if(obj.has("interpretationModel")){
			interpretationModel			=	obj.getString("interpretationModel");
		}
		MeasurementGoal temp				=	new MeasurementGoal();
		if(obj.has("authors")){
			temp.setAuthors(loadAuthors((JSONArray)(obj.get("authors"))));
		}
		if(obj.has("questionList")){
			JSONArray	questions				=	(JSONArray)obj.get("questionList");
			ArrayList<Question> questionList	=	new ArrayList<Question>();	
			for(int i=0;i<questions.length();i++){
				questionList.add(loadQuestionFromJson(questions.get(i).toString(),loaded));
			}
			temp.setQuestionList(questionList);
		}
		temp.setDescription(description);
		temp.setInterpretationModel(interpretationModel);
		temp.setLabel(mgId);
		loaded.put(mgId, temp);
		return temp;
	}

	/**
	 * Loads a question from a JSON
	 * @param string string to be loaded
	 * @param loaded objects already loaded
	 * @return loaded question
	 */
	private Question loadQuestionFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String qId		=	obj.getString("questionId");
		if(loaded.containsKey(qId)){
			logger.info("Question.java question "+qId+" already loaded");
			return (Question)loaded.get(qId);
		}
		Question aNewQuestion	=	new Question();
		if(obj.has("authors")){
			aNewQuestion.setAuthors(loadAuthors((JSONArray)(obj.get("authors"))));
		}
		if(obj.has("question")){
			String question			=	obj.getString("question");
			aNewQuestion.setQuestion(question);
		}
		aNewQuestion.setLabel(qId);
		loaded.put(qId, aNewQuestion);
		if(obj.has("metricList")){
			JSONArray metricList	=	(JSONArray)obj.get("metricList");
			ArrayList<Metric> metrics	=	new ArrayList<Metric>();
			for(int i=0;i<metricList.length();i++){
				logger.info(metricList.get(i).toString());
				metrics.add(loadMetricFromJson(metricList.get(i).toString(),loaded));
			}
			aNewQuestion.setMetricList(metrics);
		}
		return aNewQuestion;
	}

	/**
	 * Load a metric from a json
	 * @param string json to be parsed
	 * @param loaded objects already loaded
	 * @return loaded metric
	 */
	private Metric loadMetricFromJson(String string, HashMap<String, Object> loaded) {
		String first	=	string.substring(0,1);
		if(!first.equals("{")){	//gestisco formato a non standard JSON
			if(loaded.containsKey(string)){
				logger.info("Metric.java Metric "+string+" already exists");
				return (Metric)loaded.get(string);
			}
			else throw new JSONException("Metric.java object not found or wrong format String "+string+" found "+loaded.containsKey(string));
		}
		JSONObject obj	=	new JSONObject(string);
		String metricId	=	obj.getString("metricId");
		if(loaded.containsKey(metricId)){
			logger.info("Metric.java Metric "+metricId+" already exists");
			return (Metric)loaded.get(metricId);
		}
		Metric aMetric			=	new Metric();
		if(obj.has("authors")){
			aMetric.setAuthors(loadAuthors((JSONArray)(obj.get("authors"))));
		}
		if(obj.has("count")){
			aMetric.setCount(obj.getInt("count"));
		}
		if(obj.has("description")){
			aMetric.setDescription(obj.getString("description"));
		}
		if(obj.has("measUnits")){
			JSONArray	measUnits	=	(JSONArray) obj.get("measUnits");
			ArrayList<String>	units	=	new ArrayList<String>();
			for(int i=0;i<measUnits.length();i++){
				units.add(measUnits.getString(i));
			}
			aMetric.setMeasUnits(units);	
		}
		if(obj.has("measurementProcess")){
			aMetric.setMeasurementProcess(obj.getString("measurementProcess"));
		}
		aMetric.setLabel(obj.getString("metricId"));
		if(obj.has("metricType")){
			aMetric.setMetricType(obj.getString("metricType"));
		}
		if(obj.has("scaleType")){
			aMetric.setScaleType(obj.getString("scaleType"));
		}
		loaded.put(aMetric.getLabel(), aMetric);
		return aMetric;
	}
	
	/**
	 * Obtain a JSON from an element
	 * @param element element to be serialized
	 * @param type type of JSON
	 * @return JSON
	 */
	public JSONObject obtainJson(GridElement element,JSONType type){
		return obtainJson(element,type,false);
	}
	
	/**
	 * Obtain a JSON from an element
	 * @param element element to be serialized
	 * @param type type of JSON
	 * @param extended if true show also element id and creation timestamp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject obtainJson(GridElement element,JSONType type,boolean extended){
		JSONObject	returnObject	=	new JSONObject();
		String		id				=	element.getLabel();
		String		typeC			=	element.getClass().getSimpleName().toString();
		typeC						=	typeC.toLowerCase();
		HashMap<Class,HashMap<String,String>> reverseMap	=	new HashMap<Class, HashMap<String, String>>();
		Iterator<Class> anIterator			=	this.attributesMap.keySet().iterator();
		while(anIterator.hasNext()){
			Class aClass							=	anIterator.next();
			HashMap<String,String> innerMap			=	this.attributesMap.get(aClass);
			HashMap<String,String> reverseInnerMap	=	new HashMap<String,String>();
			Iterator<String> innerIterator			=	innerMap.keySet().iterator();
			while(innerIterator.hasNext()){
				String value	=	innerIterator.next();
				String key		=	innerMap.get(value);
				reverseInnerMap.put(key, value);
				logger.info("map of class "+aClass.getCanonicalName()+" original "+value+" value "+key+" reverse "+key+" value "+value);
			}
			reverseMap.put(aClass, reverseInnerMap);
		}
		//id label of measurement goal has a different format
		if(typeC.equals("measurementgoal")){
			typeC	=	"mg";
		}
		typeC						=	typeC+"Id";
		returnObject.put(typeC, id);
		//gets the fields list of the elemen
		Field[]	fields	=	element.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			//System.out.println("FIELD "+fields[i].getName());
			String 	fieldName	=	fields[i].getName();
			fields[i].setAccessible(true);
			Object fieldValue	=	null;
			try {
				if(fields[i].get(element)!=null){
					fieldValue = fields[i].get(element);	
					if(reverseMap.containsKey(fields[i].getDeclaringClass())){
						HashMap <String,String> fieldMap	=	reverseMap.get(fields[i].getDeclaringClass());
						if(fieldMap.containsKey(fieldName)){
							fieldName	=	fieldMap.get(fieldName);
						}
					}
					//if the attrubute is a list select a behaviour for creating a JSON
					if(fieldValue instanceof List){
						List list	=	(List) fieldValue;
						JSONArray	array	=	new JSONArray();
						for(int j=0;j<list.size();j++){
							Object listElement	=	list.get(j);
							if(listElement instanceof Metric){
								if(type	==	JSONType.FIRST){
									array.put(this.obtainJson((GridElement)list.get(j), type,extended));
								}
								else if(type	==	JSONType.SECOND){
									array.put(((GridElement)list.get(j)).getIdElement());
								}
							}
							else if(listElement instanceof GridElement){
								array.put(this.obtainJson((GridElement)list.get(j), type,extended));
							}
							else{
								array.put(list.get(j));
							}
							returnObject.put(fieldName, array);
						}
					}
					else{
						Object innerObj	=	null;
						if(fieldValue instanceof MeasurementGoal){
							if(type	==	JSONType.FIRST){
								innerObj	=	((MeasurementGoal)fieldValue).getLabel();	
							}
							else if(type	==	JSONType.SECOND){
								innerObj	=	this.obtainJson(((MeasurementGoal)fieldValue),type,extended);
							}
						}
						else if(fieldValue instanceof GridElement){
							innerObj	=	this.obtainJson((GridElement)fieldValue, type,extended);
						}
						else{
							innerObj	=	fieldValue;
						}
						if(!fieldName.equals("logger")){
							returnObject.put(fieldName, innerObj);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		List<Practitioner> authors	=	element.getAuthors();
		JSONArray auth				=	new JSONArray();
		for(int i=0;i<authors.size();i++){
			JSONObject author		=	new JSONObject();
			author.put("email", authors.get(i).getEmail());
			author.put("name", authors.get(i).getName());
			auth.put(author);
		}
		if(auth.length()>0){
			returnObject.put("authors", auth);
		}
		if(extended==true){
			returnObject.put("dbId", element.getIdElement());
			returnObject.put("timestamp", element.getTimestamp());
		}
		return returnObject;
	}
	
	/**
	 * Obtain a JSON from a grid
	 * @param aGrid grid to be serialized
	 * @param type type of JSON
	 * @param refGrid (optional)
	 * @return JSON
	 */
	public JSONObject obtainJson(Grid aGrid,JSONType type,Grid refGrid){
		return obtainJson(aGrid,type,refGrid,false);
	}
	
	/**
	 * Obtain a JSON from a grid
	 * @param aGrid grid to be serialized
	 * @param type type of JSON
	 * @param refGrid (optional)
	 * @param extended tell if include timestamps and id
	 * @return JSON
	 */
	public JSONObject obtainJson(Grid aGrid,JSONType type,Grid refGrid,boolean extended){
		JSONObject 	returnObject	=	new JSONObject();
		List<Goal> 	mainGoal		=	aGrid.getMainGoals();
		JSONArray	mainGoalArray	=	new JSONArray();
		for(int i=0;i<mainGoal.size();i++){
			mainGoalArray.put(this.obtainJson(mainGoal.get(i), type,extended));
		}
		returnObject.put("goalList", mainGoalArray);
		HashMap<String,GridElement> elements	=	aGrid.obtainAllEmbeddedElements();
		JSONArray	measGoalList				=	new JSONArray();
		JSONArray	metricList					=	new JSONArray();
		JSONArray	questionList				=	new JSONArray();
		JSONArray	strategyList				=	new JSONArray();
		JSONObject	project						=	new JSONObject();
		//project JSON creation
		project.put("creationDate", aGrid.getProject().getCreationDate());
		project.put("description", aGrid.getProject().getDescription());
		project.put("projectId", aGrid.getProject().getProjectId());
		//end of creation
		Iterator<String> anIterator				=	elements.keySet().iterator();
		while(anIterator.hasNext()){
			String key					=	anIterator.next();
			GridElement	anEl			=	elements.get(key);
			JSONArray destinationArray	=	null;
			if(anEl instanceof MeasurementGoal){
				destinationArray	=	measGoalList;
			}
			else if(anEl instanceof Metric){
				destinationArray	=	metricList;
			}
			else if(anEl instanceof Question){
				destinationArray	=	questionList;
			}
			else if(anEl instanceof Strategy){
				destinationArray	=	strategyList;
			}
			if(destinationArray!=null){//may have goals, with null array
				JSONObject anObj	=	this.obtainJson(anEl, type, extended);
				destinationArray.put(anObj);
			}
		}
		returnObject.put("measGoalList", measGoalList);
		returnObject.put("metricList", metricList);
		returnObject.put("project", project);
		returnObject.put("questionList", questionList);
		returnObject.put("strategyList", strategyList);
		logger.info("reference grid "+refGrid);
		if(extended==true){
			returnObject.put("timestamp", aGrid.getTimestamp());
			returnObject.put("dbId", aGrid.getId());
		}
		if (refGrid!=null){
			logger.info("exists reference grid "+refGrid);
			JSONObject shell	=	new JSONObject();
			shell.put("refVersion", refGrid.getVersion());
			shell.put("newGrid", returnObject);
			returnObject	=	shell;
		}
		return returnObject;
	}

}
