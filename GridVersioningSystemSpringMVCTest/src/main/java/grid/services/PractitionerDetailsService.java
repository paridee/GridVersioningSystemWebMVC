package grid.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import grid.entities.Practitioner;
import grid.entities.UserRole;
import grid.interfaces.services.PractitionerService;

public class PractitionerDetailsService implements UserDetailsService {

	private PractitionerService practitionerService;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(final String username) 
               throws UsernameNotFoundException {

		System.out.println("carico utente "+username+" test");
		Practitioner					 user 		=	null;
		try{
				 user 	= 	practitionerService.getPractitionerByEmail(username);
		}
		catch(Exception e){
			//System.out.println("eccezione");
			e.printStackTrace();
		}
		//System.out.println("ottenuto utente "+username);	
		Set<UserRole> roles	=	user.getUserRole();
		for(UserRole role:roles){
			System.out.println("ruolo "+role.getRole());
		}
		List<GrantedAuthority> authorities = buildUserAuthority(roles);

		return buildUserForAuthentication(user, authorities);
		

	}

	// Converts com.mkyong.users.model.User user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(Practitioner user, 
		List<GrantedAuthority> authorities) {
		return new User(user.getEmail(), 
			user.getPassword(), true, 
                        true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (UserRole userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}

	public PractitionerService getPractitionerService() {
		return practitionerService;
	}

	public void setPractitionerService(PractitionerService practitionerService) {
		this.practitionerService = practitionerService;
	}



}