package grid.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grid.entities.Practitioner;
import grid.interfaces.DAO.ConflictDAO;
import grid.interfaces.services.ConflictService;
import grid.modification.grid.Conflict;

@Service
@Transactional
public class ConflictServiceImpl implements ConflictService {

	private ConflictDAO conflictDAO;
	
	public void setConflictDAO(ConflictDAO conflictDAO) {
		this.conflictDAO = conflictDAO;
	}

	@Override
	public void addConflict(Conflict c) {
		this.conflictDAO.addConflict(c);
	}

	@Override
	public Conflict getConflict(int id) {
		return this.conflictDAO.getConflict(id);
	}

	@Override
	public void updateConflict(Conflict c) {
		this.conflictDAO.updateConflict(c);
	}

	@Override
	public void removeConflict(Conflict c) {
		this.conflictDAO.removeConflict(c);
	}

	@Override
	public List<Conflict> getAllConflicts() {
		return this.conflictDAO.getAllConflicts();
	}

	@Override
	public List<Conflict> getConflictsBelongingTo(Practitioner p) {
		return this.conflictDAO.getConflictsBelongingTo(p);
	}

}
