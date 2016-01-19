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
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.MapChange;

import grid.entities.GridElement;

public class ObjectModificationService {
	public static ArrayList<GridElementModification> getModification(GridElement oldElement, GridElement newElement) throws Exception{
		if(!(oldElement.getClass().equals(newElement.getClass()))){
			throw new Exception("Objects of different class");
		}
		if(!(oldElement.getLabel().equals(newElement.getLabel()))){
			throw new Exception("Objects with different labels");
		}
		ArrayList<GridElementModification> modifications	=	new ArrayList<GridElementModification>();
		Javers 					javers			=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		Diff 					diff			=	javers.compare(oldElement, newElement);
		List<Change> changes	=	diff.getChanges();
		System.out.println("DIFF "+diff);
		for(int i=0;i<changes.size();i++){
			Change current	=	changes.get(i);
			System.out.println(current.getClass().getName()+" "+current.getAffectedObject().get());
			if(current.getClass().equals(ValueChange.class)){
				ValueChange 		thisChange	=	(ValueChange)current;
				GridElement 		changed		=	(GridElement)thisChange.getAffectedObject().get();
				if(!thisChange.getPropertyName().equals("idElement")){
					ObjectFieldModification 	thisMod		=	new ObjectFieldModification();
					thisMod.setSubjectLabel(changed.getLabel());
					thisMod.setFieldToBeChanged(thisChange.getPropertyName());
					thisMod.setNewValue(thisChange.getRight());
					modifications.add(thisMod);
				}
			}
			else if(current.getClass().equals(ListChange.class)){
				ListChange thisChange	=	(ListChange)current;
				List<ContainerElementChange>	listchanges		=	thisChange.getChanges();	
				System.out.println("cambio a livello lista tipo "+thisChange.getChanges()+" prop "+thisChange.getPropertyName()+" object "+thisChange.getAffectedObject().get());
				Field field;
				Object subject	=	thisChange.getAffectedObject().get();
				field = subject.getClass().getDeclaredField(thisChange.getPropertyName());
				field.setAccessible(true);
				List 	oldList 	= 	(List)field.get(oldElement);
				HashMap		<String,Object>	oldListMap	=	new HashMap<String,Object>();
				List 	newList 	= 	(List)field.get(newElement);
				HashMap		<String,Object>	newListMap	=	new HashMap<String,Object>();
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
				diff			=	javers.compare(oldListMap, newListMap);
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
								append.setListNameToBeChanged(thisChange.getPropertyName());
								append.setSubjectLabel(oldElement.getLabel());
								modifications.add(append);
							}
							else if(entryChange.getClass().equals(EntryRemoved.class)){
								EntryRemoved 	thisRem	=	(EntryRemoved)entryChange;
								ListRemoval		remove	=	new ListRemoval();
								remove.setRemovedObjectLabel(thisRem.getKey().toString());
								remove.setListNameToBeChanged(thisChange.getPropertyName());
								remove.setSubjectLabel(oldElement.getLabel());
								modifications.add(remove);
							}
						}
					}
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
}
