package grid.interfaces.services;

import java.util.List;

import grid.entities.Project;

/**
 * Interface defining methods for a Project service, following CRUD operations and more
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

public interface ProjectService {
	/**
	 * Lists all the projects available on persistence
	 * @return project list
	 */
	public List<Project> listProjects();
	
	/**
	 * Returns a project from its id 
	 * @param id id of the project
	 * @return project found
	 */
	public Project getProjectById(int id);
	
	/**
	 * Returns a project with a specific project id
	 * @param id id of the project
	 * @return a project
	 */
	public Project getProjectByProjectId(String id);

}
