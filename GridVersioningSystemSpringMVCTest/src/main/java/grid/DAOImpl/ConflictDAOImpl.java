package grid.DAOImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import grid.entities.GridElement;
import grid.entities.Practitioner;
import grid.interfaces.DAO.ConflictDAO;
import grid.modification.grid.Conflict;

@Repository
public class ConflictDAOImpl implements ConflictDAO {

	private static final Logger logger	=	LoggerFactory.getLogger(ConflictDAOImpl.class);
	private SessionFactory		sessionFactory;
	
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void addConflict(Conflict c) {
		Session	session	=	this.sessionFactory.getCurrentSession();
		session.persist(c);
	}

	@Override
	public Conflict getConflict(int id) {
		Session currentSession	=	this.sessionFactory.getCurrentSession();
		Conflict	c			=	(Conflict) currentSession.load(Conflict.class,new Integer(id));
		logger.info("Conflict loaded::"+c);
		return c;
	}

	@Override
	public void updateConflict(Conflict c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConflict(Conflict c) {
		Session session	=	this.sessionFactory.getCurrentSession();
		if(c!=null){
			session.delete(c);
		}
	}

	@Override
	public List<Conflict> getAllConflicts() {
		Session session				=	this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Conflict> confList		=	session.createQuery("from Conflict").list();
		for(Conflict g : confList){
			logger.info("Conflict::"+g);
		}
		return confList;
	}

	@Override
	public List<Conflict> getConflictsBelongingTo(Practitioner p) {
		List<Conflict> conflicts	=	this.getAllConflicts();
		List<Conflict> returnList	=	new ArrayList<Conflict>();
		for(Conflict c : conflicts){
			List<GridElement> elementList	=	c.getConflicting();
			for(GridElement ge:elementList){
				List<Practitioner> authors	=	ge.getAuthors();
				for(Practitioner author:authors){
					if(author.getEmail().equals(p.getEmail())){
						if(!returnList.contains(c)){
							returnList.add(c);
						}
					}
				}
			}
		}
		return returnList;
	}

}
