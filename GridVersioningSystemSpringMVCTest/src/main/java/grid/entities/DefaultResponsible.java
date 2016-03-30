package grid.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Default Responsible for this instance
 */
@Entity
@Table(name="DefaultResponsible")
public class DefaultResponsible {
	int				id;
	Practitioner	practitioner;
	String			className;
	
	/**
	 * Returns the id of the entry
	 * @return id
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}
	
	/**
	 * Sets an id for this entry (not to be manually invoked)
	 * @param id id to be set
	 */
	public void setId(int id) {
		this.id = id;
	}
		
	/**
	 * Get the practitioner that is responsible for this type
	 * @return practitioner
	 */
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name="practitioner")
	public Practitioner getPractitioner() {
		return practitioner;
	}
	
	/**
	 * Set a practitioner for this entry
	 * @param practitioner practitioner to be set
	 */
	public void setPractitioner(Practitioner practitioner) {
		this.practitioner = practitioner;
	}
	
	/**
	 * Get the class for this entry (default practitioner class)
	 * @return class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Set a class for this default practitioner
	 * @param className class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
}
