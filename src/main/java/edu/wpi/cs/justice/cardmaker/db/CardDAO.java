package edu.wpi.cs.justice.cardmaker.db;

import java.sql.*;
import java.util.ArrayList;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;


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
            ps.setString(3,  card.getRecipient());
            ps.setString(4,  card.getOrientation());
            
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add card: " + e.getMessage());
        }
    }
    
    public boolean addPage(Page page) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO pages () values(?,?,?);");
            ps.setString(1,  page.getPageId());
            ps.setString(2,  page.getCardId());
            ps.setString(3,  page.getPageName());
            
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add page: " + e.getMessage());
        }
    }

    public boolean deleteCard(String cardId) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM cards WHERE card_id = ?;");
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

	public Card getCard(String cardId) throws Exception{
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            ps.setString(1, cardId);
            ps.execute();
            ResultSet resultSet= ps.getResultSet();
            Card c = generateCard(resultSet);
            resultSet.close();
            ps.close();
            return c;

        } catch (Exception e) {
            throw new Exception("Failed to show card: " + e.getMessage());
        }
	}

}
