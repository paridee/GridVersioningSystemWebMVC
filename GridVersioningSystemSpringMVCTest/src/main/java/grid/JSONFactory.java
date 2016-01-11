package grid;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.DAOImpl.GridDAOImpl;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
import grid.interfaces.services.ProjectService;

/**
 * This class provides a method for parsing a Grid from JSON Representation 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public class JSONFactory {
	
	private static final Logger logger	=	LoggerFactory.getLogger(GridDAOImpl.class);
	
	/**
	 * This method takes a JSON string as parameter and parses and loads the contents in a Grid entity
	 * @param json grid to be parsed
	 * @return parsed Grid object
	 */
	public static Grid loadFromJson(String json,ProjectService projService){
		Grid returnGrid					=	new Grid();	//new Grid to be loaded
		HashMap<String, Object> objects	=	new HashMap<String, Object>();
		JSONObject obj					=	new JSONObject(json);
		JSONArray metricList			=	(JSONArray)obj.get("metricList");
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
			project								=	JSONFactory.loadProjectFromJson(projectj.toString(), objects);	
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
	
	private static Project loadProjectFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject	obj		=	new JSONObject(string);
		String projectId	=	obj.getString("projectId");
		if(loaded.containsKey(projectId)){
			System.out.println("Project.java Project "+projectId+" already exists");
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

	public static Goal loadGoalFromJson(String json,HashMap<String, Object> loaded){
		JSONObject obj		=	new JSONObject(json);
		String assumption	=	obj.getString("assumption");
		String context		=	obj.getString("context");
		String description	=	obj.getString("descrizione");
		String goalID		=	obj.getString("goalId");
		if(loaded.containsKey(goalID)){	//if already exists return
			System.out.println("Goal.java goal "+goalID+" already exists");
			return (Goal)loaded.get(goalID);
		}
		JSONArray strategies=	(JSONArray)obj.get("strategyList");
		System.out.println("Goal.java elementi caricati in array: "+strategies.length());
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
			System.out.println("\n\n"+innerObj.toString()+"\n\n");
			strategiesIDList.add(JSONFactory.loadStrategyFromJson(innerObj.toString(),loaded));
			System.out.println("Goal.java added strategy "+innerObj.getString("strategyId"));
		}
		newGoal.setStrategyList(strategiesIDList);
		return newGoal;
	}

	public static Strategy loadStrategyFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String type		=	obj.getString("strategyType");
		boolean terminal=	false;
		if(type.equals("TERMINAL")){
			terminal	=	true;
		}
		String 	description			=	obj.getString("descrizione");
		String	strategyID			=	obj.getString("strategyId");
		String	strategicProjectId	=	obj.get("strategicProjectId").toString();
		if(loaded.containsKey(strategyID)){
			System.out.println("Strategy.java strategy "+strategyID+" already exists");
			return (Strategy)loaded.get(strategyID);	//if already loaded returns
		}
		Strategy aStrategy	= new Strategy();
		aStrategy.setDescription(description);
		aStrategy.setLabel(strategyID);
		aStrategy.setIsTerminal(terminal);
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

	public static MeasurementGoal loadMeasurementGoalFromJson(String string, HashMap<String, Object> loaded) {
		String first	=	string.substring(0,1);
		if(!first.equals("{")){
			if(loaded.containsKey(string)){
				System.out.println("MeasurementGoal.java MeasurementGoal "+string+" already exists");
				return (MeasurementGoal)loaded.get(string);
			}
			else throw new JSONException("MeasurementGoal.java object not found or wrong format String "+string+" found "+loaded.containsKey(string));
		}
		JSONObject obj	=	new JSONObject(string);
		String mgId		=	obj.getString("mgId");
		if(loaded.containsKey(mgId)){
			System.out.println("MeasurementGoal.java MeasurementGoal "+mgId+" already exists");
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

	private static Question loadQuestionFromJson(String string, HashMap<String, Object> loaded) {
		JSONObject obj	=	new JSONObject(string);
		String qId		=	obj.getString("questionId");
		if(loaded.containsKey(qId)){
			System.out.println("Question.java question "+qId+" already loaded");
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
			System.out.println(metricList.get(i).toString());
			metrics.add(JSONFactory.loadMetricFromJson(metricList.get(i).toString(),loaded));
		}
		aNewQuestion.setMetricList(metrics);
		return aNewQuestion;
	}

	private static Metric loadMetricFromJson(String string, HashMap<String, Object> loaded) {
		String first	=	string.substring(0,1);
		if(!first.equals("{")){	//gestisco formato a cazzo JSON
			if(loaded.containsKey(string)){
				System.out.println("Metric.java Metric "+string+" already exists");
				return (Metric)loaded.get(string);
			}
			else throw new JSONException("Metric.java object not found or wrong format String "+string+" found "+loaded.containsKey(string));
		}
		JSONObject obj	=	new JSONObject(string);
		String metricId	=	obj.getString("metricId");
		if(loaded.containsKey(metricId)){
			System.out.println("Metric.java Metric "+metricId+" already exists");
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
