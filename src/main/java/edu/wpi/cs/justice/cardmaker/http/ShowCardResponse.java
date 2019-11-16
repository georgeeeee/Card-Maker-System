package edu.wpi.cs.justice.cardmaker.http;

import edu.wpi.cs.justice.cardmaker.model.Card;

public class ShowCardResponse {
	public Card card;
	public final int statusCode;
	public final String errorMessage;
	
	public ShowCardResponse(Card card,int statusCode) {
		super();
		this.card = card;
		this.statusCode = statusCode;
		this.errorMessage = "";
	}

	public ShowCardResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
}
