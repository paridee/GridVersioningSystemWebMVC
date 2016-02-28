package grid.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.DefaultResponsible;
import grid.entities.Project;
import grid.entities.SubscriberPhase;
import grid.interfaces.DAO.DefaultResponsibleDao;

public class DefaultResponsibleDaoImpl implements DefaultResponsibleDao {

	private static final Logger logger	=	LoggerFactory.getLogger(DefaultResponsibleDaoImpl.class);
	private SessionFactory		sessionFactory;
	
	/**
	 * Sets a session factory for this DAO
	 * @param sessionFactory session factory instance
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DefaultResponsible> getAllResponsibles() {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<DefaultResponsible> subList		=	session.createQuery("from DefaultResponsible").list();
		for(DefaultResponsible g : subList){
			logger.info("DefaultResponsible::"+g);
		}
		return subList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DefaultResponsible>	getResponsiblesByClassName(String className){
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<DefaultResponsible> subList	=	session.createQuery("from DefaultResponsible P where P.className = '"+className+"'").list();
		for(DefaultResponsible g : subList){
			logger.info("DefaultResponsible List::"+g);
		}
		return subList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDefaultResponsible(DefaultResponsible p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.update(p);
		logger.info("updated a "+p.getClass()+" on persistence layer");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(DefaultResponsible p) {
		Session session	=	this.sessionFactory.getCurrentSession();
		if(p!=null){
			session.delete(p);
		}
		logger.info(p.getClass().getName()+" deleted successfully");
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(DefaultResponsible p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("added a new "+p.getClass().getName()+" on persistence layer");		
	}

}
