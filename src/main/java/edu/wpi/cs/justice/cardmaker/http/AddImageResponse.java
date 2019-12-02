package edu.wpi.cs.justice.cardmaker.http;

import java.net.URL;

import edu.wpi.cs.justice.cardmaker.model.Image;

public class AddImageResponse {
	public final String presignedUrl;
	public final int statusCode;
	public final String error;
	
	public AddImageResponse (URL url, int statusCode) {
		presignedUrl = url.toString(); // doesn't matter since error
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddImageResponse (String errorMessage, int statusCode) {
		this.presignedUrl = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
}