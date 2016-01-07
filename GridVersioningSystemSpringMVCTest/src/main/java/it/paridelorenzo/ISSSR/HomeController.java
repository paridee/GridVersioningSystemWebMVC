package it.paridelorenzo.ISSSR;

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

import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Strategy;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private GridElementService 	gridElementService;
	private GridService			gridService;
	
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

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
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
								Grid newGrid	=	gridService.updateGridElement(testupdate, clone);
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
		HashMap<String,GridElement> map			=	gridService.getAllEmbeddedElements(iesima);
		Set<String> keys						=	gridService.getAllEmbeddedElements(iesima).keySet();
		ArrayList<GridElement> elements1		=	new ArrayList<GridElement>();
		Iterator<String> iterator	=	keys.iterator();
		while(iterator.hasNext()){
			elements1.add(map.get(iterator.next()));
		}
		
		HashMap<String,GridElement> map2			=	gridService.getAllEmbeddedElements(preced);
		keys						=	gridService.getAllEmbeddedElements(preced).keySet();
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
		testst.setIsTerminal(true);
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
			if(current.getClass().equals(ListChange.class)){
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
		diff	=	javers.compare(gridService.getAllEmbeddedElements(testupdate),gridService.getAllEmbeddedElements(iesima));
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
		return "home";
	}
	
}
