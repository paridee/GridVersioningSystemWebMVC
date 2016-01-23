package grid.modification.grid;

import java.util.HashMap;

import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;

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
		HashMap<String,GridElement> 	elements	=	grid.obtainAllEmbeddedElements();
		if(elements.containsKey(this.RemovedObjectLabel)){
			GridElement goal	=	elements.get(this.RemovedObjectLabel);
			if(goal instanceof Goal){
				grid.getMainGoals().remove(goal);
			}
			else{
				throw new Exception("Element "+this.RemovedObjectLabel+" is not a goal");
			}
		}
		else throw new Exception("Main goal "+this.RemovedObjectLabel+" not present in the grid, impossible to remove");
	}

	@Override
	public String toString() {
		return "MainGoalRemove removed main Goal "+this.RemovedObjectLabel;
	}
}
