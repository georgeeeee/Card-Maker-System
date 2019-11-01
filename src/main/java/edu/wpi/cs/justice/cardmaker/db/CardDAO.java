package edu.wpi.cs.justice.cardmaker.db;

import java.sql.PreparedStatement;

import edu.wpi.cs.justice.cardmaker.model.Card;


public class CardDAO {
	java.sql.Connection conn;

    public CardDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public boolean addCard(Card card) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO cards (card_id, event_type, recipient, orientation) values(?,?,?,?);");
            ps.setString(1,  card.getCardId());
            ps.setString(2,  card.getEventType());
            ps.setString(2,  card.getRecipient());
            ps.setString(2,  card.getOrientation());
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add card: " + e.getMessage());
        }
    }

}
