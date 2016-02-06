package grid.modification.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.MapChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.GridElement.State;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectModificationService;
import grid.modification.grid.Conflict.Type;
import it.paridelorenzo.ISSSR.ModificationController;

/**
 * This class owns the task of calculating differences between two grids
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public class GridModificationService {
	private static final Logger logger = LoggerFactory.getLogger(GridModificationService.class);
	private GridElementService 	gridElementService;
	private GridService			gridService;
	private ConflictService		conflictService;
	
	@Autowired(required=true)
	@Qualifier(value="gridElementService")
	public void setGridElementService(GridElementService gridElementService) {
		this.gridElementService = gridElementService;
	}

	@Autowired(required=true)
	@Qualifier(value="gridService")
	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	
	@Autowired(required=true)
	@Qualifier(value="conflictService")
	public void setConflictService(ConflictService conflictService) {
		this.conflictService = conflictService;
	}
	
	/**
	 * Calculate the differences between two grids
	 * @param oldGrid older grid
	 * @param newGrid newer grid
	 * @return differences
	 * @throws Exception in case of errors
	 */
	public static ArrayList<Modification> getModification(Grid oldGrid,Grid newGrid) throws Exception{
		ArrayList<Modification>	allMods		=	new ArrayList<Modification>();
		//get main goals from both grids
		List<Goal>		oldMainGoals		=	oldGrid.getMainGoals();
		List<Goal>		newMainGoals		=	newGrid.getMainGoals();
		//put all these goals in a map
		HashMap<String,Goal> oldGoalsMap	=	new HashMap<String,Goal>();
		HashMap<String,Goal> newGoalsMap	=	new HashMap<String,Goal>();
		for(int i=0;i<oldMainGoals.size();i++){
			oldGoalsMap.put(oldMainGoals.get(i).getLabel(), oldMainGoals.get(i));
		}
		for(int i=0;i<newMainGoals.size();i++){
			newGoalsMap.put(newMainGoals.get(i).getLabel(), newMainGoals.get(i));
		}
		//find the differences with javers lib
		Javers 					javers			=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		Diff diff			=	javers.compare(oldGoalsMap, newGoalsMap);
		List<Change> changesInner	=	diff.getChanges();
		//for be safe, there will be (at most) 1 change of type MapChange
		for(int j=0;j<changesInner.size();j++){
			Change innerChange	=	changesInner.get(j);
			if(innerChange.getClass().equals(MapChange.class)){
				//gets all the changes in the map
				MapChange mapChange	=	(MapChange)innerChange;
				List<EntryChange> entryChanges	=	mapChange.getEntryChanges();
				for(int k=0;k<entryChanges.size();k++){
					EntryChange entryChange	=	entryChanges.get(k);
					//manage an entry insertion
					if(entryChange.getClass().equals(EntryAdded.class)){
						EntryAdded thisAdd	=	(EntryAdded)entryChange;
						MainGoalAdd append	=	new MainGoalAdd();
						append.setAppendedObject((Goal)thisAdd.getValue());
						allMods.add(append);
					}
					//manage an entry deletion
					else if(entryChange.getClass().equals(EntryRemoved.class)){
						EntryRemoved 	thisRem	=	(EntryRemoved)entryChange;
						MainGoalRemove	remove	=	new MainGoalRemove();
						remove.setRemovedObjectLabel(thisRem.getKey().toString());
						allMods.add(remove);
					}
				}
			}
		}

		//as before put all the embedded elements of the grid in a map
		HashMap<String,GridElement> oldElementsMap	=	oldGrid.obtainAllEmbeddedElements();
		HashMap<String,GridElement> newElementsMap	=	newGrid.obtainAllEmbeddedElements();
		Set<String>	keySet							=	oldElementsMap.keySet();
		Iterator<String> anIterator					=	keySet.iterator();
		//check the changes in the attributes of the elements belonging to the intersection of older and newer elements
		while(anIterator.hasNext()){
			String key	=	anIterator.next();
			if(newElementsMap.containsKey(key)){
				ArrayList<GridElementModification> someMods	=	(ObjectModificationService.getModification(oldElementsMap.get(key), newElementsMap.get(key)));
				System.out.println("####mods found for item "+key+" "+someMods.size());
				allMods.addAll(someMods);
			}
		}
		//take insertion and delections
		diff			=	javers.compare(oldElementsMap, newElementsMap);
		changesInner	=	diff.getChanges();
		System.out.println("DIFF "+diff);
		for(int j=0;j<changesInner.size();j++){
			Change innerChange	=	changesInner.get(j);
			if(innerChange.getClass().equals(MapChange.class)){
				MapChange mapChange	=	(MapChange)innerChange;
				List<EntryChange> entryChanges	=	mapChange.getEntryChanges();
				for(int k=0;k<entryChanges.size();k++){
					EntryChange entryChange	=	entryChanges.get(k);
					if(entryChange.getClass().equals(EntryAdded.class)){
						EntryAdded 		thisAdd	=	(EntryAdded)entryChange;
						GridElementAdd 	append	=	new GridElementAdd();
						append.setAppendedObjectLabel(thisAdd.getKey().toString());
						append.setGridElementAdded((GridElement) thisAdd.getValue());
						allMods.add(append);
					}
					else if(entryChange.getClass().equals(EntryRemoved.class)){
						EntryRemoved 		thisRem	=	(EntryRemoved)entryChange;
						GridElementRemove	remove	=	new GridElementRemove();
						remove.setRemovedObjectLabel(thisRem.getKey().toString());
						allMods.add(remove);
					}
				}
			}
		}
		return allMods;
	}
	
	public Grid applyModifications(List<Modification> mods,Grid latestGrid,ArrayList<String> modifiedObjectLabels) throws Exception{
		Grid supplied	=	latestGrid;		//will be used late after re-assignment
		for(int i=0;i<mods.size();i++){
			System.out.println("found modification "+mods.get(i).toString());
		}
		if(mods.size()==0){
			return null;
		}
		else{
			Grid newVersion	=	this.gridService.createStubUpgrade(latestGrid);
//			HashMap<String,GridElement> addedElements	=	new HashMap<String,GridElement>();
//			//saves all new items
//			for(int i=0;i<mods.size();i++){
//				if(mods.get(i) instanceof GridElementAdd){
//					GridElement insertion	=	((GridElementAdd)mods.get(i)).getGridElementAdded();
//					addedElements.put(insertion.getLabel(), insertion);
//				}
//			}
			ArrayList<GridElementModification>	minorNotConflict	=	new ArrayList<GridElementModification>();
			for(int i=0;i<mods.size();i++){
				HashMap<String,GridElement> elements	=	newVersion.obtainAllEmbeddedElements();
				Modification 	aMod	=	mods.get(i);
				GridElement 	subj;
				if(aMod instanceof GridElementModification){
					subj	=	elements.get(((GridElementModification) aMod).getSubjectLabel());
					logger.info("Grid Element Modification: involved class "+subj.getClass()+" label "+subj.getLabel()+" label in mod "+((GridElementModification) aMod).getSubjectLabel());
					//if is already in new grid use this one...
					if(newVersion.obtainAllEmbeddedElements().containsKey(((GridElementModification) aMod).getSubjectLabel())){
						subj	=	newVersion.obtainAllEmbeddedElements().get(((GridElementModification) aMod).getSubjectLabel());
					}
					if(Modification.minorUpdateClass.contains(subj.getClass())){
						if(!modifiedObjectLabels.contains(subj.getLabel())){
							minorNotConflict.add((GridElementModification)aMod);
						}
					}
				}
			}
			//remove from previous and apply
			for(int i=0;i<minorNotConflict.size();i++){
				HashMap<String,GridElement> elements	=	newVersion.obtainAllEmbeddedElements();
				newVersion	=	this.applyAModification(minorNotConflict.get(i),newVersion,elements);
				mods.remove(minorNotConflict.get(i));
			}
			this.updateVersionNumbers(latestGrid,newVersion);
			this.gridService.addGrid(newVersion);
			latestGrid	=	newVersion;
			newVersion	=	this.gridService.createStubUpgrade(newVersion);
			for(int i=0;i<mods.size();i++){
				HashMap<String,GridElement> elements	=	newVersion.obtainAllEmbeddedElements();
				Modification 	aMod	=	mods.get(i);
				this.logger.info(aMod.toString());
				if(aMod instanceof GridElementModification){
					GridElement 	subj;
					String subjLabel	=	((GridElementModification) aMod).getSubjectLabel();
					subj	=	elements.get(subjLabel);
					logger.info("Grid Element Modification: involved class "+subj.getClass()+" label "+subj.getLabel()+" label in mod "+((GridElementModification) aMod).getSubjectLabel());
					//if is already in new grid use this one...
					if(newVersion.obtainAllEmbeddedElements().containsKey(subjLabel)){
						subj	=	newVersion.obtainAllEmbeddedElements().get(subjLabel);
					}
					if(elements.containsKey(subjLabel)){
						this.applyAModification(((GridElementModification) aMod), newVersion, elements);
					}
					if(Modification.minorUpdateClass.contains(subj.getClass())){	//is a minor mod
						if(modifiedObjectLabels.contains(subjLabel)){				//if is in conflict
							GridElement modified	=	newVersion.obtainAllEmbeddedElements().get(subjLabel);
							modified.setState(State.MINOR_CONFLICTING);
							//TODO send email for conflict
							//Conflict aConflict	=	new Conflict();
							//ArrayList<GridElement> conflicting	= new ArrayList<GridElement>();
							//conflicting.add(modified);
							//aConflict.setConflictState(Conflict.State.PENDING);
							//aConflict.setConflictType(Type.MINOR);
							//this.conflictService.addConflict(aConflict);
						}
						
					}
					else if(!Modification.minorUpdateClass.contains(subj.getClass())){	//is a major conflict
						if(modifiedObjectLabels.contains(subjLabel)){
							GridElement modified	=	newVersion.obtainAllEmbeddedElements().get(subjLabel);
							modified.setState(State.MAJOR_CONFLICTING);
						}
						else{
							GridElement modified	=	newVersion.obtainAllEmbeddedElements().get(subjLabel);
							//TODO CHECK SCRITTO SUL FOGLIO modified.setState(State.MAJOR_UPDATING);
						}
						//Conflict aConflict	=	new Conflict();
						//ArrayList<GridElement> conflicting	= new ArrayList<GridElement>();
						//conflicting.add(modified);
						//TODO find other conflicting objects and update state o all other elements involved
						//aConflict.setConflictState(Conflict.State.PENDING);
						//aConflict.setConflictType(Type.MAJOR);
						//aConflict.setConflicting(conflicting);
						//this.conflictService.addConflict(aConflict);
					}
					else return null;
				}
				else if(aMod instanceof GridModification){
					GridModification aGridModification	=	(GridModification)aMod;
					aGridModification.apply(newVersion);
				}
				else{
					this.logger.info("manage this case "+aMod.toString());
				}
			}
			this.updateVersionNumbers(latestGrid,newVersion);
			this.gridService.addGrid(newVersion);
			logger.info("mods summary");
			for(int i=0;i<mods.size();i++){
				logger.info(mods.get(i).toString());
			}
			return newVersion;
		}
	}

	private void updateVersionNumbers(Grid latestGrid, Grid newVersion) {
		HashMap<String,GridElement> oldElements	=	latestGrid.obtainAllEmbeddedElements();
		HashMap<String,GridElement> newElements	=	newVersion.obtainAllEmbeddedElements();
		java.util.Iterator<String> anIt	=	newElements.keySet().iterator();
		while(anIt.hasNext()){
			String key	=	anIt.next();
			if(oldElements.containsKey(key)){
				GridElement oldElement 	=	oldElements.get(key);
				GridElement newElement 	=	newElements.get(key);
				if(newElement.getVersion()>oldElement.getVersion()){
					newElement.setVersion(oldElement.getVersion()+1);
				}
				else{
					newElement.setVersion(1);
				}
			}
		}
	}

	private Grid applyAModification(GridElementModification gridElementModification, Grid newVersion,HashMap<String,GridElement> elements) throws Exception {
		GridElement 	subj;
		subj	=	elements.get(gridElementModification.getSubjectLabel());
		logger.info("Grid Element Modification: involved class "+subj.getClass()+" label "+subj.getLabel()+" label in mod "+gridElementModification.getSubjectLabel());
		//if is already in new grid use this one...
		if(newVersion.obtainAllEmbeddedElements().containsKey(gridElementModification.getSubjectLabel())){
			subj	=	newVersion.obtainAllEmbeddedElements().get(gridElementModification.getSubjectLabel());
		}
		if(elements.containsKey(gridElementModification.getSubjectLabel())){
			GridElement cloned	=	subj.clone();
			cloned.setVersion(subj.getVersion()+1);
			gridElementModification.apply(cloned, newVersion);
			if(!Modification.minorUpdateClass.contains(cloned.getClass())){
				cloned.setState(State.MAJOR_UPDATING);
				logger.info("found major update");
			}
			else{
				logger.info("found minor update");
			}
			newVersion	=	this.gridService.updateGridElement(newVersion, cloned,false,false);
		}
		return newVersion;
	}
}
