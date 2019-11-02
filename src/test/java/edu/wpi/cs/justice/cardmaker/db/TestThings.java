package edu.wpi.cs.justice.cardmaker.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.justice.cardmaker.model.Card;


public class TestThings {
	CardDAO cd = new CardDAO();
	@Test
	public void testFind() {
	    try {
	    	ArrayList<Card> cl = cd.getAllCards();
	    	System.out.println("testing connection "+ cl);
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}
//	
	@Test
	public void testCreate() {
	    try {
	    	final String cardId = UUID.randomUUID().toString().substring(0, 32);
			Card card = new Card (cardId, "eventType", "recipient", "orientation");
			
	    	boolean b = cd.addCard(card);
	    	System.out.println("add addCard: " + b);
	    	
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}

}

