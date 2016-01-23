package grid.interfaces.services;

import java.util.List;
import grid.entities.Practitioner;

public interface PractitionerService {
	public List<Practitioner> getAllPractitioners();
	public List<Practitioner> getPractitionersByName(String name);
	public Practitioner getPractitionerByEmail(String email);
	public void updatePractitioner(Practitioner p);
	public void delete(Practitioner p);
	public void add(Practitioner p);	
}
