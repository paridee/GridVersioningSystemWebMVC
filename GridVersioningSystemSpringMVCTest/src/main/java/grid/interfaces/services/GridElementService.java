package grid.interfaces.services;

import java.util.List;

import grid.entities.GridElement;
import grid.entities.Project;
/**
 * Interface defining methods for a Grid element service, following CRUD operations and more
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

public interface GridElementService {
	/**
	 * Adds a grid element to persistence layer
	 * @param e element to be added on the grid
	 */
	public void addGridElement(GridElement e);
	/**
	 * Updates an element on persistence layer
	 * @param e element to be updated
	 */
	public void updateGridElement(GridElement e);
	/**
	 * Upgrades an element on persistence layer, creates a copy with bigger version number and return Its instance
	 * @param e element to be upgraded
	 * @return object upgraded
	 */
	public GridElement upgradeGridElement(GridElement e);
	
	/**
	 * Returns all the elements belonging to a specific class
	 * @param c class that will be queried
	 * @return list with all the complaining elements found
	 */
	public List<GridElement> listElement(Class<?> c);
	
	/**
	 * Returns an element of a specific class with Its own id found from DB
	 * @param id id of the object
	 * @param c class of the object
	 * @return object instance
	 */
	public GridElement getElementById(int id, String type);
	
	/**
	 * Returns the history of an element of a grid
	 * @param label id of the element
	 * @param c class of the element
	 * @return list of the objects representing the state
	 */
	public List<GridElement> getElementLog(String label, String table);
	
	/**
	 * Returns the latest version number of an element of a grid
	 * @param label id of the element
	 * @param c class of the element
	 * @return latest object found (may be null)
	 */
	public int getLatestVersion(String label, String table);
	
	/**
	 * Removes a grid element from persistence layer
	 * @param id id of the object to be removed
	 * @param c class of the object to be removed
	 */
	public void removeElement(int id,Class<?> c);

	/**
	 * Compares two versions of a grid element and check if is add-update
	 */
	public boolean isAddUpdate(GridElement oldElement,GridElement newElement);
	
	/**
	 * Obtain all elements with the same label that are on pending state
	 * @param subjLabel label of the object
	 * @param class1 class of the object
	 */
	public List<GridElement> getElementByLabelAndState(String subjLabel, String class1,GridElement.State state);
	
	/**
	 * Returns the latest working version of an element of a grid
	 * @param label id of the element
	 * @param classn class of the element
	 * @return latest object found (may be null)
	 */
	public GridElement getLatestWorking(String key,String classn);

	/**
	 * Returns all working elements on grid at the latest working version
	 * @return list of working elements
	 */
	public List<GridElement> getAllLatestWorking();
	
}
