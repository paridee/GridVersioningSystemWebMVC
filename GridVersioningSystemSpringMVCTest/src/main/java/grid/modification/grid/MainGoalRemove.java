package grid.modification.grid;

import grid.entities.Grid;

public class MainGoalRemove extends GridModification{
	private String RemovedObjectLabel;
	
	public String getRemovedObjectLabel() {
		return RemovedObjectLabel;
	}

	public void setRemovedObjectLabel(String removedObjectLabel) {
		RemovedObjectLabel = removedObjectLabel;
	}

	@Override
	public void apply(Grid grid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "MainGoalRemove removed main Goal "+this.RemovedObjectLabel;
	}
}
