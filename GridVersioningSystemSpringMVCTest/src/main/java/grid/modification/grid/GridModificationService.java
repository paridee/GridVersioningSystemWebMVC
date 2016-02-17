package grid.modification.grid;

import java.lang.reflect.Field;
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

import grid.Utils;
import grid.entities.Goal;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.GridElement.State;
import grid.entities.MeasurementGoal;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.interfaces.services.ConflictService;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import grid.modification.elements.GridElementModification;
import grid.modification.elements.Modification;
import grid.modification.elements.ObjectModificationService;

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
	private ProjectService		projectService;
	
	
	@Autowired(required=true)
	@Qualifier(value="projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

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
	 * Get modification on main goals list
	 * @param oldMainGoals older list
	 * @param newMainGoals newer list
	 * @return differences
	 */
	public static ArrayList<Modification> getMainGoalsModification(List<Goal> oldMainGoals,List<Goal> newMainGoals){
		ArrayList<Modification>	allMods		=	new ArrayList<Modification>();
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
		return allMods;
	}
	
	/**
	 * Calculate the differences between two grids
	 * @param oldGrid older grid
	 * @param newGrid newer grid
	 * @return differences
	 * @throws Exception in case of errors
	 */
	public static ArrayList<Modification> getModification(Grid oldGrid,Grid newGrid) throws Exception{
		ArrayList<Modification>	allMods		=	null;//new ArrayList<Modification>();
		//get main goals from both grids
		List<Goal>		oldMainGoals		=	oldGrid.getMainGoals();
		List<Goal>		newMainGoals		=	newGrid.getMainGoals();
		//put all these goals in a map
		allMods	=	GridModificationService.getMainGoalsModification(oldMainGoals,newMainGoals);
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
		Javers 					javers			=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		//take insertion and delections
		Diff diff					=	javers.compare(oldElementsMap, newElementsMap);
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
			if(minorNotConflict.size()>0){
				this.updateVersionNumbersAndStatus(latestGrid,newVersion);
				this.gridService.addGrid(newVersion);
			}
			else{
				newVersion.setVersion(newVersion.getVersion()-1);//if there are not minor updates not conflicting i have made no changes, restore previous version 
			}
			if(mods.size()>0){
				latestGrid	=	newVersion;
				newVersion	=	this.gridService.createStubUpgrade(newVersion);
				ArrayList<String> sentNotificationLabel	=	new ArrayList<String>();
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
						GridElement modified	=	newVersion.obtainAllEmbeddedElements().get(subjLabel);
						if(Modification.minorUpdateClass.contains(subj.getClass())){	//is a minor mod
							if(modifiedObjectLabels.contains(subjLabel)){				//if is in conflict
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
								modified.setState(State.MAJOR_CONFLICTING);
								this.logger.info("element in major conflict with an element of the current working grid");
							}
							else{
								List<GridElement> pending	=	this.gridElementService.getElementByLabelAndState( subjLabel,modified.getClass().getSimpleName(),GridElement.State.MAJOR_UPDATING);
								pending.addAll(this.gridElementService.getElementByLabelAndState(subjLabel, modified.getClass().getSimpleName(), GridElement.State.MAJOR_CONFLICTING));
								if(pending.size()==0){
									modified.setState(State.MAJOR_UPDATING);
								}
								else{
									modified.setState(State.MAJOR_CONFLICTING);
									for(GridElement el:pending){
										el.setState(State.MAJOR_CONFLICTING);
										this.gridElementService.updateGridElement(el);
									}
								}
							}
							if(modified.getIdElement()!=0){
								this.gridElementService.updateGridElement(modified);
							}
							else{
								this.gridElementService.addGridElement(modified);
							}
						}
						else return null;
						if(!sentNotificationLabel.contains(modified.getLabel())){
							this.sendGridElementNotification(modified,newVersion);
							sentNotificationLabel.add(modified.getLabel());
						}
					}
					else if(aMod instanceof GridModification){
						GridModification aGridModification	=	(GridModification)aMod;
						aGridModification.apply(newVersion);
						if(aMod instanceof GridElementAdd){
							GridElementAdd anAddition	=	(GridElementAdd)aMod;
							if(!sentNotificationLabel.contains(anAddition.getAppendedObjectLabel())){
								GridElement newElement	=	anAddition.getGridElementAdded();
								//newElement.setState(GridElement.State.MAJOR_UPDATING);
								this.sendGridElementNotification(newElement,newVersion);
								sentNotificationLabel.add(anAddition.getAppendedObjectLabel());
							}
						}
					}
					else{
						this.logger.info("manage this case "+aMod.toString());
					}
				}	
				this.updateVersionNumbersAndStatus(latestGrid,newVersion);
				this.gridService.addGrid(newVersion);
				if(newVersion.isMainGoalsChanged()){
					this.sendMainGoalChangeNotification(newVersion);
				}
				logger.info("mods summary");
				for(int i=0;i<mods.size();i++){
					logger.info(mods.get(i).toString());
				}
			}
			return newVersion;
		}
	}
	
	private void sendMainGoalChangeNotification(Grid newGrid){
		Practitioner pm	=	newGrid.getProject().getProjectManager();
		Utils.mailSender("GQM+S Versioning alert", "Dear "+pm.getName()+", the following Project: "+newGrid.getProject().getProjectId()+" had a main Goal change and requires an action, please check at the following link "+Utils.systemURL+"/MGLCResolution/"+newGrid.getId(),pm.getEmail());//+"/"+modified.getClass().getSimpleName()+"/"+modified.getIdElement(), responsibles.get(i).getEmail());
	}
	
	private void sendGridElementNotification(GridElement modified,Grid aGrid) {
		ArrayList<Practitioner> responsibles	=	new ArrayList<Practitioner>();
		if(Modification.minorUpdateClass.contains(modified.getClass())){
			responsibles.addAll(modified.getAuthors());
		}
		else{
			responsibles.add(aGrid.getProject().getProjectManager());
		}
		for(int i=0;i<responsibles.size();i++){
			if(responsibles.get(i)!=null){
				this.logger.info("sending email for "+modified.getLabel()+" to "+responsibles.get(i).getEmail());
				if(modified.getState().equals(GridElement.State.MINOR_CONFLICTING)){
					Utils.mailSender("GQM+S Versioning alert", "Dear "+responsibles.get(i).getName()+", the following Grid element: "+(modified.getClass().getSimpleName())+" "+modified.getLabel()+" is in state "+modified.getState()+" and requires an action, please check at the following link "+Utils.systemURL+"/GEResolution/"+modified.getClass().getSimpleName()+"/"+modified.getLabel(), responsibles.get(i).getEmail());
				}
				else if(modified.getState().equals(GridElement.State.MAJOR_UPDATING)){
					Utils.mailSender("GQM+S Versioning alert", "Dear "+responsibles.get(i).getName()+", the following Grid element: "+(modified.getClass().getSimpleName())+" "+modified.getLabel()+" is in state "+modified.getState()+" and requires an action, please check at the following link "+Utils.systemURL+"/GEResolution/"+modified.getClass().getSimpleName()+"/"+modified.getLabel(), responsibles.get(i).getEmail());
				}
				else if(modified.getState().equals(GridElement.State.MAJOR_CONFLICTING)){
					Utils.mailSender("GQM+S Versioning alert", "Dear "+responsibles.get(i).getName()+", the following Grid element: "+(modified.getClass().getSimpleName())+" "+modified.getLabel()+" is in state "+modified.getState()+" and requires an action, please check at the following link "+Utils.systemURL+"/GEResolution/"+modified.getClass().getSimpleName()+"/"+modified.getLabel(), responsibles.get(i).getEmail());
				}
			}
		}
		this.logger.info("#~#~NOTIFICATION STUB for item "+modified.getLabel()+" in state "+modified.getState());
	}

	private void updateVersionNumbersAndStatus(Grid latestGrid, Grid newVersion) {
		HashMap<String,GridElement> oldElements	=	latestGrid.obtainAllEmbeddedElements();
		HashMap<String,GridElement> newElements	=	newVersion.obtainAllEmbeddedElements();
		java.util.Iterator<String> anIt	=	newElements.keySet().iterator();
		int latestVersionNumber			=	this.gridService.getLatestGrid(newVersion.getProject().getId()).getVersion();
		if(latestVersionNumber>=newVersion.getVersion()){
			newVersion.setVersion(latestVersionNumber+1);
		}
		while(anIt.hasNext()){
			String key	=	anIt.next();
			this.logger.info("fixing version number element "+key);
			if(oldElements.containsKey(key)){
				this.logger.info("element "+key+" found in old set");
				GridElement oldElement 	=	oldElements.get(key);
				GridElement newElement 	=	newElements.get(key);
				if(newElement.getVersion()>oldElement.getVersion()){
					this.logger.info("element "+key+" fixing version from "+newElement.getVersion()+" to "+oldElement.getVersion()+1);
					newElement.setVersion(oldElement.getVersion()+1);
					//manages "on cascading update..." invalidates older version
					if((oldElement.getState()==GridElement.State.MAJOR_CONFLICTING)||(oldElement.getState()==GridElement.State.MAJOR_UPDATING)||(oldElement.getState()==GridElement.State.MINOR_CONFLICTING)){
						oldElement.setState(GridElement.State.ABORTED);
						this.gridElementService.updateGridElement(oldElement);
					}
				}
			}
			else{
				GridElement newElement 	=	newElements.get(key);
				int olderVersion		=	this.gridElementService.getLatestVersion(key, newElement.getClass().getSimpleName());
				if(olderVersion>0){
					//modifica richiesta da Lorenzo: se oggetto e' gia' presente nel DB ed e' di classe "Major" viene instanziato in major pending
					if(!Modification.minorUpdateClass.contains(newElement.getClass())){
						newElement.setState(GridElement.State.MAJOR_UPDATING);
					}
					newElement.setVersion(olderVersion+1);;
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
			newVersion	=	this.gridService.updateGridElement(newVersion, cloned,false,false);
			//if element was pending in previous version declare aborted
			if(subj.getState()==GridElement.State.MAJOR_CONFLICTING||subj.getState()==GridElement.State.MAJOR_UPDATING||subj.getState()==GridElement.State.MINOR_CONFLICTING){
				subj.setState(GridElement.State.ABORTED);
				this.gridElementService.updateGridElement(subj);
			}
		}
		return newVersion;
	}
	
	
	
	/**
	 * refreshes all the links in a Grid using all the latest working elements with a label //soluzione "sporca", aggiunta per modifiche al modello fatte da Lorenzo, serve per linkare grid element "Major" working che non sono linkati nella grid
	 * @param aGrid grid to be updated
	 * @return updated grid
	 */
	private Grid refreshLinks(Grid aGrid){
		aGrid	=	aGrid.clone();
		HashMap<String,GridElement> embeddedEl	=	aGrid.obtainAllEmbeddedElements();
		ArrayList<GridElement> updated			=	new ArrayList<GridElement>();
		Iterator<String> elements				=	embeddedEl.keySet().iterator();
		while(elements.hasNext()){
			String key			=	elements.next();
			GridElement current	=	embeddedEl.get(key);
			GridElement mostUp	=	this.gridElementService.getLatestWorking(key,current.getClass().getSimpleName());
			if(mostUp.getVersion()>=current.getVersion()){
				List<GridElementModification> mods;
				try {
					mods = ObjectModificationService.getModification(current, mostUp);	//check if most updated is different from current
					if(mods.size()>0){
						updated.add(mostUp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(GridElement el:updated){	//for each element in updated list
			el	=	el.clone();
			el.setVersion(el.getVersion()+1);
			Iterator<String> it	=	embeddedEl.keySet().iterator();
			while(it.hasNext()){		//restore links to current elements
				el.updateReferences(embeddedEl.get(it.next()), false, false);
			}
			this.gridService.updateGridElement(aGrid, el, false, false);
			embeddedEl	=	aGrid.obtainAllEmbeddedElements(); //updates with the current element
		}
		return aGrid;
	}

	/**
	 * Apply a modification to a GridElement to all projects
	 * @param newGridElement element to be updated
	 * @throws Exception
	 */
	public void applyAModificationToASingleElement(GridElement newGridElement) throws Exception{
		System.out.println("test");
		List<Project> 	projects	=	this.projectService.listProjects();
		for(Project p:projects){
			Grid latest	=	this.gridService.getLatestWorkingGrid(p.getId());
			try{
				System.out.println("test2");
				this.applyAModificationToASingleElement(latest, newGridElement);
			}
			catch(GridElementNotFoundInAGridException e){
				this.logger.info("skipping a grid, object not found");
			}
		}
		this.abortAllPending(newGridElement);
	}
	
	/**
	 * Apply a modification to a GridElement to a project
	 * @param aGrid grid to apply this project
	 * @param newGridElement new grid element
	 * @return
	 * @throws Exception
	 */
	public Grid applyAModificationToASingleElement(Grid aGrid,GridElement newGridElement) throws Exception{
		System.out.println("1");
		GridElement oldVersion	=	aGrid.obtainAllEmbeddedElements().get(newGridElement.getLabel());
		System.out.println("2");
		if(oldVersion == null){
			System.out.println("3");
			throw new GridElementNotFoundInAGridException("object not found in current Grid");
		}
		else{
			System.out.println("4");
			Grid updated	=	aGrid;
			List<Modification> mods	=	new ArrayList<Modification>();
			mods.addAll(ObjectModificationService.getModification(oldVersion, newGridElement));
			System.out.println("5");
			if(mods.size()>0){
				updated	=	this.gridService.createStubUpgrade(aGrid);
				for(Modification aMod : mods){
					this.logger.info("found modification "+aMod.toString());
					if(aMod instanceof GridElementModification){
						this.logger.info("apply modification "+aMod.toString());
						updated	=	this.applyAModification((GridElementModification)aMod, updated, updated.obtainAllEmbeddedElements());
					}
				}	
				this.updateVersionNumbersAndStatus(aGrid, updated);
				GridElement updatedEl	=	updated.obtainAllEmbeddedElements().get(newGridElement.getLabel());
				if(updatedEl!=null){
					updatedEl.setState(GridElement.State.WORKING);
				}
				updated	=	this.refreshLinks(updated);
				//TODO set right state for all elements
				this.gridService.addGrid(updated);
			}
			return updated;
		}
	}
	
	
	private void abortAllPending(GridElement newGridElement) {
		String label	=	newGridElement.getLabel();
		this.logger.info("aborting pending");
		List<GridElement> pending	=	this.gridElementService.getElementByLabelAndState(newGridElement.getLabel(), newGridElement.getClass().getSimpleName(), GridElement.State.MAJOR_CONFLICTING);
		pending.addAll(this.gridElementService.getElementByLabelAndState(newGridElement.getLabel(), newGridElement.getClass().getSimpleName(), GridElement.State.MAJOR_UPDATING));
		pending.addAll(this.gridElementService.getElementByLabelAndState(newGridElement.getLabel(), newGridElement.getClass().getSimpleName(), GridElement.State.MINOR_CONFLICTING));
		this.logger.info("aborting pending"+pending.toString());
		newGridElement.setState(GridElement.State.SOLVED);
		for(GridElement ge : pending){
			ge.setState(GridElement.State.SOLVED);
			this.logger.info("setting to solved the following element id "+ge.getIdElement()+" label "+ge.getLabel()+" version "+ge.getVersion());
			this.gridElementService.updateGridElement(ge);
		}
		
	}

	/**
	 * checks if is possible to apply a change to an element (there are not pending elements embedded)
	 * @param anElement
	 * @return result
	 */
	public boolean isEmbeddedPending(GridElement anElement){
		HashMap<String, GridElement> embedded	=	anElement.obtainEmbeddedElements();
		embedded.remove(anElement.getLabel());
		Iterator<String> iterator			=	embedded.keySet().iterator();
		while(iterator.hasNext()){
			GridElement anEmbedded		=	embedded.get(iterator.next());
			List<GridElement> elements	=	this.gridElementService.getElementByLabelAndState(anEmbedded.getLabel(), anEmbedded.getClass().getSimpleName(), GridElement.State.MAJOR_CONFLICTING);
			elements.addAll(this.gridElementService.getElementByLabelAndState(anEmbedded.getLabel(), anEmbedded.getClass().getSimpleName(), GridElement.State.MAJOR_UPDATING));
			elements.addAll(this.gridElementService.getElementByLabelAndState(anEmbedded.getLabel(), anEmbedded.getClass().getSimpleName(), GridElement.State.MINOR_CONFLICTING));
			if(elements.size()>0){
				return true;
			}
		}
		return false;
	}

	public void updateSingleElement(GridElement ge) {
		int lastVersion=this.gridElementService.getLatestVersion(ge.getLabel(), ge.getClass().getSimpleName());
		ge.setVersion(lastVersion+1);
		ge.setIdElement(0);
		ge.setState(GridElement.State.WORKING);
		//update link to working GE
		Field[] fields=ge.getClass().getDeclaredFields();
		for(int j=0; j<fields.length;j++){
			Field tempField=fields[j];
			tempField.setAccessible(true);
			try {
				Object fieldValue=tempField.get(ge);
				if(fieldValue instanceof GridElement){
					GridElement temp=(GridElement)fieldValue;
					GridElement active=this.gridElementService.getLatestWorking(temp.getLabel(), temp.getClass().getSimpleName());
					tempField.set(ge, active);
				}
				else if(fieldValue instanceof List){
					List myList 	=	(List)fieldValue;
					List<GridElement> newList=new ArrayList<GridElement>();
					List<GridElement> toConnect=new ArrayList<GridElement>();
					boolean gridElementList=false;
					if(myList.size()>0){
						Object	first	=	 myList.get(0);
						if(first instanceof GridElement){
							for(Object current:myList){
								GridElement currentGE=(GridElement)current;
								GridElement active=this.gridElementService.getLatestWorking(currentGE.getLabel(), currentGE.getClass().getSimpleName());   
								newList.add(active);
							}
							gridElementList=true;
						}
					}
					if(gridElementList){
						tempField.set(ge, newList);
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//save grid element
		this.gridElementService.addGridElement(ge);
		//for each project
			//for each working grid
				//clone Working Grid
				//update references to GE
		
		
		
		
		
		
		
				
				
				
			
			
			
	}
	
}
