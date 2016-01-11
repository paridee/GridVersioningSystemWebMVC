package grid.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.MapChange;
import org.springframework.transaction.annotation.Transactional;

import grid.Utils;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.interfaces.DAO.GridDAO;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;


/**
 * Service managing Grid DAO
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */

public class GridServiceImpl implements GridService {
	private GridDAO 			gridDao;
	private GridElementService 	gridElementService;
	
	/**
	 * Sets a DAO that will be used in the following Grid operations
	 * @param dao DAO that will be used
	 */
	public void setGridDAO(GridDAO dao){
		this.gridDao	=	dao;
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
		return this.gridDao.getGridById(projid);
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
	public void removeGrid(int id) {
		this.gridDao.removeGrid(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Grid updateGridElement(Grid g,GridElement ge) {
		HashMap<String,GridElement> checked	=	new HashMap<String,GridElement>();
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
						Utils.mergeLists(nextCheck, mainGoals.get(j).update(subj));
					}
				}
			}
			toBeChecked	=	nextCheck;
		}
		Grid updated	=	this.upgradeGrid(g);
		List <Goal> mainGoals	=	updated.getMainGoals();
		for(int i=0;i<mainGoals.size();i++){
			if(checked.containsKey(mainGoals.get(i).getLabel())){
				mainGoals.set(i,(Goal)checked.get(mainGoals.get(i).getLabel()));
			}
		}
		updated.setMainGoals(mainGoals);
		return updated;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, GridElement> getAllEmbeddedElements(Grid g) {
		HashMap<String,GridElement> returnMap	=	new HashMap<String,GridElement>();
		for(int i=0;i<g.getMainGoals().size();i++){
			returnMap.putAll(g.getMainGoals().get(i).obtainEmbeddedElements());
		}
		return returnMap;
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
		HashMap<String, GridElement>	oldElements	=	this.getAllEmbeddedElements(oldGrid);
		HashMap<String, GridElement>	newElements	=	this.getAllEmbeddedElements(newGrid);
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

}