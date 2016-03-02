package grid.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import grid.JSONFactory.JSONType;
import grid.Utils;
import grid.entities.DefaultResponsible;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.interfaces.DAO.GridDAO;
import grid.interfaces.services.DefaultResponsibleService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;


/**
 * Service managing Grid DAO
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */

public class GridServiceImpl implements GridService {
	private GridDAO 					gridDao;
	private GridElementService 			gridElementService;
	private DefaultResponsibleService	defaultResponsibleService;
	
	/**
	 * Sets a DAO that will be used in the following Grid operations
	 * @param dao DAO that will be used
	 */
	public void setGridDAO(GridDAO dao){
		this.gridDao	=	dao;
	}
	
	/**
	 * Sets a DefaultResponsibleService for responsibles identification
	 * @param defaultResponsibleService service
	 */
	public void setDefaultResponsibleService(DefaultResponsibleService defaultResponsibleService) {
		this.defaultResponsibleService = defaultResponsibleService;
	}



	/**
	 * Sets a GridElementService that will be used in the following Grid operations
	 * @param service that will be used
	 */
	public void setGridElementService(GridElementService gridElementService) {
		this.gridElementService = gridElementService;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Grid loadFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void addGrid(Grid g) {
		this.gridDao.addGrid(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateGrid(Grid g) {
		this.gridDao.updateGrid(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Grid upgradeGrid(Grid p) {
		return this.gridDao.upgradeGrid(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Grid> listAllGrids() {
		return this.gridDao.listAllGrids();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Grid getGridById(int id) {
		return this.gridDao.getGridById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Grid getLatestGrid(int projid) {
		return this.gridDao.getLatestGrid(projid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Grid getLatestWorkingGrid(int projid) {
		List<Grid> allGrids	=	this.gridDao.getGridLog(projid);
		Grid aGrid	=	null;
		if(allGrids.size()>0){
			int ver	=	-1;
			for(int i=0;i<allGrids.size();i++){
				if(allGrids.get(i).getVersion()>ver){
					if(allGrids.get(i).obtainGridState()==Grid.GridState.WORKING){
						aGrid	=	allGrids.get(i);
						ver		=	allGrids.get(i).getVersion();	
					}
				}
			}
		}
		return aGrid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Grid> getGridLog(int projid) {
		return this.gridDao.getGridLog(projid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void removeGrid(int id) {
		System.out.println("GridServiceImpl going to remove grid id "+id);
		this.gridDao.removeGrid(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Grid updateGridElement(Grid g,GridElement ge,boolean autoupgradeElement,boolean autoupgradeGrid) {
		//elements already checked
		HashMap<String,GridElement> checked	=	new HashMap<String,GridElement>();
		//elements to be checked
		ArrayList<GridElement> toBeChecked	=	new ArrayList<GridElement>();	
		toBeChecked.add(ge);
		while(toBeChecked.size()>0){
			ArrayList<GridElement> nextCheck	=	new ArrayList<GridElement>();
			for(int i=0;i<toBeChecked.size();i++){
				GridElement subj	=	toBeChecked.get(i);
				if(!checked.containsKey(subj.getLabel())){
					checked.put(subj.getLabel(), subj);
					List<Goal> mainGoals	=	g.getMainGoals();
					for(int j=0;j<mainGoals.size();j++){
						Utils.mergeLists(nextCheck, mainGoals.get(j).updateReferences(subj,true));
					}
				}
			}
			toBeChecked	=	nextCheck;
		}
		Grid updated	=	null;
		if(autoupgradeGrid	==	false){
			updated	=	g;	
		}
		else{
			updated	=	g.clone();
			updated.setVersion(g.getVersion()+1);
		}
		List <Goal> mainGoals	=	updated.getMainGoals();
		for(int i=0;i<mainGoals.size();i++){
			if(checked.containsKey(mainGoals.get(i).getLabel())){
				mainGoals.set(i,(Goal)checked.get(mainGoals.get(i).getLabel()));
			}
		}
		updated.setMainGoals(mainGoals);
		return updated;
	}

	@Override
	public boolean isAddUpdate(Grid oldGrid, Grid newGrid) {
		List<Goal>	oldMainGoal						=	oldGrid.getMainGoals();
		List<Goal>	newMainGoal						=	newGrid.getMainGoals();
		//checks if new main goal list contains ALL the old grid main goals
		HashMap<String,GridElement>	newMainGoalMap	=	new HashMap<String, GridElement>();
		for(int i=0;i<newMainGoal.size();i++){
			newMainGoalMap.put(newMainGoal.get(i).getLabel(), newMainGoal.get(i));
		}
		for(int i=0;i<oldMainGoal.size();i++){
			if(!newMainGoalMap.containsKey(oldMainGoal.get(i).getLabel())){
				return false;
			}
		}
		//check if all the elements of the old grid had only "add updates" in the new grid
		HashMap<String, GridElement>	oldElements	=	oldGrid.obtainAllEmbeddedElements();
		HashMap<String, GridElement>	newElements	=	newGrid.obtainAllEmbeddedElements();
		Iterator<String> 				oldIterator	=	oldElements.keySet().iterator();
		while(oldIterator.hasNext()){
			String currentLabel		=	oldIterator.next();
			//checks if new grid contains this old element (if not return false... deletion!)
			if(!newElements.containsKey(currentLabel)){
				return false;
			}
			GridElement	oldElement	=	oldElements.get(currentLabel);
			GridElement	newElement	=	newElements.get(currentLabel);
			boolean result			=	this.gridElementService.isAddUpdate(oldElement, newElement);
			if(result==false){
				return false;
			}
		}
		return true;
	}

	@Override
	public Grid createStubUpgrade(Grid g) {
		Grid 						newVersion	=	new Grid();
		ArrayList<Goal> mainGoalsCopy	=	new ArrayList<Goal>();
		List<Goal> gridMainGoals	=	g.getMainGoals(); //a direct reference creates errors in hibernate (shared reference to a collection)	
		for(int i=0;i<gridMainGoals.size();i++){
			mainGoalsCopy.add(gridMainGoals.get(i));
		}
		newVersion.setMainGoals(mainGoalsCopy);
		newVersion.setProject(g.getProject());
		newVersion.setVersion(g.getVersion()+1);
		newVersion.setMainGoalsChanged(g.isMainGoalsChanged());
		return newVersion;
	}

	@Override
	public List<Practitioner> getInvolvedPractitioners(int projid, boolean includeDefaults) {
		List<Grid> allGrids	=	this.getGridLog(projid);
		ArrayList<Practitioner> practitioners	=	new ArrayList<Practitioner>();
		Project aPrj	=	null;
		if(allGrids.size()>0){
			aPrj	=	allGrids.get(0).getProject();
			if(aPrj!=null){
				Practitioner pm	=	aPrj.getProjectManager();
				if(pm!=null){
					practitioners.add(pm);
				}
			}
			for(Grid g:allGrids){
				HashMap<String,GridElement> gridElements	=	g.obtainAllEmbeddedElements();
				Iterator<String> anIterator	=	gridElements.keySet().iterator();
				while(anIterator.hasNext()){
					GridElement anElement		=	gridElements.get(anIterator.next());
					List<Practitioner> authors	=	anElement.getAuthors();
					for(Practitioner p:authors){
						if(!practitioners.contains(p)){
							practitioners.add(p);
						}
					}
				}
			}
		}
		if(includeDefaults){
			List<DefaultResponsible> defRes	=	this.defaultResponsibleService.getAllResponsibles();
			for(DefaultResponsible dr:defRes){
				Practitioner pract	=	dr.getPractitioner();
				if(!practitioners.contains(pract)){
					practitioners.add(pract);
				}
			}
		}
		return practitioners;
	}

}
