package edu.wpi.cs.justice.cardmaker.http;

import java.util.List;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Image;
import edu.wpi.cs.justice.cardmaker.model.Page;
import edu.wpi.cs.justice.cardmaker.model.Text;

public class ShowCardResponse {
	private Card card;
	public final int statusCode;
	public final String errorMessage;
	
	public ShowCardResponse(Card card, int statusCode) {
		this.errorMessage = "";
		this.statusCode = statusCode;
		this.card = card;
	}

	public ShowCardResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
}
