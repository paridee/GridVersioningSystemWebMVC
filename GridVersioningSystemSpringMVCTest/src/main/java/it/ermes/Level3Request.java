package it.ermes;

import java.util.UUID;

public class Level3Request {
	private String tag;
	private String data;
	private String project;
	private String object;
	private UUID id;
	private String version;
	private String destinationAdress;
	public Level3Request() {
		super();
	}
	public Level3Request(String tag,String project, String object, String destinationAdress, String data, UUID id,
			String version) {
		super();
		this.tag = tag;
		this.project=project;
		this.object=object;
		this.data = data;
		this.id = id;
		this.version = version;
		this.destinationAdress=destinationAdress;
	}
	
	
	public String getDestinationAdress() {
		return destinationAdress;
	}
	public void setDestinationAdress(String destinationAdress) {
		this.destinationAdress = destinationAdress;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	

}
