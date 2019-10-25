package edu.wpi.cs.justice.cardmaker.model;

public class Card {
	int cardId;
	String eventType;
	String recipient;
	String orientation;
	
	public Card(String eventType, String recipient, String orientation) {
		this.cardId = randomizer();
		this.eventType = eventType;
		this.recipient = recipient;
		this.orientation = orientation;
	}

	private int randomizer() {
		
		return 0;
	}
}
