package grid.modification.elements;

import grid.entities.Grid;
import grid.entities.GridElement;

public abstract class GridElementModification extends Modification{
	protected String 	subjectLabel;	//element to be modified
	public abstract void apply(GridElement anElement,Grid grid) throws Exception;
	public abstract String toString();
}
