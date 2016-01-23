package grid.DAOImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import grid.entities.Goal;
import grid.entities.GridElement;
import grid.interfaces.DAO.GridElementDao;

@Repository
public class GridElementDAOImpl implements GridElementDao {

	private static final Logger logger	=	LoggerFactory.getLogger(GridElementDAOImpl.class);
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
	public void addGridElement(GridElement e) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.persist(e);
		logger.info("GoalDAOImpl: added a new Goal on persistence layer");		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGridElement(GridElement e) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.update(e);
		logger.info("updated a "+e.getClass()+" on persistence layer");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> listElement(Class c) {
		Session session					=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<GridElement> gridElList	=	session.createQuery("from "+c.getName()).list();
		for(GridElement g : gridElList){
			logger.info("Element List::"+g);
		}
		return gridElList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridElement getElementById(int id, Class c) {
		Session currentSession	=	this.sessionFactory.getCurrentSession();
		GridElement	g			=	(GridElement) currentSession.load(c,new Integer(id));
		logger.info(c.getName()+" loaded::"+g);
		return g;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> getElementLog(String label, Class c) {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<GridElement> gElList	=	session.createQuery("from "+c.getName()+" G where G.label = "+label).list();
		for(GridElement g : gElList){
			logger.info("Goal List::"+g);
		}
		return gElList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeElement(int id, Class c) {
		Session session			=	this.sessionFactory.getCurrentSession();
		GridElement g			=	(GridElement)	session.load(c, new Integer(id));
		if(g!=null){
			session.delete(g);
		}
		logger.info(g.getClass().getName()+" deleted successfully");
	}


}
