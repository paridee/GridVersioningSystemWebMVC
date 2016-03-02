package grid.interfaces.services;

import java.util.List;
import grid.entities.Practitioner;
import grid.entities.Project;

/**
 * Interface defining methods for a Practitioner service, following CRUD operations and more
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

public interface PractitionerService {
	/**
	 * Returns all the practitioners available in the system
	 * @return practitioners list
	 */
	public List<Practitioner> getAllPractitioners();
	
	/**
	 * Returns all the practitioners matching a name
	 * @param name name to be searched
	 * @return practitioners list
	 */
	public List<Practitioner> getPractitionersByName(String name);
	
	/**
	 * Returns practitioner matching a mail
	 * @param email email of the practitioner
	 * @return practitioner matching
	 */
	public Practitioner getPractitionerByEmail(String email);
	
	/**
	 * Updates a practitioner on DB
	 * @param p practitioner to be updated
	 */
	public void updatePractitioner(Practitioner p);
	
	/**
	 * Deletes a practitioner from DB
	 * @param p practitioner to be deleted
	 */
	public void delete(Practitioner p);
	
	/**
	 * Add a practitioner on DB
	 * @param p practitioner to be added
	 */
	public void add(Practitioner p);
	
	/**
	 * Get projects where practitioner is involved
	 * @param p practitioner 
	 */
	public List<Project> getProjectsForPractitioner(Practitioner pr);
}
