package grid.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import grid.modification.ObjectModification;


/**
 * This abstract class contains all the common elements among all elements belonging to every element of the Grid
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class GridElement {
	
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
}
