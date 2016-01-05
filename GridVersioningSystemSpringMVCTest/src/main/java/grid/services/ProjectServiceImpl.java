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
		// TODO Auto-generated method stub
		return this.projectDAO.listProjects();
	}

}
