package grid.interfaces.services;

import java.util.List;

import grid.entities.Project;

public interface ProjectService {
	public List<Project> listProjects();
	public Project getProjectById(int id);
	public Project getProjectByProjectId(String id);

}
