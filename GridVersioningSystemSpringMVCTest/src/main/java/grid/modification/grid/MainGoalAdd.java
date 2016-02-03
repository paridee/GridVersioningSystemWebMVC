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
	private Goal appendedObject;
		
	/**
	 * Gets the label of the appended object
	 * @return
	 */
	public Goal getAppendedObjectLabel() {
		return appendedObject;
	}

	/**
	 * Sets the Main Goal label to be added
	 * @param appendedObjectLabel
	 */
	public void setAppendedObject(Goal appendedObject) {
		this.appendedObject = appendedObject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Grid grid) throws Exception {
		HashMap<String,GridElement> elements	=	grid.obtainAllEmbeddedElements();
		if(elements.containsKey(this.appendedObject)){
			GridElement	ge	=	elements.get(this.appendedObject.getLabel());
			if(ge instanceof Goal){
				List<Goal>	list	=	grid.getMainGoals();
				Goal aGoal		=	(Goal)ge;
				list.add(aGoal);
			}
			else throw new Exception("Object "+this.appendedObject+" is not a Goal, cannot add it to maingoals");
		}
		else{
			List<Goal>	list	=	grid.getMainGoals();
			Goal aGoal		=	(Goal)this.appendedObject;
			list.add(aGoal);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MainGoalAdd added main Goal "+this.appendedObject;
	}

}
