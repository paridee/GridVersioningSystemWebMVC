package grid.modification.elements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.MapChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.Goal;
import grid.entities.GridElement;
import grid.entities.MeasurementGoal;
import grid.modification.grid.GridModificationService;

public class ObjectModificationService {
	private static final Logger logger = LoggerFactory.getLogger(ObjectModificationService.class);
	/**
	 * Gets the modifications between two GridElement
	 * @param oldElement older element
	 * @param newElement newer element
	 * @return differences 
	 * @throws Exception in case of problems
	 */
	public static ArrayList<GridElementModification> getModification(GridElement oldElement, GridElement newElement) throws Exception{
		if(!(oldElement.getClass().equals(newElement.getClass()))){
			throw new Exception("Objects of different class");
		}
		if(!(oldElement.getLabel().equals(newElement.getLabel()))){
			throw new Exception("Objects with different labels");
		}
		ArrayList<GridElementModification> modifications	=	new ArrayList<GridElementModification>();
		Javers 					javers			=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		logger.info("elements compared "+oldElement+","+newElement);
		Diff 					diff			=	javers.compare(oldElement, newElement);
		List<Change> changes	=	diff.getChanges();
		System.out.println("DIFF "+diff);
		for(int i=0;i<changes.size();i++){
			Change current	=	changes.get(i);
			System.out.println(current.getClass().getName()+" "+current.getAffectedObject().get());
			//TODO gestire se cambio measurement goal!!!
			if(current.getClass().equals(ValueChange.class)){
				ValueChange 		thisChange	=	(ValueChange)current;
				GridElement 		changed		=	(GridElement)thisChange.getAffectedObject().get();
				if((!(thisChange.getPropertyName().equals("idElement")||thisChange.getPropertyName().equals("logger")||thisChange.getPropertyName().equals("timestamp")||(thisChange.getPropertyName().equals("version")||(thisChange.getPropertyName().equals("state")))))&&(!thisChange.getAffectedGlobalId().value().contains("#"))){
					System.out.println("Adding modification on "+changed.getLabel()+" field "+thisChange.getPropertyName()+" old value "+thisChange.getLeft()+" new value "+thisChange.getRight());
					ObjectFieldModification 	thisMod		=	new ObjectFieldModification();
					thisMod.setSubjectLabel(changed.getLabel());
					thisMod.setFieldToBeChanged(thisChange.getPropertyName());
					thisMod.setNewValue(thisChange.getRight());
					modifications.add(thisMod);
				}
				if(thisChange.getAffectedGlobalId().value().contains("grid.entities.Goal/#measurementGoal")){//ad hoc solution for measurement goal
					System.out.println("cambio measurement goal");
					MeasurementGoal newMeasGoal	=	((Goal)newElement).getMeasurementGoal();
					MeasurementGoal oldMeasGoal	=	((Goal)oldElement).getMeasurementGoal();
					ObjectFieldModification 	thisMod		=	new ObjectFieldModification();
					thisMod.setSubjectLabel(oldElement.getLabel());
					thisMod.setFieldToBeChanged("measurementGoal");
					if(oldMeasGoal!=null){
						if(newMeasGoal!=null){
							if(!oldMeasGoal.getLabel().equals(newMeasGoal.getLabel())){
								thisMod.setNewValue(newMeasGoal);
								System.out.println("cambio measurement goal aggiunto in lista");
								modifications.add(thisMod);
							}
						}
						else{
							thisMod.setNewValue(null);
							modifications.add(thisMod);
						}
					}
				}
			}
			else if(current.getClass().equals(ListChange.class)){
				ListChange 	thisChange	=	(ListChange)current;
				String		listname	=	thisChange.getPropertyName();
				Field field;
				Object subject	=	thisChange.getAffectedObject().get();
				field = subject.getClass().getDeclaredField(thisChange.getPropertyName());
				field.setAccessible(true);
				if(!thisChange.getAffectedGlobalId().value().contains("#")){//excludes inner changes
					System.out.println(oldElement+" new "+newElement+" field "+field.getName()+" change "+current.toString());
					@SuppressWarnings("rawtypes")
					List 	oldList 	= 	(List)field.get(oldElement);
					@SuppressWarnings("rawtypes")
					List 	newList 	= 	(List)field.get(newElement);
					modifications.addAll(getListModification(oldList,newList,listname,oldElement.getLabel()));
					}
			}
		}
		//TODO remove: testing purpose
		System.out.println("my modifications:");
		for(int i=0;i<modifications.size();i++){
			System.out.println(modifications.get(i));
		}
		
		return modifications;
	}
	
	public static ArrayList<GridElementModification> getListModification(List oldList,List newList,String listname,String involvedObjlabel){
		HashMap		<String,Object>	oldListMap	=	new HashMap<String,Object>();
		HashMap		<String,Object>	newListMap	=	new HashMap<String,Object>();
		ArrayList<GridElementModification> modifications	=	new ArrayList<GridElementModification>();
		for(int j=0;j<oldList.size();j++){
			Object listElement	=	oldList.get(j);
			if(listElement instanceof GridElement){
				GridElement	thisElement	=	(GridElement) listElement;
				oldListMap.put(thisElement.getLabel(), thisElement);
			}
			else{
				oldListMap.put(listElement.toString(), listElement);
			}
		}
		for(int j=0;j<newList.size();j++){
			Object listElement	=	newList.get(j);
			if(listElement instanceof GridElement){
				GridElement	thisElement	=	(GridElement) listElement;
				newListMap.put(thisElement.getLabel(), thisElement);
			}
			else{
				newListMap.put(listElement.toString(), listElement);
			}
		}
		Javers 					javers			=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		Diff diff			=	javers.compare(oldListMap, newListMap);
		List<Change> changesInner	=	diff.getChanges();
		System.out.println("DIFF "+diff);
		for(int j=0;j<changesInner.size();j++){
			Change innerChange	=	changesInner.get(j);
			if(innerChange.getClass().equals(MapChange.class)){
				MapChange mapChange	=	(MapChange)innerChange;
				List<EntryChange> entryChanges	=	mapChange.getEntryChanges();
				for(int k=0;k<entryChanges.size();k++){
					EntryChange entryChange	=	entryChanges.get(k);
					if(entryChange.getClass().equals(EntryAdded.class)){
						EntryAdded thisAdd	=	(EntryAdded)entryChange;
						ListAppend append	=	new ListAppend();
						append.setAppendedObjectLabel(thisAdd.getKey().toString());
						append.setListNameToBeChanged(listname);
						append.setSubjectLabel(involvedObjlabel);
						if(thisAdd.getValue() instanceof GridElement){
							append.setNewLoadedObject(thisAdd.getValue());
						}
						modifications.add(append);
					}
					else if(entryChange.getClass().equals(EntryRemoved.class)){
						System.out.println("found removal on list "+listname+" element "+involvedObjlabel);
						EntryRemoved 	thisRem	=	(EntryRemoved)entryChange;
						ListRemoval		remove	=	new ListRemoval();
						remove.setRemovedObjectLabel(thisRem.getKey().toString());
						remove.setListNameToBeChanged(listname);
						remove.setSubjectLabel(involvedObjlabel);
						modifications.add(remove);
					}
				}
			}
		}
		return modifications;
	}
}
