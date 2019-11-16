package edu.wpi.cs.justice.cardmaker.db;

import java.sql.*;
import java.util.ArrayList;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Image;
import edu.wpi.cs.justice.cardmaker.model.Page;
import edu.wpi.cs.justice.cardmaker.model.Text;

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

    public boolean addPage(Page page) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO pages () values(?,?,?);");
            ps.setString(1, page.getPageId());
            ps.setString(2, page.getCardId());
            ps.setString(3, page.getPageName());

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
    public Card getCard(String cardId) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            ps.setString(1, cardId);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (resultSet.next()) {
                Card c = generateCard(resultSet);
                resultSet.close();
                ps.close();

                PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM pages WHERE card_id = ?");
                ps2.setString(1, cardId);
                ps2.execute();
                ResultSet pageSet = ps.getResultSet();
                while (pageSet.next()) {
                    Page page = generatePage(pageSet);
                    PreparedStatement ps3 = conn.prepareStatement(
                            "SELECT * FROM pageElement p,elements e WHERE page_id = ? AND p.element_id = e.element_id");
                    ps3.setString(1, page.getPageId());
                    ps3.execute();
                    ResultSet elementSet = ps3.getResultSet();
                    while (elementSet.next()) {
                        if (elementSet.getString("type") == "text") {
                            page.texts.add(generateText(elementSet));
                        }
                        else if(elementSet.getString("type") == "image"){
                            page.images.add(generateImage(elementSet));
                        }
                        
                    }
                    elementSet.close();
                    ps3.close();
                    c.pages.add(page);
                }
                pageSet.close();
                ps2.close();
                return c;
            }
            return null;
        } catch (Exception e) {
            throw new Exception("Failed to show card: " + e.getMessage());
        }
    }
}
