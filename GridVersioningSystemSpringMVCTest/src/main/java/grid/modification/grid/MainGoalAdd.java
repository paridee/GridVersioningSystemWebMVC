package grid.modification.grid;

import grid.entities.Grid;

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
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MainGoalAdd added main Goal "+this.appendedObjectLabel;
	}

}
