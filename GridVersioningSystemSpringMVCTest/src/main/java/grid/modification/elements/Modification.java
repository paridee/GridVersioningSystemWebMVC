package grid.modification.elements;

import java.util.ArrayList;
import java.util.List;

import org.javers.common.collections.Arrays;

import grid.entities.MeasurementGoal;
import grid.entities.Metric;
import grid.entities.Practitioner;
import grid.entities.Question;

/**
 * Root modification object, is just a stub and is used only for creating the proper lists
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */

public abstract class Modification {
	@SuppressWarnings("rawtypes")
	public static ArrayList<Class> minorUpdateClass	=	new ArrayList<Class>(){
		/**
		 * Version required by a serializable class
		 */
		private static final long serialVersionUID = -7005866737399716818L;

	{
		add(MeasurementGoal.class);
		add(Question.class);
		add(Metric.class);
	}};

	private List<Practitioner> 	responsibles;
	public List<Practitioner> getResponsibles() {
		return responsibles;
	}
	public void setResponsibles(List<Practitioner> responsibles) {
		this.responsibles = responsibles;
	}

	
}
