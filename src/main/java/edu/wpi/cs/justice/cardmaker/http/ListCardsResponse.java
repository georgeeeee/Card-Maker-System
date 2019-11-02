package edu.wpi.cs.justice.cardmaker.http;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.justice.cardmaker.model.Card;

public class ListCardsResponse {
	public final List<Card> cardsList;
	public final int statusCode;
	public final String error;
	
	public ListCardsResponse (List<Card> cards, int code) {
		this.cardsList = cards;
		this.statusCode = code;
		this.error = "";
	}
	
	public ListCardsResponse (String errorMessage, int code) {
		this.cardsList = new ArrayList<Card>();
		this.statusCode = code;
		this.error = errorMessage;
	}
}

