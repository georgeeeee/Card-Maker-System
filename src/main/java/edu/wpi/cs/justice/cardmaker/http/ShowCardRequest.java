package edu.wpi.cs.justice.cardmaker.http;

public class ShowCardRequest {
	private final String cardId;

	public ShowCardRequest(String cardId) {
		super();
		this.cardId = cardId;
	}

	public String getCardId() {
		return cardId;
	}
}
