package grid.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCrypt;

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
	protected String	password;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = 	CascadeType.PERSIST)
	private Set<UserRole> userRole = new HashSet<UserRole>(0);
	
	/**
	 * Returns practitioner name
	 * @return name name of the practitioner
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the practitioner name
	 * @param name name of the practitioner
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the practitioner email
	 * @return mail of the practitioner
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the practitioner email
	 * @param email email of the practitioner
	 */
	public void setEmail(String email) {
		this.email = email;
	}
		
	/**
	 * Returns the practitioner password
	 * @return a password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets a practitioner password
	 * @param password password to be set
	 */
	public void setPassword(String password) {
		BCrypt.hashpw(password, BCrypt.gensalt());
		this.password = password;
	}

	/**
	 * Gets roles (spring security) for this user
	 * @return roles
	 */
	public Set<UserRole> getUserRole() {
		return userRole;
	}
	
	/**
	 * Sets roles (spring security) for this user
	 * @param userRole roles
	 */
	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}
	
	/**
	 * Get id of the practitioner
	 * @return practitioner id
	 */
	public int getId() {
		return id;
	}
	
}
