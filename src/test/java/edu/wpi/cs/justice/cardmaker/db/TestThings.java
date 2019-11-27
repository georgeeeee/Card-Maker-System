package edu.wpi.cs.justice.cardmaker.db;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;


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
	
	@Test
	public void testGetCard() {
		try {
			CardDAO cardDao = new CardDAO();
			Card card = cardDao.getCard("708e3312327c4b3296456f4e8e508e43");
			
			System.out.println(card.getEventType() + " " + card.getOrientation() + " " + card.getOrientation());
		} catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}

	@Test
	public void testGetPages() {
		try {
			CardDAO cardDao = new CardDAO();
			List<Page> pages = cardDao.getPage("ea21898ac38d4450b3d257bb1ee07b8f");
			
			for(Page p : pages) {
				System.out.println(p.getName());
			}
		} catch (Exception e) {
			fail ("didn't work:" + e.getMessage());
		}
	}
}

