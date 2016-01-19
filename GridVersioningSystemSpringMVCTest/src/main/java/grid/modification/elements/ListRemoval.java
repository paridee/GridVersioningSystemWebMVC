package grid.modification.elements;

import grid.entities.Grid;
import grid.entities.GridElement;

public class ListRemoval extends GridElementModification {
	
	private String 	listNameToBeChanged;
	private String	removedObjectLabel;
	
	public String getSubjectLabel() {
		return subjectLabel;
	}
	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}
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
		
	}
	@Override
	public String toString() {
		return "ListRemoval: remove element with label "+this.removedObjectLabel+" from list "+this.listNameToBeChanged+" in Grid element "+this.subjectLabel;
	}
	
}
