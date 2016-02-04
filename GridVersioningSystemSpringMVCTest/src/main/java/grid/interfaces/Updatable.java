package grid.interfaces;

import java.util.ArrayList;

import grid.entities.GridElement;

/**
 * This interface has to be implemented by all the Grid Elements classes having a reference to another
 * Grid Element which can be updated during Its lifecycle
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public interface Updatable {
	/**
	 * Updates all the reference for an object linked
	 * @param ge updated object replacing an older one
	 * @param autoupgrade set if autoupgrade needed
	 * @return list of all GridEntity updated
	 */
	public ArrayList<GridElement> updateReferences(GridElement ge,boolean autoupgrade);
	/**
	 * Updates all the reference for an object linked
	 * @param ge updated object replacing an older one
	 * @param autoupgrade set if autoupgrade needed
	 * @param recursive	set if has to be recursive or not
	 * @return list of all GridEntity updated
	 */
	public ArrayList<GridElement> updateReferences(GridElement ge,boolean autoupgrade,boolean recursive);
}
