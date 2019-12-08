package edu.wpi.cs.justice.cardmaker.http;

import edu.wpi.cs.justice.cardmaker.model.Card;

public class DuplicateCardResponse {
	public final Card card;
	public final int statusCode;
	public final String error;
	
	public DuplicateCardResponse (Card card, int statusCode) {
		this.card = card; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = "";
	}
	
	public DuplicateCardResponse (String errorMessage, int statusCode) {
		this.card = null; // doesn't matter since error
		this.statusCode = statusCode;
		this.error = errorMessage;
	}

}