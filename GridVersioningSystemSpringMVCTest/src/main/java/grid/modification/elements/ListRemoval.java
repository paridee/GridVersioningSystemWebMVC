package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import grid.Utils;
import grid.entities.Grid;
import grid.entities.GridElement;

public class ListRemoval extends GridElementModification {
	
	private String 	listNameToBeChanged;
	private String	removedObjectLabel;
	private Object  objectToBeRemoved=null;
	
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
	public Object getObjectToBeRemoved() {
		return objectToBeRemoved;
	}
	public void setObjectToBeRemoved(Object objectToBeRemoved) {
		this.objectToBeRemoved = objectToBeRemoved;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void apply(GridElement anElement, Grid aGrid) throws Exception {
		Field aField	=	null;
		if(Utils.getFieldNamesForAClass(anElement.getClass()).contains(this.listNameToBeChanged)){				
			aField = anElement.getClass().getDeclaredField(this.listNameToBeChanged);
		}
		else if(Utils.getFieldNamesForAClass(anElement.getClass().getSuperclass()).contains(this.listNameToBeChanged)){
			aField = anElement.getClass().getSuperclass().getDeclaredField(this.listNameToBeChanged);
		}
		aField.setAccessible(true);
		Object myList	=	aField.get(anElement);
		if(myList instanceof List){
			List	aList						=	(List)myList;
			HashMap<String,GridElement>	elMap	=	anElement.obtainEmbeddedElements();
			if(elMap.containsKey(this.removedObjectLabel)){
				GridElement	element	=	elMap.get(this.removedObjectLabel);
				aList.remove(element);
			}
			else if(this.listNameToBeChanged.equals("authors")){
				//System.out.println("removing author "+this.objectToBeRemoved+" contained "+aList.contains(this.objectToBeRemoved)+" list size "+aList.size());
				aList.remove(this.objectToBeRemoved);
				//System.out.println("new list size "+aList.size());
			}
			else throw new Exception("Object to be removed not found in current Grid");	
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
