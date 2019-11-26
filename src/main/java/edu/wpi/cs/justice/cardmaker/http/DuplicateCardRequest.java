package edu.wpi.cs.justice.cardmaker.http;

public class DuplicateCardRequest{
    public final String cardId;
	
	public DuplicateCardRequest (String cardId) {
		this.cardId = cardId;
	}
}