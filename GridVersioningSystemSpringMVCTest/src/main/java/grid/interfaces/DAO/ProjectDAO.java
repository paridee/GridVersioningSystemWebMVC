package grid.interfaces.DAO;

import java.util.List;

import grid.entities.Project;

 /**
  * DAO for the project entity
  * @author Paride Casulli
  *	@author Lorenzo La Banca
  */
public interface ProjectDAO {
 
	/**
	 * Get a list with all the projects available in this system
	 * @return
	 */
    public List<Project> listProjects();
    
    /**
     * Get a project with an Id (DB ID)
     * @param id id of the project
     * @return proper project
     */
    public Project getProjectById(int id);
    
    /**
     * get a project with a project ID
     * @param id project id of the project
     * @return proper project
     */
	public Project getProjectByProjectId(String id);
}