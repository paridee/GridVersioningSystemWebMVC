package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import grid.entities.Grid;
import grid.entities.GridElement;

/**
 * This class models an addition of an element to a List belonging to a GridElement
 * @author Paride Casulli
 * @author Lorenzo La Banza
 *
 */
public class ListAppend extends GridElementModification {

	private String 	listNameToBeChanged;
	private String	appendedObjectLabel;
	private Object  newLoadedObject;
	
	public String getListNameToBeChanged() {
		return listNameToBeChanged;
	}

	public void setListNameToBeChanged(String listNameToBeChanged) {
		this.listNameToBeChanged = listNameToBeChanged;
	}

	public String getAppendedObjectLabel() {
		return appendedObjectLabel;
	}

	public void setAppendedObjectLabel(String appendedObjectLabel) {
		this.appendedObjectLabel = appendedObjectLabel;
	}

	
	public Object getNewLoadedObject() {
		return newLoadedObject;
	}

	public void setNewLoadedObject(Object newLoadedObject) {
		this.newLoadedObject = newLoadedObject;
	}

	@Override
	public void apply(GridElement anElement, Grid grid) throws Exception {
		Field aField	=	anElement.getClass().getDeclaredField(this.listNameToBeChanged);
		aField.setAccessible(true);
		Object myList	=	aField.get(anElement);
		if(myList instanceof List){
			List	aList						=	(List)myList;
			HashMap<String,GridElement>	elMap	=	grid.obtainAllEmbeddedElements();
			if(elMap.containsKey(this.appendedObjectLabel)){
				GridElement	element	=	elMap.get(this.appendedObjectLabel);
				aList.add(element);
			}
			else if(this.newLoadedObject!=null){	//i have to append a new object
				aList.add(this.newLoadedObject);
			}
			else{
				throw new Exception("Object to be added not found in current Grid");	
			}
		}
		else{
			throw new Exception("Trying to make list modification to a non list object");
		}
	}

	@Override
	public String toString() {
		return "ListAppend: append this element "+this.appendedObjectLabel+"in this list "+this.listNameToBeChanged+" in this object "+this.subjectLabel;
	}

}
