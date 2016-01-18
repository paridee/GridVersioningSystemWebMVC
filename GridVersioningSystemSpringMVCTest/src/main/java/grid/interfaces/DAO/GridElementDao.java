package grid.interfaces.DAO;

import java.util.List;
import grid.entities.GridElement;


/**
 * DAO for an element of the grid, this class has been build after a consolidation of all DAOs developed
 * for each entity, all these DAOs were sharing most of their code, this fact implies a problem for code
 * maintenance so all these classes have been unified in this one
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */

public interface GridElementDao {
	/**
	 * Adds a new Grid Element to persistence layer
	 * @param e element to be added
	 */
	public void addGridElement(GridElement e);
	
	/**
	 * Updates an existing element on Grif
	 * @param e element to be updated
	 */
	public void updateGridElement(GridElement e);
	
	/**
	 * Returns all grid elements present on persistence layer of a specific class
	 * @param c class in which elements must belong (entity classes)
	 * @return list of all elements found
	 */
	public List<GridElement> listElement(Class<?> c);
	
	/**
	 * Returns a specific element of an entity class with a specific ID
	 * @param id id of the element
	 * @param c class of the element
	 * @return element found
	 */
	public GridElement getElementById(int id,Class<?> c);
	
	/**
	 * Returns the history for an element
	 * @param label label (ID) of the interested element
	 * @param c class of the element
	 * @return list of objects representing the item history
	 */
	public List<GridElement> getElementLog(String label,Class<?> c);
	
	/**
	 * Removes a grid element from persistence
	 * @param id id of the element to be removed
	 * @param c class of the element to be removed
	 */
	public void removeElement(int id,Class<?> c);
}
