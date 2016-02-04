package it.paridelorenzo.ISSSR;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import grid.JSONFactory;
import grid.JSONFactory.JSONType;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;

@Controller
public class TestController {
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

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value = "/step1", method = RequestMethod.GET)
	public String homeGri(Locale locale, Model model) {
		Project first	=	new Project();
		first.setDescription("primo");
		first.setProjectId("first");
		Goal aGoal	=	new Goal();
		aGoal.setLabel("g1");
		aGoal.setDescription("goal...");
		MeasurementGoal mg1	=	new MeasurementGoal();
		mg1.setLabel("mg1");
		mg1.setDescription("first measurement goal");
		aGoal.setMeasurementGoal(mg1);
		Goal aGoal2	=	new Goal();
		aGoal2.setLabel("g2");
		aGoal2.setDescription("second");
		MeasurementGoal mg2	=	new MeasurementGoal();
		mg2.setLabel("mg2");
		mg2.setDescription("a measurement goal");
		aGoal2.setMeasurementGoal(mg2);
		ArrayList<Goal> goals	=	new ArrayList<Goal>();
		goals.add(aGoal);
		goals.add(aGoal2);
		Grid newGrid	=	new Grid();
		newGrid.setMainGoals(goals);
		newGrid.setProject(first);
		Grid refVer	=	newGrid;
		this.gridService.addGrid(newGrid);
		newGrid	= newGrid.clone();//this.gridService.upgradeGrid(newGrid);
		newGrid.setVersion(newGrid.getVersion()+1);
		Strategy s1	=	new Strategy();
		s1.setLabel("s1");
		s1.setDescription("a strategy");
		Strategy s2	=	new Strategy();
		s2.setLabel("s2");
		s2.setDescription("another strategy");
		ArrayList<Strategy> strs= new ArrayList<Strategy>();
		strs.add(s1);
		strs.add(s2);
		aGoal2	=	(Goal)this.gridElementService.upgradeGridElement(aGoal2);
		this.gridService.updateGridElement(newGrid, aGoal2, false, false);
		aGoal2.setStrategyList(strs);
		MeasurementGoal another	=	new MeasurementGoal();
		another.setLabel("mg4");
		another.setDescription("CAMBIATO bubu");
		aGoal2.setMeasurementGoal(another);
		JSONFactory aFactory	=	new JSONFactory();
		this.logger.info("current grid version "+newGrid.getVersion()+" id "+newGrid.getId());
		//this.gridService.addGrid(newGrid);
		Goal terzo	=	new Goal();
		terzo.setLabel("g4556");
		terzo.setDescription("terzo incomodo");
		List <Goal> maingoals	=	newGrid.getMainGoals();
		maingoals.add(terzo);
		newGrid.setMainGoals(maingoals);
		this.gridService.addGrid(newGrid);
		newGrid	=	newGrid.clone();
		newGrid.setVersion(newGrid.getVersion()+1);
		Goal quarto	=	new Goal();
		quarto.setLabel("g093");
		quarto.setDescription("ancora un altro");
		newGrid.getMainGoals().add(quarto);
		s2	=	(Strategy)this.gridElementService.upgradeGridElement(s2);
		newGrid	=	this.gridService.updateGridElement(newGrid, s2, false, false);
		ArrayList<Goal> s2goals	=	new ArrayList<Goal>();
		s2goals.add(quarto);
		s2.setGoalList(s2goals);
		System.out.print(aFactory.obtainJson(newGrid, JSONType.FIRST,refVer));
		
		
		return "home";
	}
	
     
	//TODO remove test
	@RequestMapping(value = "/testGrid", method = RequestMethod.GET)
	public String homeGrid(Locale locale, Model model) {
			logger.info("Welcome home! The client locale is {}.", locale);
			
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			JSONFactory testFactory	=	new JSONFactory();
			Project test	=	new Project();
			test.setDescription("progetto di prova");
			Grid testGrid	=	new Grid();
			testGrid.setProject(test);
			ArrayList<Goal> maingoals	=	new ArrayList<Goal>();
			testGrid.setMainGoals(maingoals);
			MeasurementGoal turboFregna	=	new MeasurementGoal();
			turboFregna.setLabel("porco dio");
			turboFregna.setDescription("e mannaggia la madonna");
			Goal firstGoal	=	new Goal();
			firstGoal.setMeasurementGoal(turboFregna);
			firstGoal.setLabel("primo goal");
			this.gridElementService.addGridElement(firstGoal);
			maingoals.add(firstGoal);
			Strategy aStrategy	=	new Strategy();
			aStrategy.setLabel("str1");
			aStrategy.setDescription("strategia di test");
			ArrayList<Strategy> strategies	=	new ArrayList<Strategy>();
			strategies.add(aStrategy);
			firstGoal.setStrategyList(strategies);
			ArrayList<Goal> goals1	=	new ArrayList<Goal>();
			Goal secondGoal	=	new Goal();
			secondGoal.setLabel("secGoal");
			secondGoal.setDescription("pippo");
			goals1.add(secondGoal);
			aStrategy.setGoalList(goals1);
			MeasurementGoal mg	=	new MeasurementGoal();
			mg.setLabel("measurementGoal pippo");
			mg.setDescription("test");
			secondGoal.setMeasurementGoal(mg);
			Strategy anotherStr	=	new Strategy();
			anotherStr.setLabel("another");
			ArrayList<Strategy> strategiesb	=	new ArrayList<Strategy>();
			strategiesb.add(anotherStr);
			secondGoal.setStrategyList(strategiesb);
			Metric metricatest	=	new Metric();
			metricatest.setLabel("metricatest1");
			metricatest.setDescription("metrica di test");
			Question testQuestion	=	new Question();
			testQuestion.setLabel("aQuestion");
			testQuestion.setQuestion("Stocazzo???");
			ArrayList<Question> questionList	=	new ArrayList<Question>();
			questionList.add(testQuestion);
			mg.setQuestionList(questionList);
			ArrayList<Metric> metrics	=	new ArrayList<Metric>();
			metrics.add(metricatest);
			testQuestion.setMetricList(metrics);
			Metric updated	=	(Metric) this.gridElementService.upgradeGridElement(metricatest);
			updated.setDescription("AGGIORNATAAAAAAAAA");
			this.gridService.addGrid(testGrid);
			Grid newGrid	=	this.gridService.updateGridElement(testGrid, updated,false,true);
			Goal anotherGoal	=	new Goal();
			anotherGoal.setLabel("testttt");
			Practitioner zio	=	new Practitioner();
			zio.setEmail("porcodio@porcamadonna.com");
			zio.setName("Dio Cane");
			ArrayList<Practitioner> auths	=	new ArrayList<Practitioner>();
			auths.add(zio);
			anotherGoal.setAuthors(auths);
			anotherGoal.setMeasurementGoal(turboFregna);
			Grid newRel	=	this.gridService.upgradeGrid(newGrid);
			newRel.getMainGoals().add(anotherGoal);
			this.gridService.updateGrid(newRel);
			anotherGoal	=	(Goal)this.gridElementService.upgradeGridElement(anotherGoal);
			anotherGoal.setAssumption("ASSUNZIONEEEEEEEEE");
			newRel	=	this.gridService.updateGridElement(newRel, anotherGoal,false,true);
			ArrayList<Strategy> strlista	=	new ArrayList<Strategy>();
			strlista.add((Strategy)newRel.obtainAllEmbeddedElements().get(aStrategy.getLabel()));
			anotherGoal	=	(Goal) this.gridElementService.upgradeGridElement(anotherGoal);
			anotherGoal.setStrategyList(strlista);
			newRel	=	this.gridService.updateGridElement(newRel, anotherGoal,false,true);
			this.gridService.addGrid(newRel);
			System.out.println("TEST JSON");
			System.out.println(testFactory.obtainJson(firstGoal, JSONType.FIRST));
			System.out.println("TEST JSON GRID");
			System.out.println(testFactory.obtainJson(newRel, JSONType.FIRST,testGrid));
			System.out.println("TEST JSON GRID ref "+testGrid);
			return "home";
	}
	
}
