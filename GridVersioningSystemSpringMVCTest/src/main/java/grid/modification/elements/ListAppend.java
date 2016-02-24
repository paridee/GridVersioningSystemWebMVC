package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.Grid;
import grid.entities.GridElement;

/**
 * This class models an addition of an element to a List belonging to a GridElement
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public class ListAppend extends GridElementModification {

	private String 	listNameToBeChanged;
	private String	appendedObjectLabel;
	private Object  newLoadedObject;
	private static final Logger logger = LoggerFactory.getLogger(ListAppend.class);
	
	/**
	 * Get the name of the list involved in this list append
	 * @return name of the list
	 */
	public String getListNameToBeChanged() {
		return listNameToBeChanged;
	}

	/**
	 * Set the name of the list in which the element will be added
	 * @param listNameToBeChanged name of the list
	 */
	public void setListNameToBeChanged(String listNameToBeChanged) {
		this.listNameToBeChanged = listNameToBeChanged;
	}

	/**
	 * Get the label of the appended object
	 * @return label of the appended object
	 */
	public String getAppendedObjectLabel() {
		return appendedObjectLabel;
	}

	/**
	 * Set the label of the appended object
	 * @param appendedObjectLabel label of the appended object
	 */
	public void setAppendedObjectLabel(String appendedObjectLabel) {
		this.appendedObjectLabel = appendedObjectLabel;
	}

	/**
	 * Get the appended object (if is a new element on the grid)
	 * @return new appened object
	 */
	public Object getNewLoadedObject() {
		return newLoadedObject;
	}

	/**
	 * Set the new loaded object
	 * @param newLoadedObject object loaded
	 */
	public void setNewLoadedObject(Object newLoadedObject) {
		this.newLoadedObject = newLoadedObject;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void apply(GridElement anElement, Grid grid) throws Exception {
		Field aField	=	anElement.getClass().getDeclaredField(this.listNameToBeChanged);
		aField.setAccessible(true);
		Object myList	=	aField.get(anElement);
		if(myList instanceof List){
			List	aList						=	(List)myList;
			HashMap<String,GridElement>	elMap	=	grid.obtainAllEmbeddedElements();
			if(elMap.containsKey(this.appendedObjectLabel)){
				logger.info("appending an object already existing on this grid");
				GridElement	element	=	elMap.get(this.appendedObjectLabel);
				aList.add(element);
			}
			else if(this.newLoadedObject!=null){	//i have to append a new object
				logger.info("appending an object not existing on this grid");
				newLoadedObject	=	((GridElement) newLoadedObject).clone();
				HashMap<String, GridElement> elements	=	grid.obtainAllEmbeddedElements();
				Iterator<String> it	=	elements.keySet().iterator();
				while(it.hasNext()){
					((GridElement) newLoadedObject).updateReferences(elements.get(it.next()), false, false);
				}
				aList.add(newLoadedObject);
			}
			else{
				throw new Exception("Object to be added not found in current Grid");	
			}
		}
		else{
			throw new Exception("Trying to make list modification to a non list object");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ListAppend: append this element "+this.appendedObjectLabel+"("+this.newLoadedObject+")in this list "+this.listNameToBeChanged+" in this object "+this.subjectLabel;
	}

}
