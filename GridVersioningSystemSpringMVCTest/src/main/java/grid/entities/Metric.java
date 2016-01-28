package grid.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Table;

import grid.interfaces.Updatable;

/**
 * 
 * @author Paride Casulli
 * @author Lorenzo La Banca
 *
 * This class models a Goal entity on a GQM+S Grid
 */

@Entity
@Table(name="Metric")
public class Metric extends GridElement implements Updatable{
	@Column(name="count")
	public int 					count				=	0;
	@Column(name="description")
	public String				description			=	"";
	public List<String>	measUnits					=	new ArrayList<String>();
	@Column(name="measurementProcess")
	public String				measurementProcess	=	"";
	@Column(name="metricType")
	public String				metricType			=	"";
	@Column(name="scaleType")
	public String				scaleType			=	"";
	
	/**
	 * Getter for the metric count
	 * @return count of this metric
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Setter for the metric count
	 * @param count count to be set for this metric
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Getter of metric description
	 * @return description of this metric
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for metric description
	 * @param description description for this metric
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter method for measurement units of this metric
	 * @return list of measurement units
	 */
	@ElementCollection
	@CollectionTable(	name		=	"MetricMeasUnits", 
						joinColumns	=	@JoinColumn(name="metrID"))
	@Column(name="measUnit")
	public List<String> getMeasUnits() {
		return measUnits;
	}

	/**
	 * Setter method for measurement units of this metric
	 * @param measUnits new measurements units to be set
	 */
	public void setMeasUnits(List<String> measUnits) {
		this.measUnits = measUnits;
	}

	/**
	 * Getter method for measurement process
	 * @return measurement process for this metric
	 */
	public String getMeasurementProcess() {
		return measurementProcess;
	}

	/**
	 * Setter of measurement process for this metric
	 * @param measurementProcess measurement process to be set
	 */
	public void setMeasurementProcess(String measurementProcess) {
		this.measurementProcess = measurementProcess;
	}

	/**
	 * Gets the metric type for this metric
	 * @return metric type
	 */
	public String getMetricType() {
		return metricType;
	}

	/**
	 * Setter for metric type
	 * @param metricType metric type to be set for this metric
	 */
	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	/**
	 * Getter method for scale type of this object
	 * @return scale type
	 */
	public String getScaleType() {
		return scaleType;
	}

	/**
	 * Setter method for scale type of this metric
	 * @param scaleType new scale type to be set
	 */
	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<GridElement> update(GridElement ge,boolean autoupgrade) {
		return new ArrayList<GridElement>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridElement clone() {
		Metric newMetric	=	new Metric();
		newMetric.setLabel(this.getLabel());
		newMetric.setVersion(this.getVersion());
		newMetric.setCount(this.count);
		newMetric.setDescription(this.description);
		List<Practitioner> clonedListP	=	new ArrayList<Practitioner>();
		for(int i=0;i<this.getAuthors().size();i++){
			clonedListP.add(this.getAuthors().get(i));
		}
		newMetric.setAuthors(clonedListP);
		List<String> clonedList	=	new ArrayList<String>();
		for(int i=0;i<this.measUnits.size();i++){
			clonedList.add(measUnits.get(i));
		}
		newMetric.setMeasUnits(clonedList);
		newMetric.setMeasurementProcess(this.measurementProcess);
		newMetric.setMetricType(this.metricType);
		newMetric.setScaleType(this.scaleType);
		newMetric.setState(this.state);
		return newMetric;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(String prefix, String divider) {
		String returnString	=	prefix+"Metric "+divider;
		returnString	=	returnString+prefix+"state: "+this.state+divider;
		returnString	=	returnString+prefix+"label: "+this.label+divider;
		returnString	=	returnString+prefix+"version: "+this.version+divider;
		returnString	=	returnString+prefix+"id: "+this.idElement+divider;
		returnString	=	returnString+prefix+"description: "+this.label+divider;
		returnString	=	returnString+prefix+"count: "+this.count+divider;
		returnString	=	returnString+prefix+"measurement process: "+this.measurementProcess+divider;
		returnString	=	returnString+prefix+"metric type: "+this.metricType+divider;
		returnString	=	returnString+prefix+"scale type: "+this.scaleType+divider;
		for(int i=0;i<this.measUnits.size();i++){
			returnString	=	returnString+prefix+"measurement unit "+i+": "+this.measUnits.get(i).toString()+divider;
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
		Metric other = (Metric) obj;
		if (this.label == null) {	//manually added
			if (other.getLabel() != null)
				return false;
		} else if (!label.equals(other.getLabel()))
			return false;
		if (count != other.count)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (measUnits == null) {
			if (other.measUnits != null)
				return false;
		} else{
			if(this.getMeasUnits().size()!=other.getMeasUnits().size()){
				return false;
			}
			ArrayList<String> meas			=	new ArrayList<String>();
			ArrayList<String> measCheck		=	new ArrayList<String>();
			for(int i=0;i<this.getMeasUnits().size();i++){	//both have same size
				meas.add(this.getMeasUnits().get(i));
				measCheck.add(other.getMeasUnits().get(i));
			}
			for(int i=0;i<meas.size();i++){
				if(!measCheck.contains(meas.get(i))){
					return	false;
				}
			}
		}
		if (measurementProcess == null) {
			if (other.measurementProcess != null)
				return false;
		} else if (!measurementProcess.equals(other.measurementProcess))
			return false;
		if (metricType == null) {
			if (other.metricType != null)
				return false;
		} else if (!metricType.equals(other.metricType))
			return false;
		if (scaleType == null) {
			if (other.scaleType != null)
				return false;
		} else if (!scaleType.equals(other.scaleType))
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
		return returnMap;
	}

}
