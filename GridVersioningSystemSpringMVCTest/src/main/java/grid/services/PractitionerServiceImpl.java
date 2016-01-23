package grid.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import grid.entities.Practitioner;
import grid.interfaces.DAO.PractitionerDAO;
import grid.interfaces.services.PractitionerService;

public class PractitionerServiceImpl implements PractitionerService {
	PractitionerDAO practitionerDAO;
	
	/**
	 * sets a DAO for this service
	 * @param practitionerDAO DAO to be set
	 */
	public void setPractitionerDAO(PractitionerDAO practitionerDAO) {
		this.practitionerDAO = practitionerDAO;
	}

	@Override
	public List<Practitioner> getAllPractitioners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Practitioner> getPractitionersByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Practitioner getPractitionerByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public void updatePractitioner(Practitioner p) {
		// TODO Auto-generated method stub
		
	}
	@Transactional
	@Override
	public void delete(Practitioner p) {
		// TODO Auto-generated method stub
		
	}
	@Transactional
	@Override
	public void add(Practitioner p) {
		// TODO Auto-generated method stub
		
	}

}
