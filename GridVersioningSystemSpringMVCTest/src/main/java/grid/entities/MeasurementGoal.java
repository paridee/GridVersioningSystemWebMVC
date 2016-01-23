package grid.entities;

import java.util.ArrayList;
import java.util.HashMap;
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
		mg.setState(this.state);
		List<Practitioner> clonedListP	=	new ArrayList<Practitioner>();
		for(int i=0;i<this.getAuthors().size();i++){
			clonedListP.add(this.getAuthors().get(i));
		}
		mg.setAuthors(clonedListP);
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
		returnString		=	returnString+prefix+"state: "+this.state+divider;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((interpretationModel == null) ? 0 : interpretationModel.hashCode());
		result = prime * result + ((questionList == null) ? 0 : questionList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementGoal other = (MeasurementGoal) obj;
		if (this.label == null) {	//manually added
			if (other.getLabel() != null)
				return false;
		} else if (!label.equals(other.getLabel()))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (interpretationModel == null) {
			if (other.interpretationModel != null)
				return false;
		} else if (!interpretationModel.equals(other.interpretationModel))
			return false;
		if (questionList == null) {
			if (other.questionList != null)
				return false;
		} else{
			if(!(this.getQuestionList().size()==other.getQuestionList().size())){
				return false;
			}
			ArrayList<String> questionLabels			=	new ArrayList<String>();
			ArrayList<String> questionLabelsCheck		=	new ArrayList<String>();
			for(int i=0;i<this.getQuestionList().size();i++){	//both have same size
				questionLabels.add(this.getQuestionList().get(i).getLabel());
				questionLabelsCheck.add(other.getQuestionList().get(i).getLabel());
			}
			for(int i=0;i<questionLabels.size();i++){
				if(!questionLabelsCheck.contains(questionLabels.get(i))){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, GridElement> obtainEmbeddedElements() {
		// TODO Auto-generated method stub
		HashMap<String, GridElement> returnMap	=	new HashMap<String, GridElement>();
		returnMap.put(this.label, this);
		for(int i=0;i<this.questionList.size();i++){
			returnMap.putAll(this.questionList.get(i).obtainEmbeddedElements());
		}
		return returnMap;
	}
	
	/*
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().equals(this.getClass())){
			boolean returnValue			=	true;
			MeasurementGoal 	check	=	(MeasurementGoal)obj;
			if(!this.getLabel().equals(check.getLabel())){
				returnValue	=	false;
			}
			if(!this.description.equals(check.getDescription())){
				returnValue	=	false;
			}
			if(!this.interpretationModel.equals(check.getInterpretationModel())){
				returnValue	=	false;
			}
			else{
				//check if refer to same strategies (identified with labels)
				ArrayList<String> questionLabels			=	new ArrayList<String>();
				ArrayList<String> questionLabelsCheck		=	new ArrayList<String>();
				for(int i=0;i<this.getQuestionList().size();i++){	//both have same size
					questionLabels.add(this.getQuestionList().get(i).getLabel());
					questionLabelsCheck.add(check.getQuestionList().get(i).getLabel());
				}
				for(int i=0;i<questionLabels.size();i++){
					if(!questionLabelsCheck.contains(questionLabels.get(i))){
						returnValue	=	false;
					}
				}
			}
			return returnValue;
		}
		return false;
	} */

}
