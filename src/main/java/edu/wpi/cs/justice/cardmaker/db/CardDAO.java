package edu.wpi.cs.justice.cardmaker.db;

import java.sql.*;
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import edu.wpi.cs.justice.cardmaker.model.Card;


public class CardDAO {
	java.sql.Connection conn;

	LambdaLogger logger;
	
    public CardDAO() {
    	if (logger != null) { logger.log("in db creation"); }
    	
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    		if (logger != null) { logger.log(e.getMessage()); }
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

    public boolean deleteCard(String cardId) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM constants WHERE card_id = ?;");
            ps.setString(1, cardId);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete: " + e.getMessage());
        }
    }
    
    public ArrayList<Card> getAllCards() throws Exception {
        
        ArrayList<Card> cards = new ArrayList<Card>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM cards";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Card c = generateCard(resultSet);
                cards.add(c);
            }
            resultSet.close();
            statement.close();
            return cards;

        } catch (Exception e) {
            throw new Exception("Failed to get cards: " + e.getMessage());
        }
    }
    
    private Card generateCard(ResultSet resultSet) throws Exception {
    	String cardId  = resultSet.getString("card_id");
        String eventType  = resultSet.getString("event_type");
        String recipient  = resultSet.getString("recipient");
        String orientation  = resultSet.getString("orientation");
        return new Card (cardId, eventType, recipient, orientation);
    }

}
