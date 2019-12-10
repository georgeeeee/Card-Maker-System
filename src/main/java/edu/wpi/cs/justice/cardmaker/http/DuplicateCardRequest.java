package edu.wpi.cs.justice.cardmaker.http;

public class DuplicateCardRequest{
    public final String cardId;
	public String recipientName;
	public DuplicateCardRequest (String cardId,String recipientName) {
		this.cardId = cardId;
		this.recipientName = recipientName;
	}
}