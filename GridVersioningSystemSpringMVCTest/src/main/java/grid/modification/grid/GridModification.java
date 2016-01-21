package grid.modification.grid;

import grid.entities.Grid;
import grid.modification.elements.Modification;

/**
 * This abstract class models a generic GridModification and contains the main methods
 * @author Paride Casulli
 *
 */
public abstract class GridModification extends Modification {
	
	/**
	 * Applies this change to a grid (the grid passed as parameter has to be the same used for difference checking)
	 * @param grid grid subject to changes
	 * @throws Exception in case of errors while applying USES transaction management
	 */
	public abstract void apply(Grid grid) throws Exception;
	public abstract String toString();
}
