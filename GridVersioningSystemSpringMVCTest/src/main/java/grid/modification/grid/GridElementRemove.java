package grid.modification.grid;

import grid.entities.Grid;

public class GridElementRemove extends GridModification {

	private String RemovedObjectLabel;


	public String getRemovedObjectLabel() {
		return RemovedObjectLabel;
	}

	public void setRemovedObjectLabel(String removedObjectLabel) {
		RemovedObjectLabel = removedObjectLabel;
	}
	
	@Override
	public void apply(Grid grid) throws Exception {
		//nothing to do, this check is implied by a GridElementModification
	}

	@Override
	public String toString() {
		return "GridElementRemove removed GridElement "+this.RemovedObjectLabel;
	}


}
