package grid.modification.grid;

import java.util.HashMap;
import java.util.List;

import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;

/**
 * Models an inserction of a main goal to a Grid
 * @author Paride Casulli
 *
 */
public class MainGoalAdd extends GridModification {
	/**
	 * label of the Main Goal added
	 */
	private String appendedObjectLabel;
		
	/**
	 * Gets the label of the appended object
	 * @return
	 */
	public String getAppendedObjectLabel() {
		return appendedObjectLabel;
	}

	/**
	 * Sets the Main Goal label to be added
	 * @param appendedObjectLabel
	 */
	public void setAppendedObjectLabel(String appendedObjectLabel) {
		this.appendedObjectLabel = appendedObjectLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Grid grid) throws Exception {
		HashMap<String,GridElement> elements	=	grid.obtainAllEmbeddedElements();
		if(elements.containsKey(this.appendedObjectLabel)){
			GridElement	ge	=	elements.get(this.appendedObjectLabel);
			if(ge instanceof Goal){
				List<Goal>	list	=	grid.getMainGoals();
				Goal aGoal		=	(Goal)ge;
				list.add(aGoal);
			}
			else throw new Exception("Object "+this.appendedObjectLabel+" is not a Goal, cannot add it to maingoals");
		}
		else throw new Exception("Goal not found in elements, not possible to add as main goal");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MainGoalAdd added main Goal "+this.appendedObjectLabel;
	}

}
