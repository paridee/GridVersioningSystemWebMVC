package grid.modification.grid;

import grid.entities.Grid;
import grid.entities.GridElement;
import grid.modification.elements.Modification;

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
		if(!(Modification.minorUpdateClass.contains(this.gridElementAdded.getClass()))){
			//adding a new GQM+S element requires confirmation by PM
			gridElementAdded.setState(GridElement.State.MAJOR_UPDATING);
		}
	}

	@Override
	public String toString() {
		return "GridElementAdd added GridElement "+this.appendedObjectLabel;
	}

}
