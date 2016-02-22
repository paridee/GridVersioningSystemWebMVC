package grid.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import grid.entities.Practitioner;
import grid.interfaces.DAO.PractitionerDAO;

@Repository
public class PractitionerDAOImpl implements PractitionerDAO {

	private static final Logger logger	=	LoggerFactory.getLogger(PractitionerDAOImpl.class);
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
	public List<Practitioner> getAllPractitioners() {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Practitioner> practList		=	session.createQuery("from Practitioner").list();
		for(Practitioner g : practList){
			logger.info("Practitioner::"+g);
		}
		return practList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Practitioner> getPractitionersByName(String name) {
		Session session				=	this.sessionFactory.getCurrentSession();
		List<Practitioner> pElList	=	session.createQuery("from Practitioner P where P.name = "+name).list();
		for(Practitioner g : pElList){
			logger.info("Practitioner List::"+g);
		}
		return pElList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Practitioner getPractitionerByEmail(String email) {
		Session session				=	this.sessionFactory.getCurrentSession();
		//should be only one
		List<Practitioner> pElList	=	session.createQuery("from Practitioner P where P.email = "+email).list();
		for(Practitioner g : pElList){
			logger.info("Practitioner List::"+g);
		}
		return pElList.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePractitioner(Practitioner p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.update(p);
		logger.info("updated a "+p.getClass()+" on persistence layer");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Practitioner p) {
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
	public void add(Practitioner p) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("PractitionerDAOImpl: added a new Practitioner on persistence layer");	
	}

}
