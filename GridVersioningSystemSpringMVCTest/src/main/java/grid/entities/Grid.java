package grid.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.GridElement.State;
import grid.interfaces.Updatable;

/**
 * This class models a Grid entity with all Its attributes
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

@Entity
@Table(name="Grid")
public class Grid implements Updatable{
	/**
	 * Enumeration of possible grid states
	 * @author Paride Casulli
	 * @author Lorenzo La Banca	
	 */
	public enum GridState{
		WORKING,UPDATING,FINAL_KO
	}
	private int 		id;
	private	int 		version=1; 
	private List<Goal> 	mainGoals				=	null;
	private Project		project;
	private boolean		mainGoalsChanged		=	false;
	private static final Logger logger = LoggerFactory.getLogger(Grid.class);
	//TODO mancano attributi della grid di Serena e Marco
	
	/**
	 * Getter method for ID generated by DB, if 0 still not saved on persistence
	 * @return id of this object
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}

	/**
	 * Setter method for object ID, should not be used manually
	 * @param id ID to be set
	 */
	public void setId(int id) {
		this.id = id;
	}	
	
	/**
	 * Getter method for Grid version
	 * @return Grid version int
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Setter method for Grid version
	 * @param version to be set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Getter for root goals for this Grid
	 * @return list of root goals
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(	name = "GridToRootGoal", 
				joinColumns 		= 	{ 
						@JoinColumn(name		=	"gridID", 
									nullable 	= 	false, 
									updatable 	= 	false) 
						}, 
				inverseJoinColumns 	= 	{ 
						@JoinColumn(name 		= 	"goalID", 
									nullable 	= 	false, 
									updatable 	= 	false) 
						})
	public List<Goal> getMainGoals() {
		return mainGoals;
	}

	/**
	 * Setter for main goals list for this Grid
	 * @param mainGoals
	 */
	public void setMainGoals(List<Goal> mainGoals) {
		this.mainGoals = mainGoals;
	}

	/**
	 * Getter method for project owning this Grid
	 * @return project owner
	 */
	@ManyToOne(	fetch	=	FetchType.LAZY,
				cascade = 	CascadeType.PERSIST)
	@JoinColumn(name	=	"projID")
	public Project getProject() {
		return project;
	}

	/**
	 * Setter for owning project
	 * @param project project owning this Grid
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Returns a boolean check for main goals changed
	 * @return main goal change state
	 */
	public boolean isMainGoalsChanged() {
		return mainGoalsChanged;
	}

	/**
	 * Set to true if main goal list changed
	 * @param mainGoalsChanged new main goal change state
	 */
	public void setMainGoalsChanged(boolean mainGoalsChanged) {
		this.mainGoalsChanged = mainGoalsChanged;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> updateReferences(GridElement ge,boolean autoupgrade) {
		// nothing to do
		
		logger.info("do not update references from here, use main goal list");
		return null;
	}
	
	/**
	 * Returns all the embedded elements belonging to this Grid
	 * @return hashmap with all the grid elements within
	 */
	public HashMap<String, GridElement> obtainAllEmbeddedElements() {
		HashMap<String,GridElement> returnMap	=	new HashMap<String,GridElement>();
		for(int i=0;i<this.getMainGoals().size();i++){
			returnMap.putAll(this.getMainGoals().get(i).obtainEmbeddedElements());
		}
		return returnMap;
	}

	/**
	 * Obtain the current state for this grid
	 * @return state
	 */
	public GridState obtainGridState(){
		HashMap<String,GridElement>	allElements	=	new HashMap<String,GridElement>();
		for(int i=0;i<this.mainGoals.size();i++){
			allElements.putAll(this.mainGoals.get(i).obtainEmbeddedElements());
		}
		GridState		returnState	=	GridState.WORKING;
		Set<String>	keySet		=	allElements.keySet();
		Iterator<String> anIterator	=	keySet.iterator();
		while(anIterator.hasNext()){
			String key		=	anIterator.next();
			State aState	=	allElements.get(key).getState();
			if(aState	==	State.FINAL_KO){
				return GridState.FINAL_KO;
			}
			if((aState	==	State.MAJOR_UPDATING)||(aState	==	State.MINOR_CONFLICTING)||(aState	==	State.MAJOR_CONFLICTING)){
				returnState	=	GridState.UPDATING;
			}
		}
		return returnState;
	}
	
	/**
	 * Calls Grid Element custom toString
	 * @param prefix row prefix
	 * @param divider row divider
	 * @return string representation of the objecr
	 */
	public String toString(String prefix, String divider) {
		String returnString	=	prefix+"Grid "+divider;
		for(int i=0;i<this.mainGoals.size();i++){
			returnString	=	returnString+prefix+"main goal "+i+": "+this.mainGoals.get(i).toString(prefix,divider)+divider;
		}
		return returnString;
	}
	/**
	 * Clone method
	 * @return cloned object
	 */
	public Grid clone(){
		Grid cloned	=	new Grid();
		cloned.setProject(this.project);
		cloned.setVersion(this.version);
		cloned.setMainGoalsChanged(this.mainGoalsChanged);
		ArrayList<Goal> clonedMainGoals	=	new ArrayList<Goal>();
		for(int i=0;i<this.mainGoals.size();i++){
			clonedMainGoals.add(this.mainGoals.get(i));
		}
		cloned.setMainGoals(clonedMainGoals);
		return cloned;
	}

	@Override
	public ArrayList<GridElement> updateReferences(GridElement ge, boolean autoupgrade, boolean recursive) {
		return this.updateReferences(ge, autoupgrade);
	}
}
