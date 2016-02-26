package grid.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import grid.entities.Project;
import grid.entities.SubscriberPhase;
import grid.interfaces.DAO.SubscriberPhaseDAO;

public class SubscriberPhaseDAOImpl implements SubscriberPhaseDAO {
	private static final Logger logger	=	LoggerFactory.getLogger(SubscriberPhaseDAOImpl.class);
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
	public List<SubscriberPhase> getAllSubscribers() {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<SubscriberPhase> subList		=	session.createQuery("from SubscriberPhase").list();
		for(SubscriberPhase g : subList){
			logger.info("SubscriberPhase::"+g);
		}
		return subList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriberPhase> getSubscribersByProject(Project aPrj) {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<SubscriberPhase> subList	=	session.createQuery("from SubscriberPhase P where P.aProject = "+aPrj.getId()).list();
		for(SubscriberPhase g : subList){
			logger.info("SubscriberPhase List::"+g);
		}
		return subList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriberPhase> getSubscribersByUrl(String url) {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<SubscriberPhase> subList	=	session.createQuery("from SubscriberPhase P where P.url = "+url).list();
		for(SubscriberPhase g : subList){
			logger.info("SubscriberPhase List::"+g);
		}
		return subList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSubscriber(SubscriberPhase p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.update(p);
		logger.info("updated a "+p.getClass()+" on persistence layer");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(SubscriberPhase p) {
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
	public void add(SubscriberPhase p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("added a new "+p.getClass().getName()+" on persistence layer");	
	}

}
