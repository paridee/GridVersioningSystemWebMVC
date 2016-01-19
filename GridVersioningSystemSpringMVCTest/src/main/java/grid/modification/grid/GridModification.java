package grid.modification.grid;

import grid.entities.Grid;
import grid.modification.elements.Modification;

public abstract class GridModification extends Modification {
	public abstract void apply(Grid grid) throws Exception;
	public abstract String toString();
}
