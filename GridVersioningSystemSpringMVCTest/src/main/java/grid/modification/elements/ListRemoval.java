package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import grid.entities.Grid;
import grid.entities.GridElement;

public class ListRemoval extends GridElementModification {
	
	private String 	listNameToBeChanged;
	private String	removedObjectLabel;
	
	public String getListNameToBeChanged() {
		return listNameToBeChanged;
	}
	public void setListNameToBeChanged(String listNameToBeChanged) {
		this.listNameToBeChanged = listNameToBeChanged;
	}
	public String getRemovedObjectLabel() {
		return removedObjectLabel;
	}
	public void setRemovedObjectLabel(String removedObjectLabel) {
		this.removedObjectLabel = removedObjectLabel;
	}
	@Override
	public void apply(GridElement anElement, Grid aGrid) throws Exception {
		// TODO Auto-generated method stub
		Field aField	=	anElement.getClass().getDeclaredField(this.listNameToBeChanged);
		aField.setAccessible(true);
		Object myList	=	aField.get(anElement);
		if(myList instanceof List){
			List	aList						=	(List)myList;
			HashMap<String,GridElement>	elMap	=	anElement.obtainEmbeddedElements();
			if(elMap.containsKey(this.removedObjectLabel)){
				GridElement	element	=	elMap.get(this.removedObjectLabel);
				aList.remove(element);
			}
			else throw new Exception("Object to be added not found in current Grid");	
		}
		else{
			throw new Exception("Trying to make list modification to a non list object");
		}
	}
	@Override
	public String toString() {
		return "ListRemoval: remove element with label "+this.removedObjectLabel+" from list "+this.listNameToBeChanged+" in Grid element "+this.subjectLabel;
	}
	
}
