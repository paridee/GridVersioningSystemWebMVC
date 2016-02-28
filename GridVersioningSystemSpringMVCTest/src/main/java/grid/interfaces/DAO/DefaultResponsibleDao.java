package grid.interfaces.DAO;

import java.util.List;

import grid.entities.DefaultResponsible;
import grid.entities.Project;
import grid.entities.SubscriberPhase;

public interface DefaultResponsibleDao {
	/**
	 * returns all the default responsible present in this system
	 * @return responsible list
	 */
	public List<DefaultResponsible> 	getAllResponsibles();
	
	/**
	 * returns a list of responsibles of that project
	 * @param project a project
	 * @return responsibles
	 */
	public List<DefaultResponsible>	getResponsiblesByClassName(String className);
		
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
