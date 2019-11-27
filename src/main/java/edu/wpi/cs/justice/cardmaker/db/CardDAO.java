package edu.wpi.cs.justice.cardmaker.db;

import java.sql.*;
import java.util.ArrayList;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Image;
import edu.wpi.cs.justice.cardmaker.model.Page;
import edu.wpi.cs.justice.cardmaker.model.Text;
import util.Util;

public class CardDAO {
    java.sql.Connection conn;

    public CardDAO() {
        try {
            conn = DatabaseUtil.connect();
        } catch (Exception e) {
            conn = null;
        }
    }

    public boolean addCard(Card card) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cards (card_id, event_type, recipient, orientation) values(?,?,?,?);");
            ps.setString(1, card.getCardId());
            ps.setString(2, card.getEventType());
            ps.setString(3, card.getRecipient());
            ps.setString(4, card.getOrientation());

            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add card: " + e.getMessage());
        }
    }
    
    public boolean addPages(ArrayList<Page> pages) throws Exception {
    	try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO pages () values(?,?,?);");
            
            for (Page page : pages) {
            	ps.clearParameters();
            	ps.setString(1,  page.getPageId());
                ps.setString(2,  page.getCardId());
                ps.setString(3,  page.getPageName());

                ps.addBatch();
            }
            
            ps.executeBatch();

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
    
    public ArrayList<Page> getPage(String cardId) throws Exception {
    	String query = "SELECT * FROM pages WHERE card_id = ?";
    	try {
    		PreparedStatement ps = conn.prepareStatement(query);
    		ps.setString(1, cardId);
    		ResultSet resultSet = ps.executeQuery();
    		
    		ArrayList<Page> pages = new ArrayList<Page>();
    		while (resultSet.next()) {
    			String pageId  = resultSet.getString("page_id");
                String name  = resultSet.getString("name");
                pages.add(new Page(cardId, pageId, name));
            }
    		
    		resultSet.close();
            ps.close();	
            return pages;
    	} catch(Exception e) {
    		throw new Exception("Failed to get pages: " + e.getMessage());
    	}
    }

    public String getCardId(String pageId) throws Exception {
        String query = "SELECT card_id FROM pages WHERE page_id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, pageId);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getString(1);

        } catch (SQLException e) {
            throw new Exception("Failed to get cardId by pageId: " + e.getMessage());
        }
    }
    
    public Card getCard(String cardId) throws Exception {
    	String query = "SELECT * FROM cards WHERE card_id = ?";
    	try {
    		PreparedStatement ps = conn.prepareStatement(query);
    		ps.setString(1, cardId);
    		ResultSet resultSet = ps.executeQuery();
    		
    		resultSet.next();
    		String eventType  = resultSet.getString("event_type");
            String recipient  = resultSet.getString("recipient");
            String orientation  = resultSet.getString("orientation");
            
            resultSet.close();
            ps.close();
            return new Card(cardId, eventType, recipient, orientation);
    	} catch(Exception e) {
    		throw new Exception("Failed to get card: " + e.getMessage());
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
        String cardId = resultSet.getString("card_id");
        String eventType = resultSet.getString("event_type");
        String recipient = resultSet.getString("recipient");
        String orientation = resultSet.getString("orientation");
        return new Card(cardId, eventType, recipient, orientation);
    }

    private Page generatePage(ResultSet resultSet) throws SQLException {
        String pageId = resultSet.getString("page_id");
        String cardId = resultSet.getString("card_id");
        String name = resultSet.getString("name");
        return new Page(cardId, pageId, name);
    }

    private Image generateImage(ResultSet elementSet) throws SQLException {
        String elementId = elementSet.getString("element_id");
        String imageUrl = elementSet.getString("imageUrl");
        String locationY = elementSet.getString("location_Y");
        String width = elementSet.getString("width");
        String height = elementSet.getString("height");
        String locationX = elementSet.getString("location_X");
        return new Image(elementId, imageUrl, locationX, locationY, width, height);
    }
    private Text generateText(ResultSet elementSet) throws SQLException {
        String elementId = elementSet.getString("element_id");
        String text = elementSet.getString("text");
        String fontName = elementSet.getString("font_name");
        String fontType = elementSet.getString("font_type");
        String fontSize = elementSet.getString("font_size");
        String locationX = elementSet.getString("location_X");
        String locationY = elementSet.getString("location_Y");
        return new Text(elementId,text,fontName,fontSize,fontType,locationX,locationY);
    }

	public Card duplicateCard(String cardId) throws Exception {
		try {
            Card card = getCard(cardId) ;
            String newcardId = util.Util.generateUniqueId();

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO cards (card_id, event_type, recipient, orientation) values(?,?,?,?);");
            ps.setString(1, newcardId);
            ps.setString(2, card.getEventType());
            ps.setString(3, card.getRecipient());
            ps.setString(4, card.getOrientation());

            ps.execute();
            ps.close();
            
            return new Card(newcardId, card.getEventType(), card.getRecipient(), card.getOrientation());
    	} catch(Exception e) {
    		throw new Exception("Failed to duplicate card: " + e.getMessage());
    	}
	}

	public boolean duplicatePage(ArrayList<Page> orignPages) throws Exception{
        try {
            ElementDAO elementDAO = new ElementDAO();
            ArrayList<Page> duplicatepages = new ArrayList<Page>();
            //duplicate page
            for(Page page: orignPages){
                String newPageId = util.Util.generateUniqueId();
                duplicatepages.add(new Page(page.getCardId(),newPageId,page.getName()));
            }
            addPages(duplicatepages);

            for(Page page: orignPages){
                ArrayList<String> elementIds = elementDAO.getPageElement(page.getPageId());
                //duplicate Page Elements
                for(String elementId:elementIds){
                    for(Page duplicatepage:duplicatepages){
                        if(duplicatepage.getPageName() == page.getPageName()){
                            elementDAO.duplicatePageElement(elementId,page.getPageId(),duplicatepage.getPageId());
                        }
                    }
                //duplicate text
                for(Text text:page.getTexts()){
                        elementDAO.duplicateText(text);
                    }
                //duplicate Image
                for(Image image:page.getImages()){
                        elementDAO.duplicateImage(image);
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            throw new Exception("Failed to duplicate page: " + e.getMessage());
        }
	}
}
