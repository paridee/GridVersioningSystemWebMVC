package grid.interfaces.services;

import java.util.List;

import grid.entities.DefaultResponsible;
import grid.entities.Project;

public interface DefaultResponsibleService {
	/**
	 * returns all the default responsible present in this system
	 * @return responsible list
	 */
	public List<DefaultResponsible> 	getAllResponsibles();
	
		
	/**
	 * returns a responsible of that project with a specific role
	 * @param classN name or "pm" if looking for default project manager
	 * @return responsibles
	 */
	public DefaultResponsible	getResponsibleByClassName(String classN);
	
	/**
	 * updates a responsible
	 * @param p responsible to be updated
	 */
	public void 				updateDefaultResponsible(DefaultResponsible p);
	
	/**
	 * delete a default responsible
	 * @param p responsible to be deleted
	 */
	public void					delete(DefaultResponsible p);
	
	/**
	 * add a responsible
	 * @param p responsible to be added
	 */ 
	public void					add(DefaultResponsible p);
}
