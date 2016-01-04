package grid.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import grid.Utils;
import grid.interfaces.Updatable;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Goal entity on a Grid
 */

@Entity
@Table(name="Goal")
public class Goal extends GridElement implements Updatable{
	@Column(name="assumption")
	private String 				assumption		=	"";
	@Column(name="context")
	private	String 				context			=	"";
	@Column(name="description")
	private String 				description		=	"";
	
	private MeasurementGoal		measurementGoal	=	null;
	
	private List<Strategy> 		strategyList	=	new ArrayList<Strategy>();
	
	/**
	 * Returns a goal assumption
	 * @return string assumption
	 */
	public String getAssumption() {
		return assumption;
	}

	/**
	 * Sets a Goal assumption getting a String
	 * @param assumption assumption string
	 */
	public void setAssumption(String assumption) {
		this.assumption = assumption;
	}

	/**
	 * Returns context factors for this goal in string format
	 * @return context factor
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Sets context factor for this goal
	 * @param context string representing the context factor
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * Returns a description for this goal
	 * @return string description of the goal
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a description for this goal
	 * @param description string description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a measurement goal referenced for this goal
	 * @return measurement goal instance
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="measurementGoal")
	public MeasurementGoal getMeasurementGoal() {
		return measurementGoal;
	}

	/**
	 * Sets a measurement goal for this object
	 * @param measurementGoal measurement goal to be set for this goal
	 */
	public void setMeasurementGoal(MeasurementGoal measurementGoal) {
		this.measurementGoal = measurementGoal;
	}

	/**
	 * Returns the list of strategies for this goal
	 * @return list of strategies
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(	name = "GoalToStrategyList", 
				joinColumns 		= 	{ 
						@JoinColumn(name 		=	"goalID", 
									nullable 	= 	false, 
									updatable 	= 	false)}, 
				inverseJoinColumns 	= 	{ 
						@JoinColumn(name 		= 	"strID", 
									nullable 	= 	false, 
									updatable 	= 	false)}
			)
	public List<Strategy> getStrategyList() {
		return strategyList;
	}

	/**
	 * Sets the list of strategies for this goal
	 * @param strategyList list of strategies to be assigned for this goal
	 */
	public void setStrategyList(List<Strategy> strategyList) {
		this.strategyList = strategyList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> update(GridElement ge) {
		ArrayList<GridElement> returnList	=	new ArrayList<GridElement>();
		boolean addThis						=	false;	
		Goal updated						=	(Goal) this.clone();
		updated.setVersion(this.getVersion()+1);
		if(this.measurementGoal.getLabel().equals(ge.getLabel())){
			updated.setMeasurementGoal((MeasurementGoal)ge);
			addThis	=	true;
		}
		for(int i=0;i<this.strategyList.size();i++){
			if(this.strategyList.get(i).getLabel().equals(ge.getLabel())){
				updated.strategyList.set(i, (Strategy) ge);
				addThis=true;
			}
		}
		Utils.mergeLists(returnList, this.measurementGoal.update(ge));
		for(int i=0;i<this.strategyList.size();i++){
			Utils.mergeLists(returnList, this.strategyList.get(i).update(ge));
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
		// TODO Auto-generated method stub
		Goal newGoal	=	new Goal();
		newGoal.setLabel(this.label);
		newGoal.setVersion(this.version);
		newGoal.setAssumption(this.assumption);
		newGoal.setContext(this.context);
		newGoal.setDescription(this.description);
		newGoal.setMeasurementGoal(this.measurementGoal);
		List<Strategy> clonedList	=	new ArrayList<Strategy>();
		for(int i=0;i<this.strategyList.size();i++){
			clonedList.add(strategyList.get(i));
		}
		newGoal.setStrategyList(clonedList);
		return newGoal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix,String divider) {
		String returnString		=	prefix+"Goal "+divider;
		returnString			=	returnString+prefix+"label: "+this.label+divider;
		returnString			=	returnString+prefix+"version: "+this.version+divider;
		returnString			=	returnString+prefix+"id: "+this.idElement+divider;
		returnString			=	returnString+prefix+"assumption: "+this.assumption+divider;
		returnString			=	returnString+prefix+"context: "+this.context+divider;
		returnString			=	returnString+prefix+"description: "+this.label+divider;
		returnString			=	returnString+prefix+"measurement goal: "+this.measurementGoal.toString(prefix+prefix, divider)+divider;
		for(int i=0;i<this.strategyList.size();i++){
			returnString	=	returnString+prefix+"strategy "+i+": "+this.strategyList.get(i).toString(prefix+prefix, divider)+divider;
		}
		return returnString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goal other = (Goal) obj;
		if (this.label == null) {	//manually added
			if (other.getLabel() != null)
				return false;
		} else if (!label.equals(other.getLabel()))
			return false;
		if (assumption == null) {
			if (other.assumption != null)
				return false;
		} else if (!assumption.equals(other.assumption))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (measurementGoal == null) {
			if (other.measurementGoal != null)
				return false;
		} else if (!measurementGoal.getLabel().equals(other.measurementGoal.getLabel()))
			return false;
		if (strategyList == null) {
			if (other.strategyList != null)
				return false;
		} else {
			if(!(this.getStrategyList().size()==other.getStrategyList().size())){
				return false;
			}
			else{
				//check if refer to same strategies (identified with labels)
				ArrayList<String> strategyLabels			=	new ArrayList<String>();
				ArrayList<String> strategyLabelsCheckGoal	=	new ArrayList<String>();
				for(int i=0;i<this.getStrategyList().size();i++){	//both have same size
					strategyLabels.add(this.getStrategyList().get(i).getLabel());
					strategyLabelsCheckGoal.add(other.getStrategyList().get(i).getLabel());
				}
				for(int i=0;i<strategyLabels.size();i++){
					if(!strategyLabelsCheckGoal.contains(strategyLabels.get(i))){
						return false;
					}
				}
			}
		}
		return true;
	}

}
