package edu.wpi.cs.justice.cardmaker.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.cs.justice.cardmaker.model.Image;
import edu.wpi.cs.justice.cardmaker.model.Page;
import edu.wpi.cs.justice.cardmaker.model.Text;
import util.Util;

public class ElementDAO {
    java.sql.Connection conn;

    public ElementDAO() {
        try {
            conn = DatabaseUtil.connect();
        } catch (Exception e) {
            conn = null;
        }
    }

    public boolean addText(Text text) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO elements (element_id, type, text, font_name,font_size,font_type,imageUrl) values(?,?,?,?,?,?,?);");
            ps.setString(1, text.getElementId());
            ps.setString(2, "text");
            ps.setString(3, text.getText());
            ps.setString(4, text.getFontName());
            ps.setString(5, text.getFontSize());
            ps.setString(6, text.getFontType());
            ps.setString(7, null);
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add text: " + e.getMessage());
        }
    }
    public boolean addImage(Image image) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO elements (element_id, type, text, font_name,font_size,font_type,imageUrl) values(?,?,?,?,?,?,?);");
            ps.setString(1, image.getElementId());
            ps.setString(2, "image");
            ps.setString(3, null);
            ps.setString(4, null);
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setString(7, image.getImageUrl());
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add text: " + e.getMessage());
        }
    }

    public boolean addPageElement(String elementId, String locationX, String locationY, String pageId,String width,String height) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO pageElements (element_id, page_id, location_X, location_Y,width,height) values(?,?,?,?,?,?);");
            ps.setString(1, elementId);
            ps.setString(2, pageId);
            ps.setString(3, locationX);
            ps.setString(4, locationY);
            ps.setString(5, width);
            ps.setString(6, height);
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add pageElement: " + e.getMessage());
        }
    }
    public ArrayList<String> getPageElement(String  pageId) throws Exception{
        ArrayList<String> ElementIds = new ArrayList<String>();
        try {
            String query = "SELECT * FROM pageElements WHERE page_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, pageId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String elementId = resultSet.getString("element_id");
                ElementIds.add(elementId);
            }
            return ElementIds;
        } catch (Exception e) {
            throw new Exception("Failed to get pageElement: " + e.getMessage());
        }

    }
    public boolean duplicatePageElement(String elementId,String pageId,String newPageId) throws Exception{
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM pageElements WHERE element_id = ? AND page_id = ?");
            ps.setString(1, elementId);
            ps.setString(2, pageId);
            ps.execute();

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String locationX = resultSet.getString("location_X");
                String locationY = resultSet.getString("location_Y");
                String width = resultSet.getString("width");
                String height = resultSet.getString("height");
                addPageElement(elementId, locationX, locationY, newPageId, width, height);
            }
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to duplicate pageElement: " + e.getMessage());
        }

    }
    public boolean duplicateText(Text text) throws Exception{
        try {
            String newElementId = util.Util.generateUniqueId();
            addText(new Text(newElementId, text.getText(), text.getFontName(), text.getFontSize(), text.getFontType(), text.getLocationX(), text.getLocationY()));
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to duplicate text " + e.getMessage());
        }
    }
    public boolean duplicateImage(Image image) throws Exception{
        try {
            String newElementId = util.Util.generateUniqueId();
            addImage(new Image(newElementId, image.getImageUrl(), image.getLocationX(), image.getLocationY(),image.getWidth(),image.getHeight()));
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to duplicate image " + e.getMessage());
        }
    }
    public ArrayList<Text> getTexts(String pageId) throws Exception {
        ArrayList<Text> texts = new ArrayList<Text>();
        String query = "SELECT location_X, location_Y, elements.element_id, text, font_name, font_size, font_type " +
                "FROM pageElements " +
                "inner join elements " +
                "WHERE pageElements.element_id = elements.element_id " +
                "AND page_id = ?" +
                "AND type = 'text'";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, pageId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String elementId = resultSet.getString("element_id");
                String locationX = resultSet.getString("location_X");
                String locationY = resultSet.getString("location_Y");
                String text = resultSet.getString("text");
                String fontName = resultSet.getString("font_name");
                String fontSize = resultSet.getString("font_size");
                String fontType = resultSet.getString("font_type");
                texts.add(new Text(elementId, text, fontName, fontSize, fontType, locationX, locationY));
            }
        } catch (SQLException e) {
            throw new Exception("Failed to get texts: " + e.getMessage());
        }
        return texts;
    }

    public ArrayList<Image> getImages(String pageId) throws Exception {
        ArrayList<Image> images = new ArrayList<Image>();
        String query = "SELECT location_X, location_Y, elements.element_id, imageUrl, width, height " +
                "FROM pageElements " +
                "inner join elements " +
                "WHERE pageElements.element_id = elements.element_id " +
                "AND page_id = ?" +
                "AND type = 'image'";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, pageId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String elementId = resultSet.getString("element_id");
                String locationX = resultSet.getString("location_X");
                String locationY = resultSet.getString("location_Y");
                String imageUrl = resultSet.getString("imageUrl");
                String width = resultSet.getString("width");
                String height = resultSet.getString("height");
                images.add(new Image(elementId, imageUrl, locationX, locationY, width, height));
            }
        } catch (SQLException e) {
            throw new Exception("Failed to get images: " + e.getMessage());
        }
        return images;
    }

    public boolean deleteText(String elementId, String pageId) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM pageElements WHERE element_id = ? AND page_id = ?;");
            ps.setString(1, elementId);
            ps.setString(2, pageId);
            int numAffected = ps.executeUpdate();
            ps.close();
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to delete text: " + e.getMessage());
        }
    }
}
