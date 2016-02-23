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
		
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}
	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}
	public int getId() {
		return id;
	}
	
}
