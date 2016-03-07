package it.ermes;

import java.util.ArrayList;
import java.util.UUID;


public class Level1Request {

	private String destinationAdress;
	private ArrayList<String> data;
	private String originAdress;
    private UUID id;
	public Level1Request(String originAdress, String destinationAdress, ArrayList<String> data,String project,String object, UUID id) {
		super();
		this.destinationAdress = destinationAdress;
		this.data = data;
		this.originAdress=originAdress;
		this.id=id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Level1Request() {
		super();
	}

	public String getDestinationAdress() {
		return destinationAdress;
	}

	public void setDestinationAdress(String destinationAdress) {
		this.destinationAdress = destinationAdress;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

	public String getOriginAdress() {
		return originAdress;
	}

	public void setOriginAdress(String originAdress) {
		this.originAdress = originAdress;
	}

	
	

}
