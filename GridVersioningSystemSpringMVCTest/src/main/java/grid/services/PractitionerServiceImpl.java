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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Practitioner> getAllPractitioners() {
		return this.practitionerDAO.getAllPractitioners();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Practitioner> getPractitionersByName(String name) {
		return this.practitionerDAO.getPractitionersByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Practitioner getPractitionerByEmail(String email) {
		return this.practitionerDAO.getPractitionerByEmail(email);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updatePractitioner(Practitioner p) {
		this.practitionerDAO.updatePractitioner(p);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void delete(Practitioner p) {
		this.practitionerDAO.delete(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void add(Practitioner p) {
		this.practitionerDAO.add(p);
	}

}
