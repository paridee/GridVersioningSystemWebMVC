package grid.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User role entity, used in spring security
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
@Entity
@Table(name = "user_roles")
public class UserRole {
	private Integer userRoleId;
	private Practitioner user;
	private String role;
	
	/**
	 * Get id for the user role
	 * @return id of the user role
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_role_id", 
		unique = true, nullable = false)
	public Integer getUserRoleId() {
		return userRoleId;
	}
	
	/**
	 * Set an Id for this user role
	 * @param userRoleId id to be set
	 */
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	/**
	 * Get the user of this user role
	 * @return user of this role instance
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", nullable = false)
	public Practitioner getUser() {
		return user;
	}
	
	/**
	 * Set an user for this role
	 * @param user user to be set for this role
	 */
	public void setUser(Practitioner user) {
		this.user = user;
	}
	
	/**
	 * Get a role for this user role
	 * @return role for this user role
	 */
	@Column(name = "role", nullable = false, length = 45)
	public String getRole() {
		return role;
	}
	
	/**
	 * Set a role for this user role instance
	 * @param role role to be set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
