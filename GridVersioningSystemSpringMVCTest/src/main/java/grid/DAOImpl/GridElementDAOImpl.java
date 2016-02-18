package grid.DAOImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
		logger.info("added a new "+e.getClass().getSimpleName()+" on persistence layer");		
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
		List<GridElement> gridElList	=	session.createQuery("from "+c.getName()).list();
		for(GridElement g : gridElList){
			logger.info("Element List::"+g);
		}
		return gridElList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public GridElement getElementById(int id, String type) {
		Session session	=	this.sessionFactory.getCurrentSession();
		List<GridElement> returnres	=session.createQuery("from "+type+" P where P.id = \'"+id+"\'").list();
		if(returnres.size()==0){
			return null;
		}
		GridElement g	=	(GridElement) returnres.get(0) ;
		this.logger.info("refreshing "+g);
		session.refresh(g);
		return g;
		
		
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<GridElement> getElementLog(String id, String table) {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<GridElement> gElList	=	session.createQuery("from "+table+" where label = '"+id+"'").list();
		for(GridElement g : gElList){
			logger.info("GridElement List:"+g);
		}
		return gElList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<GridElement> getLatestWorkingElements() {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<String> tablesToCheck=new ArrayList<String>();
		tablesToCheck.add("Goal");tablesToCheck.add("MeasurementGoal");tablesToCheck.add("Metric");tablesToCheck.add("Question");tablesToCheck.add("Strategy");
		List<GridElement> latestWorkingElements	=new ArrayList<GridElement>();
		for(String s:tablesToCheck){
			List<GridElement> gElList	=	session.createQuery("from "+s+" where state = '"+GridElement.State.WORKING+"' group by label").list();
			for(GridElement g : gElList){
				List<GridElement> gElList2	=	session.createQuery("from "+s+" where state = '"+GridElement.State.WORKING+"' and label='"+g.getLabel()+"' order by version desc").list();
				latestWorkingElements.add(gElList2.get(0));
			}
		}
		return latestWorkingElements;
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
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<GridElement> getElementByState(String state) {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<GridElement> gElList	=	null;
		//TODO
		for(GridElement g : gElList){
			logger.info("GridElement List:"+g);
		}
		return gElList;
	}


}
