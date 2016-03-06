package grid.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import grid.entities.Practitioner;
import grid.entities.Project;
import grid.interfaces.DAO.PractitionerDAO;
import grid.interfaces.services.GridService;
import grid.interfaces.services.PractitionerService;
import grid.interfaces.services.ProjectService;

public class PractitionerServiceImpl implements PractitionerService {
	PractitionerDAO practitionerDAO;
	private ProjectService 			projectService;
	private GridService 				gridService;
	
	
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}

	
	
	/**
	 * sets a DAO for this service
	 * @param practitionerDAO DAO to be set
	 */
	public void setPractitionerDAO(PractitionerDAO practitionerDAO) {
		this.practitionerDAO = practitionerDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Practitioner> getAllPractitioners() {
		return this.practitionerDAO.getAllPractitioners();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Practitioner> getPractitionersByName(String name) {
		return this.practitionerDAO.getPractitionersByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Practitioner getPractitionerByEmail(String email) {
		return this.practitionerDAO.getPractitionerByEmail(email);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updatePractitioner(Practitioner p) {
		this.practitionerDAO.updatePractitioner(p);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void delete(Practitioner p) {
		this.practitionerDAO.delete(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void add(Practitioner p) {
		this.practitionerDAO.add(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Project> getProjectsForPractitioner(Practitioner pr){
		List<Project> temp = this.projectService.listProjects();
		List<Project> available=new ArrayList<Project>();
	    for(Project prj:temp){
	    	List<Practitioner> practs=this.gridService.getInvolvedPractitioners(prj.getId(), true);
			if(practs.contains(pr)){
				available.add(prj);
			}
	    }
	    return available;
	}

}
