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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_roles")
public class UserRole {
	private Integer userRoleId;
	private Practitioner user;
	private String role;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_role_id", 
		unique = true, nullable = false)
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", nullable = false)
	public Practitioner getUser() {
		return user;
	}
	public void setUser(Practitioner user) {
		this.user = user;
	}
	
	@Column(name = "role", nullable = false, length = 45)
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
