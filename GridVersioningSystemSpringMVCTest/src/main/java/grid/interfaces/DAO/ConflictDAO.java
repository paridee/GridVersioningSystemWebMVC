package grid.interfaces.DAO;

import java.util.List;

import grid.entities.Practitioner;
import grid.modification.grid.Conflict;

public interface ConflictDAO {
	public void addConflict(Conflict c);
	public Conflict getConflict(int id);
	public void updateConflict(Conflict c);
	public void removeConflict(Conflict c);
	public List<Conflict> getAllConflicts();
	public List<Conflict> getConflictsBelongingTo(Practitioner p);	
}
