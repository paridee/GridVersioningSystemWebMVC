package grid.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import grid.entities.GridElement;
import grid.interfaces.DAO.GridElementDao;
import grid.interfaces.services.GridElementService;

/**
 * Service managing Grid Elements DAO
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */

@Service
@Transactional
public class GridElementServiceImpl implements GridElementService {

	private GridElementDao gridElementDao;
	
	/**
	 * Sets a DAO choosing persistence layer for Grid Elements
	 * @param gDAO DAO
	 */
	public void setGridElementDAO(GridElementDao gDAO) {
		this.gridElementDao = gDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void addGridElement(GridElement e) {
		this.gridElementDao.addGridElement(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateGridElement(GridElement e) {
		this.gridElementDao.updateGridElement(e);	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public GridElement upgradeGridElement(GridElement e) {
		GridElement upgraded	=	e.clone();
		upgraded.setVersion(upgraded.getVersion()+1);
		this.gridElementDao.addGridElement(upgraded);
		return upgraded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> listElement(Class<?> c) {
		return this.gridElementDao.listElement(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridElement getElementById(int id, Class<?> c) {
		return this.gridElementDao.getElementById(id, c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GridElement> getElementLog(String label, Class<?> c) {
		return this.gridElementDao.getElementLog(label, c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void removeElement(int id, Class<?> c) {
		this.gridElementDao.removeElement(id, c);
		
	}

	

}
