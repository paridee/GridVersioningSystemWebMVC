package grid;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
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
	 * Struct returned if a modification json is submitted, contains all the changes and new elements to be added
	 * @author Paride Casulli
	 * @author Lorenzo La Banca
	 *
	 */
	
	private static final Logger logger	=	LoggerFactory.getLogger(JSONFactory.class);
	@SuppressWarnings("rawtypes")
	HashMap<Class,HashMap<String,String>> attributesMap	=	new HashMap<Class,HashMap<String,String>>();
	
	
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
		measurementGoalMap.put("decrizione3", "description");
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
	public static Grid loadFromJson(String json,ProjectService projService) throws Exception{
		Grid returnGrid					=	new Grid();	//new Grid to be loaded
		HashMap<String, Object> objects	=	new HashMap<String, Object>();
		JSONArray 	metricList	=	null;
		JSONObject 	obj			=	null;
		try{
			obj					=	new JSONObject(json);
			metricList			=	(JSONArray)obj.get("metricList");
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
			Metric aMetric	=	JSONFactory.loadMetricFromJson(metricList.get(i).toString(), objects);
			metrics.add(aMetric);
			logger.info("Metric loaded "+aMetric.getLabel());
		}
		logger.info("Number of loaded metrics "+metrics.size());
		ArrayList<MeasurementGoal> measGoals	=	new ArrayList<MeasurementGoal>();	//loads measurement goals
		JSONArray measGoalList					=	(JSONArray)obj.get("measGoalList");
		for(int i=0;i<measGoalList.length();i++){
			MeasurementGoal aMG					=	JSONFactory.loadMeasurementGoalFromJson(measGoalList.get(i).toString(), objects);
			measGoals.add(aMG);
		}
		logger.info("Number of Measurement Goal loaded "+measGoals.size());
		JSONArray goalList						=	(JSONArray)obj.get("goalList");
		ArrayList<Goal> goals					=	new ArrayList<Goal>();
		for(int i=0;i<goalList.length();i++){
			goals.add(JSONFactory.loadGoalFromJson(goalList.get(i).toString(),objects));
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
		JSONArray qList							=	(JSONArray) obj.get("questionList");//loads questions
		ArrayList<Question> questionList		=	new ArrayList<Question>();
		for(int i=0;i<qList.length();i++){
			Question aQ							=	JSONFactory.loadQuestionFromJson(qList.get(i).toString(),objects);
			questionList.add(aQ);
		}
		logger.info("MainClass.java questions loaded "+questionList.size());
		JSONArray strategies					=	(JSONArray) obj.get("strategyList");//loads strategies
		ArrayList<Strategy> strategyList		=	new ArrayList<Strategy>();
		for(int i=0;i<strategies.length();i++){
			Strategy aStr						=	JSONFactory.loadStrategyFromJson(strategies.get(i).toString(), objects);
			strategyList.add(aStr);
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
	 * @param refGrid reference grid
	 * @return Modification array
	 * @throws JSONException in case of wrong format
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Modification> loadModificationJson(String json,Grid refGrid) throws JSONException{
		ArrayList<Modification> response	=	new ArrayList<Modification>();
		JSONObject	obj;
		JSONObject	mods;
		obj											=	new JSONObject(json);
		mods										=	(JSONObject)obj.get("modifiche");
		HashMap<String,GridElement> gridElements	=	refGrid.obtainAllEmbeddedElements();
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
									MeasurementGoal aMg	=	loadMeasurementGoalFromJson(currentObj.getString(JSONname), Utils.convertHashMap(gridElements));
									ObjectFieldModification aModification	=	new ObjectFieldModification();
									aModification.setFieldToBeChanged(attrName);
									aModification.setSubjectLabel(objLabel);
									aModification.setNewValue(aMg);
									response.add(aModification);
								}
							}
							else{
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
	private static GridElement loadGridObj(String objectStr,HashMap<String,Object> loaded) {
		JSONObject object	=	new JSONObject(objectStr);
		if(object.has("goalId")){
			return JSONFactory.loadGoalFromJson(objectStr, loaded);
		}
		else if(object.has("mgId")){
			return JSONFactory.loadMeasurementGoalFromJson(objectStr, loaded);
		}
		else if(object.has("strategyId")){
			return JSONFactory.loadStrategyFromJson(objectStr, loaded);
		}
		else if(object.has("metricId")){
			return JSONFactory.loadMetricFromJson(objectStr, loaded);
		}
		else if(object.has("questionId")){
			return JSONFactory.loadMetricFromJson(objectStr, loaded);
		}
		return null;
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
		JSONArray meas	=	(JSONArray) obj.get("availableMeasUnits");
		ArrayList<String>measUnits	=	new ArrayList<String>();
		for(int i=0;i<meas.length();i++){
			measUnits.add(meas.get(i).toString());
		}
		aProject.setAvailableMeasUnits(measUnits);
		loaded.put(projectId, aProject);
		return aProject;
	}

	/**
	 * Load a goal from a given Json
	 * @param json json to be parsed
	 * @param loaded objects already loaded
	 * @return loaded object
	 */
	public static Goal loadGoalFromJson(String json,HashMap<String, Object> loaded){
		JSONObject obj		=	new JSONObject(json);
		String assumption	=	obj.getString("assumption");
		String context		=	obj.getString("context");
		String description	=	obj.getString("descrizione");
		String goalID		=	obj.getString("goalId");
		if(loaded.containsKey(goalID)){	//if already exists return
			logger.info("Goal.java goal "+goalID+" already exists");
			return (Goal)loaded.get(goalID);
		}
		JSONArray strategies=	(JSONArray)obj.get("strategyList");
		logger.info("Goal.java elementi caricati in array: "+strategies.length());
		Goal newGoal		=	new Goal();
		newGoal.setAssumption(assumption);
		newGoal.setContext(context);
		newGoal.setDescription(description);
		newGoal.setLabel(goalID);
		loaded.put(goalID, newGoal);	//added in loaded objects list
		newGoal.setMeasurementGoal(JSONFactory.loadMeasurementGoalFromJson(obj.get("measurementGoal").toString(),loaded));
		ArrayList<Strategy> strategiesIDList	=	new ArrayList<Strategy>();
		for(int i=0;i<strategies.length();i++){
			JSONObject innerObj	=	(JSONObject)strategies.get(i);
			logger.info("\n\n"+innerObj.toString()+"\n\n");
			strategiesIDList.add(JSONFactory.loadStrategyFromJson(innerObj.toString(),loaded));
			logger.info("Goal.java added strategy "+innerObj.getString("strategyId"));
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
	public static Strategy loadStrategyFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String type		=	obj.getString("strategyType");
		String 	description			=	obj.getString("descrizione");
		String	strategyID			=	obj.getString("strategyId");
		String	strategicProjectId	=	obj.get("strategicProjectId").toString();
		if(loaded.containsKey(strategyID)){
			logger.info("Strategy.java strategy "+strategyID+" already exists");
			return (Strategy)loaded.get(strategyID);	//if already loaded returns
		}
		Strategy aStrategy	= new Strategy();
		aStrategy.setDescription(description);
		aStrategy.setLabel(strategyID);
		aStrategy.setStrategyType(type);
		aStrategy.setStrategicProjectId(strategicProjectId);
		loaded.put(strategyID, aStrategy);	//added in loaded objects list
		JSONArray goals	=	(JSONArray)obj.get("goalList");
		ArrayList<Goal> goalList	=	new ArrayList<Goal>();
		for(int i=0;i<goals.length();i++){
			goalList.add(JSONFactory.loadGoalFromJson(goals.get(i).toString(), loaded));
		}
		aStrategy.setGoalList(goalList);
		return aStrategy;
	}

	/**
	 * Load a measurement goal from a given json 
	 * @param string json to be loaded
	 * @param loaded objects already loaded
	 * @return loaded measurement goal
	 */
	public static MeasurementGoal loadMeasurementGoalFromJson(String string, HashMap<String, Object> loaded) {
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
		String description					=	obj.getString("descrizione");
		String interpretationModel			=	obj.getString("interpretationModel");
		JSONArray	questions				=	(JSONArray)obj.get("questionList");
		ArrayList<Question> questionList	=	new ArrayList<Question>();	
		MeasurementGoal temp				=	new MeasurementGoal();
		temp.setDescription(description);
		temp.setInterpretationModel(interpretationModel);
		temp.setLabel(mgId);
		loaded.put(mgId, temp);
		for(int i=0;i<questions.length();i++){
			questionList.add(JSONFactory.loadQuestionFromJson(questions.get(i).toString(),loaded));
		}
		temp.setQuestionList(questionList);
		return temp;
	}

	/**
	 * Loads a question from a JSON
	 * @param string string to be loaded
	 * @param loaded objects already loaded
	 * @return loaded question
	 */
	private static Question loadQuestionFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String qId		=	obj.getString("questionId");
		if(loaded.containsKey(qId)){
			logger.info("Question.java question "+qId+" already loaded");
			return (Question)loaded.get(qId);
		}
		String question			=	obj.getString("question");
		Question aNewQuestion	=	new Question();
		aNewQuestion.setQuestion(question);
		aNewQuestion.setLabel(qId);
		loaded.put(qId, aNewQuestion);
		JSONArray metricList	=	(JSONArray)obj.get("metricList");
		ArrayList<Metric> metrics	=	new ArrayList<Metric>();
		for(int i=0;i<metricList.length();i++){
			logger.info(metricList.get(i).toString());
			metrics.add(JSONFactory.loadMetricFromJson(metricList.get(i).toString(),loaded));
		}
		aNewQuestion.setMetricList(metrics);
		return aNewQuestion;
	}

	/**
	 * Load a metric from a json
	 * @param string json to be parsed
	 * @param loaded objects already loaded
	 * @return loaded metric
	 */
	private static Metric loadMetricFromJson(String string, HashMap<String, Object> loaded) {
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
		aMetric.setCount(obj.getInt("count"));
		aMetric.setDescription(obj.getString("description"));
		JSONArray	measUnits	=	(JSONArray) obj.get("measUnits");
		ArrayList<String>	units	=	new ArrayList<String>();
		for(int i=0;i<measUnits.length();i++){
			units.add(measUnits.getString(i));
		}
		aMetric.setMeasUnits(units);
		aMetric.setMeasurementProcess(obj.getString("measurementProcess"));
		aMetric.setLabel(obj.getString("metricId"));
		aMetric.setMetricType(obj.getString("metricType"));
		aMetric.setScaleType(obj.getString("scaleType"));
		loaded.put(aMetric.getLabel(), aMetric);
		return aMetric;
		
	}
}
