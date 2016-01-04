package grid.entities;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Project managing a Grid
 */

@Entity
@Table(name="Project")
public class Project {
	
	private int 			id;
	private String 			projectId;
	private String 			description;
	private String 			creationDate;
	private	List<String> 	availableMeasUnits;
	
	/**
	 * Getter of project ID (primary key)
	 * @return id of the project
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}
	
	/**
	 * Setter for project ID (primary key), should not be used manually!
	 * @param id new ID for this project
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Getter for project (String) ID used in JSON
	 * @return id of this project
	 */
	public String getProjectId() {
		return projectId;
	}
	
	/**
	 * Setter for project ID
	 * @param projectId ID of this project, as written in JSON
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	/**
	 * Getter for project description
	 * @return description of this project
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set a description for this project
	 * @param description string to be ste
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets a creation date for this project
	 * @return string representation of creation date
	 */
	public String getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Sets a creation date for this project
	 * @param creationDate string representation of this project creation date
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Gets a list of measurement units for this project
	 * @return measurement units list
	 */
	@ElementCollection
	@CollectionTable(	name		=	"ProjectMeasUnits", 
						joinColumns	=	@JoinColumn(name="projID"))
	@Column(name="measUnit")
	public List<String> getAvailableMeasUnits() {
		return availableMeasUnits;
	}
	
	/**
	 * Sets a list of measurement units available for this project
	 * @param availableMeasUnits list of units to be set
	 */
	public void setAvailableMeasUnits(List<String> availableMeasUnits) {
		this.availableMeasUnits = availableMeasUnits;
	}
}
