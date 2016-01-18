package grid.interfaces.DAO;

import java.util.List;

import grid.entities.Project;

 
public interface ProjectDAO {
 
    public List<Project> listProjects();
    public Project getProjectById(int id);
	public Project getProjectByProjectId(String id);
}