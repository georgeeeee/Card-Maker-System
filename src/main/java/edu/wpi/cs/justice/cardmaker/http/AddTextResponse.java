package edu.wpi.cs.justice.cardmaker.http;

import edu.wpi.cs.justice.cardmaker.model.Text;

public class AddTextResponse {
	public final Text text;
	public final int statusCode;
	public final String error;
	
	public AddTextResponse (Text text, int statusCode) {
		this.text = text; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public AddTextResponse (String errorMessage, int statusCode) {
		this.text = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}
}