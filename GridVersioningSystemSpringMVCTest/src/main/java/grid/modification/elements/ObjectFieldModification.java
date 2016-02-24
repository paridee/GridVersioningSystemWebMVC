package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import grid.entities.Grid;
import grid.entities.GridElement;

/**
 * This class models a value change in a simple attribute on a Grid Element
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public class ObjectFieldModification extends GridElementModification {
	
	private String 	fieldToBeChanged;
	private Object	newValue;

	/**
	 * Gets the name of the property involved by this change
	 * @return name of the field to be changed
	 */
	public String getFieldToBeChanged() {
		return fieldToBeChanged;
	}
	
	/**
	 * Sets the name of the field to be changed
	 * @param fieldToBeChanged name of the field
	 */
	public void setFieldToBeChanged(String fieldToBeChanged) {
		this.fieldToBeChanged = fieldToBeChanged;
	}
	
	/**
	 * Gets the new value to be set on a field
	 * @return new value
	 */
	public Object getNewValue() {
		return newValue;
	}
	
	/**
	 * Set a new value to be set
	 * @param newValue new value to be set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(GridElement anElement,Grid aGrid) throws Exception{
		//check if is a superclass field
		Field[] fields						=	GridElement.class.getDeclaredFields();
		HashMap<String,Field>	fieldsMap	=	new HashMap<String,Field>();
		for(int i=0;i<fields.length;i++){
			fieldsMap.put(fields[i].getName(), fields[i]);
		}
		if(fieldsMap.containsKey(this.fieldToBeChanged)){
			Field	aField	=	fieldsMap.get(this.fieldToBeChanged);
			aField.setAccessible(true);
			if(newValue instanceof GridElement){
				System.out.println("rimpiazzo in elemento grid "+anElement.getLabel()+" attributo "+this.fieldToBeChanged+" con elemento "+((GridElement)newValue).getLabel());
				GridElement newV	=	(GridElement)newValue;
				if(aGrid.obtainAllEmbeddedElements().containsKey(newV.getLabel())){
					newValue	=	aGrid.obtainAllEmbeddedElements().get(newV.getLabel());
				}
				else{
					//if object not belonging to grid rebuild links
					newValue	=	((GridElement) newValue).clone();
					HashMap<String, GridElement> elements	=	aGrid.obtainAllEmbeddedElements();
					Iterator<String> it	=	elements.keySet().iterator();
					while(it.hasNext()){
						((GridElement) newValue).updateReferences(elements.get(it.next()), false, false);
					}
				}
			}
			aField.set(anElement, newValue);
		}
		//is not a superclass field, check if is subclass field
		else{
			fields		=	anElement.getClass().getDeclaredFields();
			fieldsMap	=	new HashMap<String,Field>();
			for(int i=0;i<fields.length;i++){
				fieldsMap.put(fields[i].getName(), fields[i]);
			}
			if(fieldsMap.containsKey(this.fieldToBeChanged)){
				Field	aField	=	fieldsMap.get(this.fieldToBeChanged);
				aField.setAccessible(true);
				aField.set(anElement, newValue);
			}
			else{
				throw new Exception("Field not found "+this.fieldToBeChanged);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ObjectFieldModification on label "+this.subjectLabel+" field "+this.fieldToBeChanged+" new value "+this.newValue;
	}
}
