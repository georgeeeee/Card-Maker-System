package edu.wpi.cs.justice.cardmaker.http;

public class CreateCardRequest {
	public final String eventType;
	public final String recipient;
	public final String orientation;
	
	public CreateCardRequest (String eventType, String recipient, String orientation) {
		this.eventType = eventType;
		this.recipient = recipient;
		this.orientation = orientation;
	}
}
