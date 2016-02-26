package grid.interfaces.DAO;

import java.util.List;
import grid.entities.Project;
import grid.entities.SubscriberPhase;

/**
 * DAO for a Subscribing Phase (to updates, manages all CRUD operations
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
public interface SubscriberPhaseDAO {
	/**
	 * returns all the subscriptions present in this system
	 * @return subscribers list
	 */
	public List<SubscriberPhase> 	getAllSubscribers();
	
	/**
	 * returns a list of subscriptions with that project
	 * @param project a project
	 * @return subscribers
	 */
	public List<SubscriberPhase> 	getSubscribersByProject(Project aPrj);
	
	/**
	 * returns a subscriber identified by an URL
	 * @param url
	 * @return a subscriber
	 */
	public List<SubscriberPhase>			getSubscribersByUrl(String url);
	
	/**
	 * updates a subscriber
	 * @param p subscriber to be updated
	 */
	public void 				updateSubscriber(SubscriberPhase p);
	
	/**
	 * delete a subscriber
	 * @param p subscriber to be deleted
	 */
	public void					delete(SubscriberPhase p);
	
	/**
	 * add a subscriber
	 * @param p subscriber to be added
	 */ 
	public void					add(SubscriberPhase p);
}
