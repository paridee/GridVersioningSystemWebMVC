package grid.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import grid.entities.DefaultResponsible;
import grid.interfaces.DAO.DefaultResponsibleDao;
import grid.interfaces.services.DefaultResponsibleService;

public class DefaultResponsibleServiceImpl implements DefaultResponsibleService {
	private DefaultResponsibleDao defaultRespondibleDao;
	
	public void setDefaultResponsibleDao(DefaultResponsibleDao defaultRespondibleDao) {
		this.defaultRespondibleDao = defaultRespondibleDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DefaultResponsible> getAllResponsibles() {
		return this.defaultRespondibleDao.getAllResponsibles();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DefaultResponsible getResponsibleByClassName(String classN) {
		List<DefaultResponsible> prjResp	=	this.defaultRespondibleDao.getResponsiblesByClassName(classN);
		for(DefaultResponsible dr: prjResp){
			if(dr.getClassName().equals(classN)){
				return dr;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateDefaultResponsible(DefaultResponsible p) {
		this.defaultRespondibleDao.updateDefaultResponsible(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void delete(DefaultResponsible p) {
		this.defaultRespondibleDao.delete(p);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void add(DefaultResponsible p) {
		this.defaultRespondibleDao.add(p);
	}

}
