package grid.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Practitioner (author) of a Grid or its element
 */

@Entity
@Table(name="Practitioner")
public class Practitioner {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	protected int 	id	=	0;
	protected String 	name;
	protected String 	email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
