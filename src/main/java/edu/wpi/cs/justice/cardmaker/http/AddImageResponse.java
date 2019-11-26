package edu.wpi.cs.justice.cardmaker.http;

import edu.wpi.cs.justice.cardmaker.model.Image;

public class AddImageResponse {
	public final Image image;
	public final int statusCode;
	public final String error;
	
	public AddImageResponse (Image image, int statusCode) {
		this.image = image; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddImageResponse (String errorMessage, int statusCode) {
		this.image = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
}