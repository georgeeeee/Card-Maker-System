package edu.wpi.cs.justice.cardmaker.http;

import java.net.URL;

import edu.wpi.cs.justice.cardmaker.model.Card;

public class EditImageResponse {
	public final String presignedUrl;
	public final int statusCode;
	public final String error;
	
	public EditImageResponse (int statusCode, URL presignedUrl) {
		this.statusCode = statusCode;
		this.error = "";
		if(presignedUrl != null) {
			this.presignedUrl = presignedUrl.toString();
		} else {
			this.presignedUrl = null;
		}
	}
	
	public EditImageResponse (String errorMessage, int statusCode) {
		this.presignedUrl = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
}