package grid.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import grid.entities.Project;
import grid.entities.SubscriberPhase;
import grid.interfaces.DAO.SubscriberPhaseDAO;
import grid.interfaces.services.SubscriberPhaseService;

public class SubscriberPhaseServiceImpl implements SubscriberPhaseService {

	private SubscriberPhaseDAO subscriberPhaseDAO;
	 
    public void setSubscriberPhaseDAO(SubscriberPhaseDAO subscriberPhaseDAO) {
        this.subscriberPhaseDAO = subscriberPhaseDAO;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriberPhase> getAllSubscribers() {
		return this.subscriberPhaseDAO.getAllSubscribers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriberPhase> getSubscribersByProject(Project aPrj) {
		return this.subscriberPhaseDAO.getSubscribersByProject(aPrj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SubscriberPhase> getSubscribersByUrl(String url) {
		return this.subscriberPhaseDAO.getSubscribersByUrl(url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateSubscriber(SubscriberPhase p) {
		this.subscriberPhaseDAO.updateSubscriber(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void delete(SubscriberPhase p) {
		this.subscriberPhaseDAO.delete(p);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void add(SubscriberPhase p) {
		this.subscriberPhaseDAO.add(p);
	}

}
