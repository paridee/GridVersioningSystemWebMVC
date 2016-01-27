package grid.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import grid.entities.Project;
import grid.interfaces.DAO.ProjectDAO;


public class ProjectDAOImpl implements ProjectDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectDAOImpl.class);
	 
    private SessionFactory sessionFactory;
     
    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }
 
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> listProjects() {
        Session session 			= 	this.sessionFactory.getCurrentSession();
        List<Project> projectsList 	=	session.createQuery("from Project").list();
        for(Project p : projectsList){
            logger.info("Project List::"+p);
        }
        return projectsList;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProjectById(int id) {
		Session currentSession	=	this.sessionFactory.getCurrentSession();
		Project	g				=	(Project) currentSession.load(Project.class,new Integer(id));
		logger.info(g.getClass().getName()+" loaded::"+g);
		return g;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Project getProjectByProjectId(String id) {
		Session session			=	this.sessionFactory.getCurrentSession();
		List<Project> returnres	=	session.createQuery("from Project P where P.projectId = \'"+id+"\'").list();
		if(returnres.size()==0){
			return null;
		}
		Project project	=	(Project) returnres.get(0) ;
		return project;
	}

}
