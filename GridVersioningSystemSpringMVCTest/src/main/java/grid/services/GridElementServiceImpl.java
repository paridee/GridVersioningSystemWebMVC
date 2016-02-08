package grid.services;

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
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grid.entities.GridElement;
import grid.entities.MeasurementGoal;
import grid.entities.Practitioner;
import grid.entities.Project;
import grid.interfaces.DAO.GridElementDao;
import grid.interfaces.services.GridElementService;
/**
 * Service managing Grid Elements DAO
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
@Service
@Transactional
public class GridElementServiceImpl implements GridElementService {

	private GridElementDao gridElementDao;
	
	/**
	 * Sets a DAO choosing persistence layer for Grid Elements
	 * @param gDAO DAO
	 */
	public void setGridElementDAO(GridElementDao gDAO) {
		this.gridElementDao = gDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void addGridElement(GridElement e) {
		this.gridElementDao.addGridElement(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateGridElement(GridElement e) {
		this.gridElementDao.updateGridElement(e);	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public GridElement upgradeGridElement(GridElement e) {
		GridElement upgraded	=	e.clone();
		upgraded.setVersion(upgraded.getVersion()+1);
		this.gridElementDao.addGridElement(upgraded);
		return upgraded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> listElement(Class<?> c) {
		return this.gridElementDao.listElement(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridElement getElementById(int id, String type) {
		return this.gridElementDao.getElementById(id, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> getElementLog(String label, String table) {
		return this.gridElementDao.getElementLog(label, table);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void removeElement(int id, Class<?> c) {
		this.gridElementDao.removeElement(id, c);
		
	}

	
	//TODO check if is correct due to strange JAVERS behaviour (for example # etc...)
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAddUpdate(GridElement oldElement, GridElement newElement) {
		Javers javers				=	JaversBuilder.javers().registerValueObject(GridElement.class).build();
		Diff diffElem				=	javers.compare(oldElement,newElement);
		List<Change> elementChanges	=	diffElem.getChanges();
		for(int i=0;i<elementChanges.size();i++){
			Change thisChange	=	elementChanges.get(i);
			System.out.println("Change type "+thisChange.getClass());
			//there is a value change in an attribute
			if(thisChange.getClass().equals(ValueChange.class)){
				System.out.println("Value change detected");
				ValueChange thisValueChange	=	(ValueChange) thisChange;
				try {
					System.out.println("left "+thisValueChange.getLeft()+" right "+thisValueChange.getRight());
					if(thisValueChange.getLeft().getClass().equals(String.class)){
						String oldS	=	(String)thisValueChange.getLeft();
						String newS	=	(String)thisValueChange.getRight();
						System.out.println(newS+" e' upgrade di "+oldS+"?");
						if(!newS.startsWith(oldS)){
							System.out.println(newS+"non e' upgrade di "+oldS);
							return false;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//there is a change on a list, check if the new one contains, at least, all the referenced objects of the old one
			else if(thisChange.getClass().equals(ListChange.class)&&(!thisChange.getAffectedGlobalId().value().contains("#"))){ //not a change in a linked object
				System.out.println("List change detected: "+thisChange.toString()+" <-");
				ListChange	thisListChange	=	(ListChange) thisChange;
				Field listField;
				try {
					System.out.println("OLD ELEMENT CLASS "+oldElement.getClass()+" FIELD "+thisListChange.getPropertyName());
					listField = oldElement.getClass().getDeclaredField(thisListChange.getPropertyName());
					listField.setAccessible(true);
					List<?> 	oldList		=	(List<?>)listField.get(oldElement);
					HashMap <String,GridElement>	oldMap	=	new HashMap<String, GridElement>();
					List<?> 	newList		=	(List<?>)listField.get(newElement);
					HashMap <String,GridElement>	newMap	=	new HashMap<String, GridElement>();
					for(int j=0;j<oldList.size();j++){
						oldMap.put(((GridElement)oldList.get(j)).getLabel(), (GridElement)oldList.get(j));
					}
					for(int j=0;j<newList.size();j++){
						newMap.put(((GridElement)newList.get(j)).getLabel(), (GridElement)newList.get(j));
					}
					Diff elementDiff			=	javers.compare(oldMap,newMap);
					System.out.println("Element comparison result "+elementDiff);
					Set<String> oldKeySet	=	oldMap.keySet();
					Iterator<String> anIterator	=	oldKeySet.iterator();
					while(anIterator.hasNext()){
						if(!newMap.containsKey(anIterator.next())){
							return false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> getElementByLabelAndState(Project prj, String subjLabel, Class<? extends GridElement> class1,GridElement.State aState) {
		ArrayList<GridElement> pendingElement	=	new ArrayList<GridElement>();
		List<GridElement> allElements			=	this.gridElementDao.getElementLog(subjLabel, class1.getSimpleName());
		for(GridElement el : pendingElement){
			if(el.getState()==aState){
				pendingElement.add(el);
			}
		}
		return pendingElement;
		
	}
	public List<GridElement> getElementByState(Project prj, Class<? extends GridElement> class1,GridElement.State state){
		
		return null;
	}

	
}
