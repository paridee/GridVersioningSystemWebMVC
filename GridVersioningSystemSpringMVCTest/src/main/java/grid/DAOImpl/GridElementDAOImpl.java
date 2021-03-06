package grid.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import grid.entities.GridElement;
import grid.interfaces.DAO.GridElementDao;

/**
 * DAO implementation for a GridElement in Hibernate
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

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
		logger.info("added a new "+e.getClass().getSimpleName()+" on persistence layer id:"+e.getIdElement());		
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
	@SuppressWarnings("rawtypes")
	@Override
	public List<GridElement> listElement(Class c) {
		Session session					=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<GridElement> gridElList	=	session.createQuery("from "+c.getName()+" order by timestamp asc").list();
		for(GridElement g : gridElList){
			logger.info("Element List::"+g);
		}
		return gridElList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public GridElement getElementById(int id, String type) {
		Session session	=	this.sessionFactory.getCurrentSession();
		List<GridElement> returnres	=session.createQuery("from "+type+" P where P.id = \'"+id+"\'").list();
		if(returnres.size()==0){
			return null;
		}
		GridElement g	=	(GridElement) returnres.get(0) ;
		logger.info("refreshing "+g);
		session.refresh(g);
		return g;
		
		
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public List<GridElement> getElementLog(String id, String table) {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<GridElement> gElList	=	session.createQuery("from "+table+" where label = '"+id+"' order by timestamp desc").list();
		for(GridElement g : gElList){
			logger.info("GridElement List:"+g);
		}
		return gElList;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
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
