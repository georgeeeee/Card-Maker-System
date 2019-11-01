package edu.wpi.cs.justice.cardmaker.model;

public class Card {
	final String cardId;
	String eventType;
	String recipient;
	String orientation;
	
	public Card(String cardId, String eventType, String recipient, String orientation) {
		this.cardId = cardId;
		this.eventType = eventType;
		this.recipient = recipient;
		this.orientation = orientation;
	}
}
