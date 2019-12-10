package edu.wpi.cs.justice.cardmaker.http;

public class DeleteCardResponse {
	public final int statusCode;
	public final String error;
	
	public DeleteCardResponse (int statusCode) {
		this.statusCode = statusCode;
		this.error = "";
	}
	
	// 200 means success
	public DeleteCardResponse (int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.error = errorMessage;
	}

}
