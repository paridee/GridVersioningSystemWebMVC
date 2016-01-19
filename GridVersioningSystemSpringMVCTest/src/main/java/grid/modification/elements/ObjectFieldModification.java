package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import grid.entities.Grid;
import grid.entities.GridElement;

public class ObjectFieldModification extends GridElementModification {
	
	private String 	fieldToBeChanged;
	private Object	newValue;
	public String getSubjectLabel() {
		return subjectLabel;
	}
	public void setSubjectLabel(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}
	public String getFieldToBeChanged() {
		return fieldToBeChanged;
	}
	public void setFieldToBeChanged(String fieldToBeChanged) {
		this.fieldToBeChanged = fieldToBeChanged;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	
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
				aField.set(anElement, newValue);
			}
			else{
				throw new Exception("Field not found "+this.fieldToBeChanged);
			}
		}
	}
	@Override
	public String toString() {
		return "ObjectFieldModification on label "+this.subjectLabel+" field "+this.fieldToBeChanged+" new value "+this.newValue;
	}
}
