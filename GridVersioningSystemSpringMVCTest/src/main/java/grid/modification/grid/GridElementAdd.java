package grid.modification.grid;

import grid.entities.Grid;

public class GridElementAdd extends GridModification {

	private String appendedObjectLabel;
	
	public String getAppendedObjectLabel() {
		return appendedObjectLabel;
	}

	public void setAppendedObjectLabel(String appendedObjectLabel) {
		this.appendedObjectLabel = appendedObjectLabel;
	}

	@Override
	public void apply(Grid grid) throws Exception {
		// TODO Auto-generated method stub
		//nothing to do, this check is implied by a GridElementModification
	}

	@Override
	public String toString() {
		return "GridElementAdd added GridElement "+this.appendedObjectLabel;
	}

}
