package grid.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import grid.interfaces.Updatable;

/**
 * This abstract class contains all the common elements among all elements belonging to every element of the Grid
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class GridElement implements Updatable{
	public enum State{
		WORKING, MAJOR_UPDATING, MINOR_UPDATING, FINAL_KO
	}
	protected List<Practitioner> authors	=	new ArrayList<Practitioner>();
	
	protected State 	state	=	State.WORKING;
	
	@Column(name 		= 	"label", 
			nullable 	= 	false, 
			length 		= 	20)
	protected String 	label;
	
	protected int 		idElement;	//primary key
	
	@Column(name 		= 	"version", 
			nullable 	= 	false, 
			length 		= 	20)
	protected int 	version=1;
	
	/**
	 * Returns a label for this Grid Entity (in Grid is named as ID, depending on the type of Element
	 * @return label of this element
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets a label for this element
	 * @param label to be set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Returns the element ID (primary key on DB)
	 * @return Grid element ID
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getIdElement() {
		return idElement;
	}
	
	/**
	 * sets an Id for this element
	 * @param idElement
	 */
	public void setIdElement(int idElement) {
		this.idElement = idElement;
	}
	
	/**
	 * Gets the version of this object
	 * @return int version
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * Sets a version for this object
	 * @param version new version of this object
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	/**
	 * Gets the state for this object
	 * @return state of this object
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the state for this object
	 * @return state to be set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Get the authors list for this element
	 * @return authors list
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(	name = "GridElementToAuthors", 
				joinColumns 		= 	{ 
						@JoinColumn(name 		=	"gridElementID", 
									nullable 	= 	false, 
									updatable 	= 	false)}, 
				inverseJoinColumns 	= 	{ 
						@JoinColumn(name 		= 	"author", 
									nullable 	= 	false, 
									updatable 	= 	false)}
			)
	public List<Practitioner> getAuthors() {
		return authors;
	}

	/**
	 * Set the authors list for this element
	 * @param authors authors to be set
	 */
	public void setAuthors(List<Practitioner> authors) {
		this.authors = authors;
	}

	/**
	 * Clones this object and creates a copy of references (doesn't copy the referenced objects!)
	 */
	public abstract GridElement clone();
	
	/**
	 * Returns a string representation of the object ruled by parameters
	 * @param prefix prefix before each level
	 * @param divider divider among rows
	 * @return string representation
	 */
	public abstract  String toString(String prefix,String divider);
	
	/**
	 * Equals, ignore version and checks all t other properties/attributes
	 */
	public abstract boolean equals(Object obj);
	
	/**
	 * Gets a map with all the elements embedded in this one
	 * @return hashmap of all included elements
	 */
	public abstract HashMap<String,GridElement> obtainEmbeddedElements();
}
