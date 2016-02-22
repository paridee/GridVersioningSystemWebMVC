package grid.interfaces.DAO;

import java.util.List;

import grid.entities.Practitioner;

/**
 * DAO for a Practitioner of the grid, manages all CRUD operations
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public interface PractitionerDAO {
	/**
	 * returns all the practitioners present in this system
	 * @return practitioners list
	 */
	public List<Practitioner> 	getAllPractitioners();
	
	/**
	 * returns a list of practitioners with that name
	 * @param name name of the practitioner
	 * @return practitioners
	 */
	public List<Practitioner> 	getPractitionersByName(String name);
	
	/**
	 * returns a practitioner identified by an email address
	 * @param email
	 * @return a practitioner
	 */
	public Practitioner			getPractitionerByEmail(String email);
	
	/**
	 * updates a practitioner
	 * @param p practitioner to be updated
	 */
	public void 				updatePractitioner(Practitioner p);
	
	/**
	 * delete a practitioner
	 * @param p practitioner to be deleted
	 */
	public void					delete(Practitioner p);
	
	/**
	 * add a practitioner
	 * @param p practitioner to be added
	 */ 
	public void					add(Practitioner p);
}
