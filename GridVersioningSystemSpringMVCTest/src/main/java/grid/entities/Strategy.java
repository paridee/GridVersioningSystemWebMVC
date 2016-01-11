package grid.entities;

import java.util.ArrayList;
import java.util.HashMap;
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
 * This class models a Strategy entity on a Grid
 */

@Entity
@Table(name="Strategy")
public class Strategy extends GridElement implements Updatable{
	
	private String 			description;
	private boolean			isTerminal	=	false;
	private String			strategicProjectId;
	private List<Goal>		goalList	=	new ArrayList<Goal>();
	
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsTerminal() {
		return isTerminal;
	}


	public void setIsTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}


	public String getStrategicProjectId() {
		return strategicProjectId;
	}


	public void setStrategicProjectId(String strategicProjectId) {
		this.strategicProjectId = strategicProjectId;
	}

	@ManyToMany(	cascade 	= 	CascadeType.ALL)
	@JoinTable(		name 		= 	"StrategyToGoalList", 
					joinColumns = 	{ 
							@JoinColumn(name 		= 	"strID", 
										nullable 	= 	false, 
										updatable 	= 	false) 
							}, 
					inverseJoinColumns	= { 
							@JoinColumn(name 		= 	"goalID", 
										nullable 	= 	false, 
										updatable 	= 	false) 
							})
	public List<Goal> getGoalList() {
		return goalList;
	}


	public void setGoalList(List<Goal> goalList) {
		this.goalList = goalList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> update(GridElement ge) {
		Strategy updated	=	(Strategy)this.clone();
		updated.setVersion(this.getVersion()+1);
		ArrayList<GridElement> returnList	=	new ArrayList<GridElement>();
		boolean addThis						=	false;	
		for(int i=0;i<this.goalList.size();i++){
			if(this.goalList.get(i).getLabel().equals(ge.getLabel())){
				updated.goalList.set(i, (Goal) ge);
				addThis=true;
			}
		}
		for(int i=0;i<this.goalList.size();i++){
			Utils.mergeLists(returnList, this.goalList.get(i).update(ge));
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
		Strategy cloned	=	new Strategy();
		cloned.setLabel(this.label);
		cloned.setVersion(this.version);
		cloned.setDescription(this.description);
		List<Goal> clonedList	=	new ArrayList<Goal>();
		for(int i=0;i<this.goalList.size();i++){
			clonedList.add(goalList.get(i));
		}
		cloned.setGoalList(clonedList);
		cloned.setIsTerminal(this.isTerminal);
		cloned.setStrategicProjectId(this.strategicProjectId);
		cloned.setState(this.state);
		return cloned;		
	}


	@Override
	public String toString(String prefix, String divider) {
		String returnString	=	prefix+"Strategy "+divider;
		returnString	=	returnString+prefix+"state: "+this.state+divider;
		returnString	=	returnString+prefix+"label: "+this.label+divider;
		returnString	=	returnString+prefix+"version: "+this.version+divider;
		returnString	=	returnString+prefix+"id: "+this.idElement+divider;
		returnString	=	returnString+prefix+"description: "+this.label+divider;
		returnString	=	returnString+prefix+"terminal: "+this.isTerminal+divider;
		returnString	=	returnString+prefix+"strategic project: "+this.strategicProjectId+divider;
		for(int i=0;i<this.goalList.size();i++){
			returnString	=	returnString+prefix+"goal "+i+": "+this.goalList.get(i).toString(prefix,divider)+divider;
		}
		return returnString;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Strategy other = (Strategy) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (goalList == null) {
			if (other.goalList != null)
				return false;
		} else{
			if(!(this.getGoalList().size()==other.getGoalList().size())){
				return false;
			}
			else{
				ArrayList<String> goalLabels			=	new ArrayList<String>();
				ArrayList<String> otherGoalLabels		=	new ArrayList<String>();
				for(int i=0;i<this.getGoalList().size();i++){	//both have same size
					goalLabels.add(this.getGoalList().get(i).getLabel());
					otherGoalLabels.add(other.getGoalList().get(i).getLabel());
				}
				for(int i=0;i<goalLabels.size();i++){
					if(!otherGoalLabels.contains(goalLabels.get(i))){
						return false;
					}
				}
			}
		}
		if (isTerminal != other.isTerminal)
			return false;
		if (strategicProjectId == null) {
			if (other.strategicProjectId != null)
				return false;
		} else if (!strategicProjectId.equals(other.strategicProjectId))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, GridElement> obtainEmbeddedElements() {
		HashMap<String, GridElement> returnMap	=	new HashMap<String, GridElement>();
		returnMap.put(this.label, this);
		for(int i=0;i<this.goalList.size();i++){
			returnMap.putAll(this.goalList.get(i).obtainEmbeddedElements());
		}
		return returnMap;
	}
	
}