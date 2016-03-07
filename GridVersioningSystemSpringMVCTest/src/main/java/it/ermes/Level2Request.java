package it.ermes;

import java.util.ArrayList;
import java.util.UUID;



public class Level2Request {
	private String originAdress;
	private String destinationAdress;
	private ArrayList<String> data;
	 private UUID id;
	public Level2Request() {
		super();
		
	}
	public Level2Request(String originAdress, String destinationAdress,
			ArrayList<String> data,  UUID id) {
		super();
		this.originAdress = originAdress;
		this.destinationAdress = destinationAdress;
		this.data = data;
		this.id=id;
	}
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	public String getOriginAdress() {
		return originAdress;
	}
	public void setOriginAdress(String originAdress) {
		this.originAdress = originAdress;
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

	
}
