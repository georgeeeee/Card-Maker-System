package edu.wpi.cs.justice.cardmaker.http;

import edu.wpi.cs.justice.cardmaker.model.Card;

public class ShowCardResponse {
	public Card card;
	public final int statusCode;
	public final String errorMessage;
	
	public ShowCardResponse(Card card,int statusCode) {
		this.errorMessage = "";
		this.statusCode = statusCode;
		this.card = card;
	}

	public ShowCardResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
}
