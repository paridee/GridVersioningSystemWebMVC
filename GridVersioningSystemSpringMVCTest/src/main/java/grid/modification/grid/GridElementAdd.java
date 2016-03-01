package grid.modification.grid;

import grid.entities.Grid;
import grid.entities.GridElement;

public class GridElementAdd extends GridModification {

	private String appendedObjectLabel;
	private GridElement gridElementAdded;
	
	public String getAppendedObjectLabel() {
		return appendedObjectLabel;
	}

	public void setAppendedObjectLabel(String appendedObjectLabel) {
		this.appendedObjectLabel = appendedObjectLabel;
	}

	public GridElement getGridElementAdded() {
		return gridElementAdded;
	}

	public void setGridElementAdded(GridElement gridElementAdded) {
		this.gridElementAdded = gridElementAdded;
	}

	@Override
	public void apply(Grid grid) throws Exception {
	}

	@Override
	public String toString() {
		return "GridElementAdd added GridElement "+this.appendedObjectLabel;
	}

}
