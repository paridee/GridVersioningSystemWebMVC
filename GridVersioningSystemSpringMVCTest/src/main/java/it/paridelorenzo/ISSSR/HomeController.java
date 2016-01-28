package it.paridelorenzo.ISSSR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import grid.JSONFactory;
import grid.DAOImpl.ProjectDAOImpl;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.GridElement.State;
import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Project;
import grid.entities.Question;
import grid.entities.Strategy;
import grid.interfaces.DAO.ProjectDAO;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectModificationService;
import grid.modification.grid.GridModificationService;
import grid.services.GridElementServiceImpl;
import grid.services.ProjectServiceImpl;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ProjectService		projectService;
	
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


	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String hometest(Locale locale, Model model) {
		Goal pippo	=	new Goal();
		pippo.setAssumption("pippoassumption");
		pippo.setLabel("stocazzo");
		this.gridElementService.addGridElement(pippo);
		System.out.println("prova load");
		Goal pippo2	=	(Goal) pippo.clone();
		pippo2.setDescription("popopop");
		Strategy pipps	=	new Strategy();
		pipps.setLabel("stocazzo_strategy");
		List strList	=	pippo2.getStrategyList();
		MeasurementGoal stub	=	new MeasurementGoal();
		stub.setLabel("stubMG");
		pippo2.setMeasurementGoal(stub);
		pippo.setMeasurementGoal(stub);
		System.out.print("clone: "+pippo2.toString("\t\t", "\n"));
		strList.add(pipps);
		try {
			List<GridElementModification>	modList	=	ObjectModificationService.getModification(pippo2, pippo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Grid test	=	new Grid();
		ArrayList<Goal> mg1	=	new ArrayList<Goal>();
		mg1.add(pippo);
		test.setMainGoals(mg1);
		Grid test2	=	new Grid();
		Goal testMG	=	new Goal();
		testMG.setLabel("test main goal");
		ArrayList<Goal> mg2	=	new ArrayList<Goal>();
		mg2.add(pippo2);
		mg2.add(testMG);
		test2.setMainGoals(mg2);
		String jsonV	=	this.gridElementService.obtainJson(pippo2, GridElementServiceImpl.JSONType.FIRST).toString();
		System.out.println("JSON Ottenuto "+jsonV);
		List<Modification> mod;
		try {
			mod = GridModificationService.getModification(test, test2);
			System.out.println("Mie modifiche");
			for(int i=0;i<mod.size();i++){
				System.out.println(mod.get(i).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mod	=	GridModificationService.getModification(test2, test);
			System.out.println("Mie modifiche 2");
			for(int i=0;i<mod.size();i++){
				System.out.println(mod.get(i).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Goal test3	=	(Goal)testMG.clone();
		test3.getStrategyList().add(pipps);
		System.out.println("TESTISSIMO "+test3.toString("\t\t","\n"));
		return "home";
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
			mods	=	testFactory.loadModificationJson(text, refGrid);
			for(int i=0;i<mods.size();i++){
				System.out.println(mods.get(i).toString());
			}
			
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
	
	//TODO remove test
	@RequestMapping(value = "/test2Grid", method = RequestMethod.GET)
	public String home2Grid(Locale locale, Model model) {
			logger.info("Welcome home! The client locale is {}.", locale);
			
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			
			Project test	=	new Project();
			test.setDescription("progetto di prova");
			
			String everything	=	"";
			try(BufferedReader br = new BufferedReader(new FileReader(new File("grid.txt")))) {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    everything = sb.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Grid testGrid = null;
			try {
				testGrid = JSONFactory.loadFromJson(everything, projectService);
				System.out.println("CARICATA GRID DA JSON "+everything);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("HomeController stampo errore json");
				e.printStackTrace();
				model.addAttribute("jsonError", e.getMessage());
			}

			everything	=	"";
			try(BufferedReader br = new BufferedReader(new FileReader(new File("grid2.txt")))) {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    everything = sb.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Grid testGrid2 = null;
			try {
				testGrid2 = JSONFactory.loadFromJson(everything, projectService);
				System.out.println("CARICATA GRID DA JSON "+everything);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("HomeController stampo errore json");
				e.printStackTrace();
				model.addAttribute("jsonError", e.getMessage());
			}
			try {
				ArrayList<Modification> modifications	=	GridModificationService.getModification(testGrid, testGrid2);
				this.logger.info("Modifications number: "+modifications.size());
				for(int i=0;i<modifications.size();i++){
					this.logger.info(modifications.get(i).toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "home";
	}
	
	//TODO remove test
	@RequestMapping(value = "/testGrid", method = RequestMethod.GET)
	public String homeGrid(Locale locale, Model model) {
			logger.info("Welcome home! The client locale is {}.", locale);
			
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			
			Project test	=	new Project();
			test.setDescription("progetto di prova");
			Grid testGrid	=	new Grid();
			testGrid.setProject(test);
			ArrayList<Goal> maingoals	=	new ArrayList<Goal>();
			testGrid.setMainGoals(maingoals);
			Goal firstGoal	=	new Goal();
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
			Grid newGrid	=	this.gridService.updateGridElement(testGrid, updated,true);
			Goal anotherGoal	=	new Goal();
			anotherGoal.setLabel("testttt");
			Grid newRel	=	this.gridService.upgradeGrid(newGrid);
			newRel.getMainGoals().add(anotherGoal);
			this.gridService.updateGrid(newRel);
			anotherGoal	=	(Goal)this.gridElementService.upgradeGridElement(anotherGoal);
			anotherGoal.setAssumption("ASSUNZIONEEEEEEEEE");
			newRel	=	this.gridService.updateGridElement(newRel, anotherGoal,true);
			ArrayList<Strategy> strlista	=	new ArrayList<Strategy>();
			strlista.add(aStrategy);
			anotherGoal	=	(Goal) this.gridElementService.upgradeGridElement(anotherGoal);
			anotherGoal.setStrategyList(strlista);
			newRel	=	this.gridService.updateGridElement(newRel, anotherGoal,true);
			return "home";
	}
	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );	
		
		List<Grid> pippo	=	gridService.listAllGrids();
		System.out.println("Grid caricate "+pippo.size());
		Grid testupdate	=	null;
		Grid iesima		=	null;
		Grid preced		=	null;
		for(int i=0;i<pippo.size();i++){
			preced	=	iesima;
			iesima	=	pippo.get(i);
			List<Goal> listagoal	=	iesima.getMainGoals();
			System.out.println("Grid numero "+i+" caricati "+listagoal.size()+" goals");
			for(int j=0;j<listagoal.size();j++){
				System.out.println("Grid numero "+i+" goal numero "+j+" caricate "+listagoal.get(j).getStrategyList().size()+" strategies");
			}
			testupdate	=	iesima;
		}
		List<Goal> goals	=	testupdate.getMainGoals();
		System.out.println("CARICATO "+goals.size()+" GOALS");
		for(int i=0;i<goals.size();i++){
			System.out.println(goals.get(i).getLabel());
			if(goals.get(i).getLabel().equals("g1")){
				System.out.println(goals.get(i).getLabel()+" TROVATO");
				List<Strategy> strategies	=	goals.get(i).getStrategyList();
				for(int j=0;j<strategies.size();j++){
					if(strategies.get(j).getLabel().equals("s1")){
						List<Goal> goalsInf	=	strategies.get(j).getGoalList();
						for(int k=0;k<goalsInf.size();k++){
							if(goalsInf.get(k).getLabel().equals("g2")){
								Goal clone	=	(Goal)goalsInf.get(k).clone();
								clone.setVersion(clone.getVersion()+1);
								clone.setDescription(clone.getDescription()+"*");//+clone.getVersion());
								System.out.println("Aggiorno");
								Grid newGrid	=	gridService.updateGridElement(testupdate, clone,true);
								//strategies.get(j).setDescription("non deve salvare questo");
								System.out.println("Aggiornato");
								model.addAttribute("gridString",newGrid.toString("&nbsp&nbsp&nbsp&nbsp", "<br>") );
								Goal clone2	=	(Goal) clone.clone();
								//clone2.setDescription(clone2.getDescription()+" stocazzo");
								System.out.println("checkEquals "+clone.equals(clone2));
								iesima	=	newGrid;
							}
						}
					}
				}
			}
		}
		HashMap<String,GridElement> map			=	iesima.obtainAllEmbeddedElements();
		Set<String> keys						=	iesima.obtainAllEmbeddedElements().keySet();
		ArrayList<GridElement> elements1		=	new ArrayList<GridElement>();
		Iterator<String> iterator	=	keys.iterator();
		while(iterator.hasNext()){
			elements1.add(map.get(iterator.next()));
		}
		
		HashMap<String,GridElement> map2			=	preced.obtainAllEmbeddedElements();
		keys						=	preced.obtainAllEmbeddedElements().keySet();
		ArrayList<GridElement> elements2		=	new ArrayList<GridElement>();
		iterator	=	keys.iterator();
		while(iterator.hasNext()){
			elements2.add(map2.get(iterator.next()));
		}
		Javers javers	=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		Diff diff	=	javers.compare(elements1, elements2);
		System.out.println("DIFF GRIDS "+diff);
		Goal testdiff	=	new Goal();
		testdiff.setLabel("pippo");
		testdiff.setDescription("test tool diff");
		Strategy testst	=	new Strategy();
		testst.setLabel("prova");
		testst.setStrategyType("TERMINAL");
		ArrayList<Strategy> l1	=	new ArrayList<Strategy>();
		ArrayList<Strategy> l2	=	new ArrayList<Strategy>();
		l1.add(testst);
		Goal testdiff2	=	(Goal)testdiff.clone();
		testdiff.setStrategyList(l1);
		testdiff2.setStrategyList(l2);
		diff	=	javers.compare(testdiff, testdiff2);
		System.out.println("DIFF "+diff);
		diff	=	javers.compare(map, map2);
		Goal g21	=	(Goal) map.get("g2");
		Goal g22	=	(Goal) map2.get("g2");
		g22.setState(State.MAJOR_UPDATING);
		String me	=	"paride.casulli@gmail.com";
		ArrayList<String> authors	=	new ArrayList<String>();
		authors.add(me);
		//g22.setAuthors(authors);
		diff	=	javers.compare(g22, g21);
		System.out.println("DIFF goals"+diff);
		System.out.println("DIFF elements "+diff);
		diff	=	javers.compare(testdiff2.getStrategyList(),testdiff.getStrategyList());
		System.out.println("DIFFLIST1 "+diff);
		Strategy another	=	new Strategy();
		another.setLabel("ppppp");
		l2.add(another);
		another	=	new Strategy();
		another.setLabel("ppppp2");
		l2.add(another);
		diff	=	javers.compare(testdiff2.getStrategyList(),testdiff.getStrategyList());
		System.out.println("DIFFLIST2 "+diff);
		testdiff2.setDescription("pippowww");
		testdiff2.setVersion(10000);
		System.out.println("TEST CAMBIO IO");
		
		try {
			Field strList	=	Goal.class.getDeclaredField("description");
			strList.setAccessible(true);
			String listlist		=	(String)strList.get(testdiff);
			//List listlist		=	(List)strList.get(testdiff2);
			System.out.println("TROVATA LISTAAAAAAA"+listlist);
			
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.gridElementService.isAddUpdate(testdiff,testdiff2);
		diff	=	javers.compare(testdiff, testdiff2);
		System.out.println("DIFF2 "+diff);
		List<Change> changes	=	diff.getChanges();
		for(int i=0;i<changes.size();i++){
			Change current	=	changes.get(i);
			System.out.println(current.getClass().getName()+" "+current.getAffectedObject().get());
			if(current.getClass().equals(ValueChange.class)){
				ValueChange thisChange	=	(ValueChange)current;
				Object changed	=	thisChange.getAffectedObject().get();
				//try {
					System.out.println("Classe oggetto "+changed.getClass());
					Field[] attributes	=	changed.getClass().getDeclaredFields();
					ArrayList<String> superClassFields	=	new ArrayList();
					Field[] supAttributes	=	GridElement.class.getDeclaredFields();
					for(int z=0;z<supAttributes.length;z++){
						System.out.println("ATTRIBUTO"+supAttributes[z].getName());
						superClassFields.add(supAttributes[z].getName());
					}
					if(superClassFields.contains(thisChange.getPropertyName())){
						System.out.println("attributo della superclasse");
						Field test;
						try {
							test = GridElement.class.getDeclaredField(thisChange.getPropertyName());
							test.setAccessible(true);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						System.out.println("attributo non della superclasse");
						Field test;
						try {
							test = changed.getClass().getDeclaredField(thisChange.getPropertyName());
							test.setAccessible(true);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//System.out.println("Campi oggetto "+attributes);
					GridElement changedGE	=	(GridElement)changed;
					System.out.println("Cambiato valore su "+changedGE.getLabel()+" attributo "+thisChange.getPropertyName()+" da "+thisChange.getLeft()+" a "+thisChange.getRight()+" tipo attributo "+thisChange.getLeft().getClass());//+attribute.getClass());
					/*
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			else if(current.getClass().equals(ListChange.class)){
				ListChange thisChange	=	(ListChange)current;
				List<ContainerElementChange>	listchanges		=	thisChange.getChanges();	
				System.out.println("cambio a livello lista tipo "+thisChange.getChanges()+" prop "+thisChange.getPropertyName()+" object "+thisChange.getAffectedObject().get());
				Field field;
				Object subject	=	thisChange.getAffectedObject().get();
				try {
					field = subject.getClass().getDeclaredField(thisChange.getPropertyName());
					field.setAccessible(true);
					Object value = field.get(subject);
					System.out.println("Test Reflection "+value);
					
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int j=0;j<listchanges.size();j++){
					ContainerElementChange	currentChange	=	listchanges.get(j);
					System.out.println("cambio "+currentChange.toString());
				}
			}
		}
		diff	=	javers.compare(testupdate.obtainAllEmbeddedElements(),testupdate.obtainAllEmbeddedElements());
		System.out.println("DIFF3 "+diff);
		System.out.println("DIFF4 ");
		ArrayList<String> test1	=	new ArrayList<String>();
		test1.add("pippo");
		test1.add("pasquale");
		test1.add("filippo");
		test1.add("marco");
		test1.add("luca");
		ArrayList<String> test2	=	new ArrayList<String>();
		test2.add("pippo");
		test2.add("pasquale");
		test2.add("filippo");
		test2.add("marco");
		test2.add("luca");
		diff	=	javers.compare(test1,test2);
		System.out.println("DIFF TEST STRING "+diff);
		test2.remove(2);
		diff	=	javers.compare(test1,test2);
		System.out.println("DIFF TEST STRING after deletion "+diff);
		
		Project testpr	=	iesima.getProject();
		String test	=	testpr.getProjectId()+" "+testpr.getDescription();
		System.out.println(test);
		System.out.println(gridService.getGridLog(testpr.getId()));
		
		Project prova	=	this.projectService.getProjectByProjectId("progetto di prova cazzo");
		System.out.println("PROGETTO TROVATO "+prova);
		
		
		//PARSING GRID
		File file	=	new File("grid.txt");
		try {
			System.out.println(file.getAbsolutePath()+" "+file.getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String everything	=	"";
		try(BufferedReader br = new BufferedReader(new FileReader(new File("grid.txt")))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Grid testGrid;
		try {
			testGrid = JSONFactory.loadFromJson(everything, projectService);
			this.gridService.addGrid(testGrid);
			System.out.println("CARICATA GRID DA JSON "+everything);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("HomeController stampo errore json");
			e.printStackTrace();
			model.addAttribute("jsonError", e.getMessage());
		}
		return "home";
	}
	
}
