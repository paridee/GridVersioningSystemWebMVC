package it.paridelorenzo.ISSSR;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import grid.Utils;
import grid.JSONFactory.JSONType;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.PractitionerService;
import grid.interfaces.services.ProjectService;
import grid.modification.grid.GridModificationService;

@Controller
public class TestController {
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ProjectService		projectService;
	private JSONFactory 		jFact;
	private ConflictService		conflictService;
	private PractitionerService practitionerService;
	private GridModificationService gridModificationService;
	
	@Autowired(required=true)
	@Qualifier(value="gridModificationService")
	public void setGridModificationService(GridModificationService gridModificationService) {
		this.gridModificationService = gridModificationService;
	}
	
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
		Practitioner pm	=	new Practitioner();
		pm.setEmail("paride.casulli@gmail.com");
		pm.setName("Paride Casulli");
		Practitioner lorenzo	=	new Practitioner();
		lorenzo.setEmail("lorenzo.labanca@gmail.com");
		lorenzo.setName("Lorenzo La Banca");
		Project first	=	new Project();
		first.setProjectManager(pm);
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
		this.gridService.addGrid(newGrid);/*
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
		newGrid	=	this.gridService.updateGridElement(newGrid, aGoal2, false, false);
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
		ArrayList<Practitioner> authors	=	new ArrayList<Practitioner>();
		authors.add(pm);
		authors.add(lorenzo);ffffffff
		quarto.setAuthors(authors);
		newGrid.getMainGoals().add(quarto);
		s2	=	(Strategy)this.gridElementService.upgradeGridElement(s2);
		newGrid	=	this.gridService.updateGridElement(newGrid, s2, false, false);
		ArrayList<Goal> s2goals	=	new ArrayList<Goal>();
		s2goals.add(quarto);
		s2.setGoalList(s2goals);
		Strategy extra	=	new Strategy();
		extra.setDescription("dovrebbe innescare major pending");
		extra.setLabel("extraStrategy");
		ArrayList<Strategy> strNewList	=	new ArrayList<Strategy>();
		strNewList.add(extra);
		aGoal.setStrategyList(strNewList);
		System.out.print(aFactory.obtainJson(newGrid, JSONType.FIRST,refVer));*/
		//this.gridService.addGrid(newGrid);
		return "home";
	}
	
	@RequestMapping(value = "/testLorenzo2", method = RequestMethod.GET)
	public String homeGrifddfdf(Locale locale, Model model) {
		Grid start	=	this.gridService.getGridById(1);
		Grid original =	start;
		HashMap<String,GridElement> map	=	start.obtainAllEmbeddedElements();
		Metric m4	=	(Metric) map.get("m4");
		m4	=	(Metric) m4.clone();
		m4.setDescription("modificata m4");
		m4.setScaleType("Ordinale");
		start	=	this.gridService.updateGridElement(start, m4, true, true);
		map	=	start.obtainAllEmbeddedElements();
		MeasurementGoal mg2	=	(MeasurementGoal) map.get("mg2");
		mg2	=	(MeasurementGoal) mg2.clone();
		mg2.setInterpretationModel("un modello interpretativo");
		mg2.setDescription("meassssssssssss");
		start	=	this.gridService.updateGridElement(start, mg2, true, true);
		map	=	start.obtainAllEmbeddedElements();
		Strategy s3	=	(Strategy) map.get("s3");
		s3	=	(Strategy) s3.clone();
		s3.setDescription("essetre");
		List<Goal>s3goals	=	s3.getGoalList();
		Goal nuovoGoal	=	new Goal();
		nuovoGoal.setLabel("g4534");
		nuovoGoal.setDescription("Goal rompiscatole");
		s3goals.add(nuovoGoal);
		start	=	this.gridService.updateGridElement(start, s3, true, true);		
		JSONFactory aFactory	=	new JSONFactory();
		this.logger.info("JSON PRODOTTO "+aFactory.obtainJson(start, JSONType.FIRST,null));
		s3.setDescription("this has to be a major conflict");
		mg2.setDescription("this has to be a minor conflict");
		Strategy s2	=	(Strategy) map.get("s2");
		s2.setDescription("this has to be a major update");
		Goal aNewMainGoal	=	new Goal();
		aNewMainGoal.setLabel("g9843");
		aNewMainGoal.setDescription("sono un nuovo main goal");
		start.getMainGoals().add(aNewMainGoal);
		this.logger.info("JSON PRODOTTO "+aFactory.obtainJson(start, JSONType.FIRST,original));
		//this.gridService.addGrid(start);
		
		return "home";
	}
	
	@RequestMapping(value = "/testLorenzo", method = RequestMethod.GET)
	public String homeGridfdf(Locale locale, Model model) {
		Practitioner pm	=	new Practitioner();
		pm.setEmail("paride.casulli@gmail.com");
		pm.setName("Paride Casulli");
		Practitioner lorenzo	=	new Practitioner();
		lorenzo.setEmail("lorenzo.labanca@gmail.com");
		lorenzo.setName("Lorenzo La Banca");
		Project first	=	new Project();
		first.setProjectManager(pm);
		first.setDescription("test1");
		first.setProjectId("prj-test1");
		Goal firstg		=	new Goal();
		firstg.setLabel("goal1");
		firstg.setDescription("primo");
		Grid firstGrid	=	new Grid();
		firstGrid.setProject(first);
		ArrayList<Goal> firstTest	=	new ArrayList<Goal>();
		firstTest.add(firstg);
		firstGrid.setMainGoals(firstTest);
		this.gridService.addGrid(firstGrid);
		/*
		Goal secondg	=	new Goal();
		secondg.setLabel("goal2");
		secondg.setDescription("secondo goal");
		Grid prj1Grid	=	new Grid();
		prj1Grid.setProject(first);
		prj1Grid.setVersion(2);
		ArrayList<Goal> mainGoals	=	new ArrayList<Goal>();
		mainGoals.add(firstg);
		mainGoals.add(secondg);
		prj1Grid.setMainGoals(mainGoals);
		Strategy s1		=	new Strategy();
		Strategy s2		=	new Strategy();
		s1.setLabel("str1");
		s1.setDescription("first strategy");
		s2.setLabel("str2");
		s2.setDescription("second strategy");
		ArrayList<Strategy> g1Str	=	new ArrayList<Strategy>();
		g1Str.add(s1);
		firstg.setStrategyList(g1Str);
		ArrayList<Strategy> g2Str	=	new ArrayList<Strategy>();
		g2Str.add(s2);
		secondg.setStrategyList(g2Str);
		this.gridService.addGrid(prj1Grid);
		Grid ref	=	prj1Grid;
		prj1Grid	=	this.gridService.upgradeGrid(prj1Grid);
		Strategy third = new Strategy();
		third.setLabel("str3");
		third.setDescription("third strategy");
		secondg	=	(Goal) this.gridElementService.upgradeGridElement(secondg);
		List<Strategy> secondGStr	=	secondg.getStrategyList();
		secondGStr.add(third);
		secondg.setStrategyList(secondGStr);
		this.gridService.updateGridElement(prj1Grid, secondg, false, false);
		this.gridService.updateGrid(prj1Grid);
		JSONFactory aFactory	=	new JSONFactory();
		Grid secondJson	=	this.gridService.createStubUpgrade(prj1Grid);
		third	=	(Strategy) this.gridElementService.upgradeGridElement(third);
		third.setDescription("descrizione modificata");
		this.gridService.updateGridElement(secondJson, third, false, false);
		Goal nuovoMainGoal	=	new Goal();
		nuovoMainGoal.setDescription("nuovo main goal");
		nuovoMainGoal.setLabel("goal234");
		secondJson.getMainGoals().add(nuovoMainGoal);
		System.out.println(aFactory.obtainJson(prj1Grid, JSONType.FIRST,ref));
		System.out.println("##########");
		System.out.println(aFactory.obtainJson(secondJson,JSONType.FIRST,prj1Grid));
		//second step test
		this.gridService.addGrid(secondJson);
		nuovoMainGoal	=	(Goal) nuovoMainGoal.clone();
		nuovoMainGoal.setDescription("descrizione modificata main goal");
		ArrayList<Strategy> aStrategyList	=	new ArrayList<Strategy>();
		aStrategyList.add(s2);
		aStrategyList.add(s1);
		nuovoMainGoal.setStrategyList(aStrategyList);
		try {
			secondJson	=	this.gridModificationService.applyAModificationToASingleElement(secondJson, nuovoMainGoal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nuovoMainGoal	=	(Goal) nuovoMainGoal.clone();
		aStrategyList	=	new ArrayList<Strategy>();
		aStrategyList.add(s2);
		nuovoMainGoal.setStrategyList(aStrategyList);
		secondJson.getMainGoals().add(nuovoMainGoal);
		Goal innerGoal	=	new Goal();
		innerGoal.setLabel("test");
		innerGoal.setDescription("speriamo che ci sia");
		this.gridElementService.addGridElement(innerGoal);
		innerGoal	=	(Goal) innerGoal.clone();
		innerGoal.setVersion(innerGoal.getVersion()+1);
		ArrayList<Goal> s2glist	=	new ArrayList<Goal>();
		s2glist.add(innerGoal);
		s2.setGoalList(s2glist);
		Strategy leaf	=	new Strategy();
		leaf.setLabel("a leaf strategy");
		this.gridElementService.addGridElement(leaf);
		leaf	=	(Strategy) leaf.clone();
		leaf.setVersion(leaf.getVersion()+1);
		ArrayList<Strategy> leafstr	=	new ArrayList<Strategy>();
		leafstr.add(leaf);
		innerGoal.setStrategyList(leafstr);
		System.out.println("a json:");
		System.out.println(aFactory.obtainJson(secondJson,JSONType.FIRST,null));*/
		return "home";
	}
	
	//TODO remove test
	@RequestMapping(value = "/testpad", method = RequestMethod.GET)
	public String homeGridpad(Locale locale, Model model) { 
	return "firepadtest";
	}
	
	@RequestMapping(value = "/testHibernate", method = RequestMethod.GET)
	public String homeGridpadxx(Locale locale, Model model) { 
		for(int i=0;i<20;i++){
			try {
				Thread.sleep(1000);
				GridElement test	=	this.gridElementService.getElementById(1, "Goal");
				System.out.println(test);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return "home";
	}
	
	
	//TODO remove test
	@RequestMapping(value = "/testStep2", method = RequestMethod.GET)
	public String homeGridpad2(Locale locale, Model model) { 
		Grid aGrid		=	new Grid();
		Goal example	=	new Goal();
		Practitioner paride	=	new Practitioner();
		paride.setEmail("paride.casulli@gmail.com");
		paride.setName("Paride");
		Project aProject	=	new Project();
		aProject.setDescription("prova");
		aProject.setProjectId("prj1");
		aProject.setProjectManager(paride);
		aGrid.setProject(aProject);
		example.setAssumption("assunzione");
		example.setDescription("pippo");
		example.setLabel("unGoal");
		ArrayList<Goal> mainG	=	new ArrayList<Goal>();
		mainG.add(example);
		aGrid.setMainGoals(mainG);
		this.gridService.addGrid(aGrid);
		Grid prev	=	aGrid;
		aGrid	=	this.gridService.createStubUpgrade(aGrid);
		aGrid.setVersion(prev.getVersion());
		example	=	(Goal)example.clone();
		Strategy aStrategy	=	new Strategy();
		ArrayList<Practitioner> authors	=	new ArrayList<Practitioner>();
		authors.add(paride);
		aStrategy.setLabel("strategyLabel");
		aStrategy.setDescription("prova");
		aStrategy.setAuthors(authors);
		Strategy strategy2	=	new Strategy();
		strategy2.setLabel("secondStrategy");
		strategy2.setDescription("strategyyyyyyy");
		ArrayList<Strategy> strategies	=	new ArrayList<Strategy>();
		strategies.add(aStrategy);
		strategies.add(strategy2);
		example.setStrategyList(strategies);
		Goal secondGoal	=	new Goal();
		secondGoal.setLabel("secondgoallabel");
		secondGoal.setDescription("descrizione prima");
		secondGoal.setAuthors(authors);
		ArrayList<Goal> someGoals	=	new ArrayList<Goal>();
		someGoals.add(secondGoal);
		aStrategy.setGoalList(someGoals);
		try {
			Grid updated	=	this.gridModificationService.applyAModificationToASingleElement(prev, example);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nsleep	=	0;
		while(nsleep<30){
			this.logger.info("wait "+(30-nsleep)+"seconds");
			try {
				nsleep	=	nsleep+1;
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(secondGoal.getDescription());
			Goal sameElement	=	(Goal)this.gridElementService.getElementById(secondGoal.getIdElement(), secondGoal.getClass().getSimpleName());
			System.out.println(sameElement.getDescription());
		}
		ArrayList<GridElement> parameter	=	new ArrayList<GridElement>();
		parameter.addAll(strategies);
		String pad	=	Utils.generateEditor(parameter);
		model.addAttribute("pad", pad);
		return "firepadtest";
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
