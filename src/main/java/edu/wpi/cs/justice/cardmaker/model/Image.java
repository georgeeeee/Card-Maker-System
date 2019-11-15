package edu.wpi.cs.justice.cardmaker.model;

public class Image {
	private final String element_id;
	private	String type;
	private String imageUrl;
	
	public Image(String element_id, String type, String imageUrl) {
		super();
		this.element_id = element_id;
		this.type = type;
		this.imageUrl = imageUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getElement_id() {
		return element_id;
	}
	
	
}
