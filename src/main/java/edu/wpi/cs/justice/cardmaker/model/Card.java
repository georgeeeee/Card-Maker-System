package edu.wpi.cs.justice.cardmaker.model;

import java.util.ArrayList;

/** Card Object
 *
 *  @author justice509
 */
public class Card {
	final String cardId;
	String eventType;
	String recipient;
	String orientation;
	ArrayList<Page> pages;
	
	public Card(String cardId, String eventType, String recipient, String orientation) {
		this.cardId = cardId;
		this.eventType = eventType;
		this.recipient = recipient;
		this.orientation = orientation;
		this.pages = new ArrayList<Page>();
	}
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getCardId() {
		return cardId;
	}

	public ArrayList<Page> getPages() {
		return this.pages;
	}
	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
	}
}
