package grid.services;

import java.util.List;

import grid.entities.Project;
import grid.interfaces.DAO.ProjectDAO;
import grid.interfaces.services.ProjectService;

public class ProjectServiceImpl implements ProjectService{
	
	private ProjectDAO projectDAO;
	 
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

	@Override
	public List<Project> listProjects() {
		return this.projectDAO.listProjects();
	}
	
	@Override
	public Project getProjectById(int id) {
		return this.projectDAO.getProjectById(id);
	}

	@Override
	public Project getProjectByProjectId(String id) {
		return this.projectDAO.getProjectByProjectId(id);
	}
	

}
