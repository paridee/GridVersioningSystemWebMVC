package grid.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import grid.Utils;
import grid.interfaces.Updatable;
/**
 * Class modeling a Measurement goal element on GQM+S Grid
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 */
@Entity
@Table(name="MeasurementGoal")
public class MeasurementGoal extends GridElement implements Updatable{
	private	String description			=	"";
	private	String interpretationModel	=	"";
	private	List<Question> questionList	=	new ArrayList<Question>();
	
	/**
	 * Returns a description of the measurement goal
	 * @return description string
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a description for this measurement goal
	 * @param description string
	 */
	public void setDescription(String description) {
		this.description 	= 	description;
	}

	/**
	 * Returns an Interpretation Model for this measurement Goal
	 * @return interpretation model
	 */
	public String getInterpretationModel() {
		return interpretationModel;
	}

	/**
	 * Sets an interpretation model for this measurement goal
	 * @param interpretationModel
	 */
	public void setInterpretationModel(String interpretationModel) {
		this.interpretationModel = interpretationModel;
	}
	
	/**
	 * Gets a question list for this measurement goal
	 * @return question list
	 */
	@ManyToMany(fetch 	= 	FetchType.LAZY, 
				cascade = 	CascadeType.ALL)
				@JoinTable(	name 	= 	"MeasurementGoalToQuestion", 
							joinColumns 	= 	{ 
									@JoinColumn(name 		= 	"goalID", 
												nullable	= 	false, 
												updatable 	= 	false) 
									}, 
							inverseJoinColumns 	= 	{ 
									@JoinColumn(name 		= 	"quesID", 
												nullable 	= 	false, 
												updatable 	= 	false) 
									})
	public List<Question> getQuestionList() {
		return questionList;
	}

	/**
	 * Sets a question list for this measurement goal
	 * @param questionList question list to be set for this measurement goal
	 */
	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> update(GridElement ge) {
		MeasurementGoal updated	=	(MeasurementGoal) this.clone();
		updated.setVersion(this.getVersion()+1);
		ArrayList<GridElement> returnList	=	new ArrayList<GridElement>();
		boolean addThis						=	false;	
		for(int i=0;i<this.questionList.size();i++){
			if(this.questionList.get(i).getLabel().equals(ge.getLabel())){
				updated.questionList.set(i, (Question) ge);
				addThis	=	true;
			}
		}
		for(int i=0;i<this.questionList.size();i++){
			Utils.mergeLists(returnList, this.questionList.get(i).update(ge));
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
		MeasurementGoal mg	=	new MeasurementGoal();
		mg.setLabel(this.label);
		mg.setVersion(this.version);
		mg.setDescription(this.description);
		mg.setInterpretationModel(this.interpretationModel);
		List<Question> clonedList	=	new ArrayList<Question>();
		for(int i=0;i<this.questionList.size();i++){
			clonedList.add(questionList.get(i));
		}
		mg.setQuestionList(clonedList);
		return mg;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix, String divider) {
		String returnString	=	prefix+"MeasurementGoal "+divider;
		returnString		=	returnString+prefix+"label: "+this.label+divider;
		returnString		=	returnString+prefix+"version: "+this.version+divider;
		returnString		=	returnString+prefix+"id: "+this.idElement+divider;
		returnString		=	returnString+prefix+"description: "+this.label+divider;
		returnString		=	returnString+prefix+"interpretation model: "+this.interpretationModel+divider;
		for(int i=0;i<this.questionList.size();i++){
			returnString	=	returnString+prefix+"question "+i+": "+this.questionList.get(i).toString(prefix+prefix, divider)+divider;
		}
		return returnString;
	}

}
