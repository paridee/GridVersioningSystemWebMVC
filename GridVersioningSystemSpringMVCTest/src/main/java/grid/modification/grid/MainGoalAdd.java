package grid.modification.grid;

import grid.entities.Grid;

public class MainGoalAdd extends GridModification {
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

	}

	@Override
	public String toString() {
		return "MainGoalAdd added main Goal "+this.appendedObjectLabel;
	}

}
