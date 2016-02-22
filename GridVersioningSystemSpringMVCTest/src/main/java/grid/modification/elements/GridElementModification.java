package grid.modification.elements;

import grid.entities.Grid;
import grid.entities.GridElement;

/**
 * Class modeling a modification on a GridElement, will be extended by each implementation with the 
 * appropriate operations for the type of the change
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public abstract class GridElementModification extends Modification{
	protected String 	subjectLabel;	//element to be modified
	
	/**
	 * Gets the label of the object involved in this modification
	 * @return label of the object that will be modified
	 */
	public String getSubjectLabel() {
		return subjectLabel;
	}

	/**
	 * Sets the label of the object that will be modified
	 * @param subjectLabel label of the object to be modified
	 */
	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}
	
	/**
	 * Applies this modification to an element belonging to a Grid
	 * @param anElement element to be updated
	 * @param grid grid in which this modification will be applied
	 * @throws Exception raised in case of error while updating, manage with transactions
	 */
	public abstract void apply(GridElement anElement,Grid grid) throws Exception;
	public abstract String toString();
}
