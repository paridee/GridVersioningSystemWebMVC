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

	@Override
	public String toString() {
		return "ListAppend: append this element "+this.appendedObjectLabel+"("+this.newLoadedObject+")in this list "+this.listNameToBeChanged+" in this object "+this.subjectLabel;
	}

}
