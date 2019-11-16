package edu.wpi.cs.justice.cardmaker.model;

public class Image {
	private final String element_id;
	private String imageUrl;
	
	public Image(String element_id, String imageUrl) {
		super();
		this.element_id = element_id;
		this.imageUrl = imageUrl;
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
