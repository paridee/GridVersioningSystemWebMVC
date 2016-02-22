package grid.modification.grid;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import grid.entities.GridElement;

@Entity
@Table(name="Conflict")
public class Conflict {
	public enum State{PENDING,SOLVED,ABORTED};
	public enum Type{MAJOR,MINOR};
	//ATTUALE,ROOT VERSION, LATEST VERSION
	
	private ArrayList<GridElement> conflicting;
	private State 	conflictState;
	private Type 	conflictType;
	@Id
	private int id;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(	name = "ConflictToGridElement", 
				joinColumns 		= 	{ 
						@JoinColumn(name 		=	"conflictID", 
									nullable 	= 	false, 
									updatable 	= 	false)}, 
				inverseJoinColumns 	= 	{ 
						@JoinColumn(name 		= 	"gridElementID", 
									nullable 	= 	false, 
									updatable 	= 	false)}
			)
	public ArrayList<GridElement> getConflicting() {
		return conflicting;
	}
	public void setConflicting(ArrayList<GridElement> conflicting) {
		this.conflicting = conflicting;
	}
	public State getConflictState() {
		return conflictState;
	}
	public void setConflictState(State conflictState) {
		this.conflictState = conflictState;
	}
	public Type getConflictType() {
		return conflictType;
	}
	public void setConflictType(Type conflictType) {
		this.conflictType = conflictType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
