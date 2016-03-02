package grid.interfaces.services;

import java.util.List;

import org.json.JSONObject;

import grid.JSONFactory.JSONType;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Practitioner;

/**
 * Interface defining methods for a Grid service, following CRUD operations and more
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

public interface GridService {
	/**
	 * Loads a Grid from JSON and saves on DB
	 * @param json string to be parsed 
	 * @return grid instance representing the Grid just loaded
	 */
	Grid loadFromJson(String json);
	
	/**
	 * Adds a Grid Instance to persistence layer 
	 * @param g grid to be added
	 */
	public void addGrid(Grid g);
	
	/**
	 * Updates an existing grid on persistence layer
	 * @param p grid to be updated
	 */
	public void updateGrid(Grid p);
	
	/**
	 * Upgrades a Grid on persistence layer following DAO specifications
	 * @param p grid to be upgraded
	 * @return upgraded grid
	 */
	public Grid upgradeGrid(Grid p);
	
	/**
	 * Creates an upgrade of a grid without saving it on persistence layer
	 * @param g grid to be upgraded
	 * @return upgraded object
	 */
	public Grid createStubUpgrade(Grid g);
	
	/**
	 * Returns a list with all the grids present on persistence layer
	 * @return Grid list
	 */
	public List<Grid> listAllGrids();
	
	/**
	 * Gets a Grid with a known Id
	 * @param id id of the grid desired
	 * @return Grid object
	 */
	public Grid getGridById(int id);
	
	/**
	 * Returns the latest Grid for a project
	 * @param projid id of project
	 * @return Grid desired
	 */
	public Grid getLatestGrid(int projid);
	
	
	/**
	 * Returns the latest working Grid for a project
	 * @param projid id of project
	 * @return Grid desired
	 */
	public Grid getLatestWorkingGrid(int projid);
	
	/**
	 * Returns the history of Grids for a project
	 * @param projid id of the desired project
	 * @return list of Grids of the project
	 */
	public List<Grid> getGridLog(int projid);
	
	/**
	 * Returns a list of practitioners involved in this project
	 * @param projid project id
	 * @return involved practitioners
	 */
	public List<Practitioner> getInvolvedPractitioners(int projid,boolean includeDefaults);
	
	/**
	 * Updates an element on the Grid
	 * @param g Grid of the element to be updated
	 * @param ge Element of the Grid to be updated
	 * @param autoupgradeElement set if autoupgrade is enabled
	 * @return updated Grid
	 */
	public Grid updateGridElement(Grid g,GridElement ge,boolean autoupgradeElement,boolean autoupgradeGrid);
	
	/**
	 * Removes a Grid from persistence layer
	 * @param id id of the element to be removed
	 */
	public void removeGrid(int id);
	
	/**
	 * Check if a grid is an add update of another one
	 */
	public boolean isAddUpdate(Grid oldGrid, Grid newGrid);
	
}
