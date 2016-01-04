package grid.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import grid.Utils;
import grid.interfaces.Updatable;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Question entity on a Grid
 */

@Entity
@Table(name="Question")
public class Question extends GridElement implements Updatable{

	private List<Metric> 	metricList	=	new ArrayList<Metric>();
	private String 			question;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "QuestionToMetric", joinColumns = { 
			@JoinColumn(name 		= 	"quesID", 
						nullable 	= 	false, 
						updatable 	= 	false) 
			}, 
			inverseJoinColumns 	= { 
					@JoinColumn(name 		= 	"metrID", 
								nullable 	= 	false, 
								updatable 	= 	false) 
					})
	
	/**
	 * Getter of all the metrics involved in this question
	 * @return list of metrics involved
	 */
	public List<Metric> getMetricList() {
		return metricList;
	}

	/**
	 * Setter of metrics involved in this question
	 * @param metricList metrics involved
	 */
	public void setMetricList(List<Metric> metricList) {
		this.metricList = metricList;
	}

	/**
	 * Get the question
	 * @return question string
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Set a question string
	 * @param question a question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> update(GridElement ge) {
		Question updated	=	(Question) this.clone();
		updated.setVersion(this.getVersion()+1);
		ArrayList<GridElement> returnList	=	new ArrayList<GridElement>();
		boolean addThis						=	false;	
		for(int i=0;i<this.metricList.size();i++){
			if(this.metricList.get(i).getLabel().equals(ge.getLabel())){
				updated.metricList.set(i, (Metric) ge);
				addThis=true;
			}
		}
		for(int i=0;i<this.metricList.size();i++){
			Utils.mergeLists(returnList, this.metricList.get(i).update(ge));
		}
		if(addThis==true){
			returnList.add(updated);
		}
		return returnList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridElement clone() {
		Question cloned	=	new Question();
		cloned.setLabel(this.label);
		cloned.setVersion(this.version);
		List<Metric> clonedList	=	new ArrayList<Metric>();
		for(int i=0;i<this.metricList.size();i++){
			clonedList.add(metricList.get(i));
		}
		cloned.setMetricList(clonedList);
		cloned.setQuestion(this.question);
		return cloned;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix, String divider) {
		String returnString	=	prefix+"Question "+divider;
		returnString	=	returnString+prefix+"label: "+this.label+divider;
		returnString	=	returnString+prefix+"version: "+this.version+divider;
		returnString	=	returnString+prefix+"id: "+this.idElement+divider;
		returnString	=	returnString+prefix+"description: "+this.label+divider;
		returnString	=	returnString+prefix+"question: "+this.question+divider;
		for(int i=0;i<this.metricList.size();i++){
			returnString	=	returnString+prefix+"metric "+i+": "+this.metricList.get(i).toString(prefix,divider)+divider;
		}
		return returnString;
	}

}
