package it.ermes;



import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

	private String tag;

	private ArrayList<String> content;

	private String resolvedAdress;
	
	private String originAdress;
	
	private UUID id;
	

	public Request(String tag, ArrayList<String> content, String originAdress, String resolvedAdress, UUID id) {
		super();
		this.tag = tag;
		this.content = content;
		this.originAdress = originAdress;
		this.resolvedAdress= resolvedAdress;
		this.id=id;
	}
	public String getResolvedAdress() {
		return resolvedAdress;
	}
	public void setResolvedAdress(String resolvedAdress) {
		this.resolvedAdress = resolvedAdress;
	}
	public String getOriginAdress() {
		return originAdress;
	}
	public void setOriginAdress(String originAdress) {
		this.originAdress = originAdress;
	}
	public Request() {
		super();
		
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public ArrayList<String> getContent() {
		return content;
	}
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
	public void fillContent(String content)
	{
		this.content.add(content);
	}

}
