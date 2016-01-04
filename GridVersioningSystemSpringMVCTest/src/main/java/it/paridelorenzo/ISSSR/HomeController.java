package it.paridelorenzo.ISSSR;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
		for(int i=0;i<pippo.size();i++){
			Grid iesima	=	pippo.get(i);
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
								clone.setDescription("modificatoTESTSpringMVCFunzionante");
								System.out.println("Aggiorno");
								Grid newGrid	=	gridService.updateGridElement(testupdate, clone);
								strategies.get(j).setDescription("non deve salvare questo");
								System.out.println("Aggiornato");
								model.addAttribute("gridString",newGrid.toString("&nbsp&nbsp&nbsp&nbsp", "<br>") );
							}
						}
					}
				}
			}
		}
		return "home";
	}
	
}
