package edu.wpi.cs.justice.cardmaker.db;

import java.sql.PreparedStatement;

import edu.wpi.cs.justice.cardmaker.model.Text;

public class TextDAO {
	java.sql.Connection conn;
	
    public TextDAO() {    	
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
	public boolean addText(Text text) throws Exception{
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO elements (element_id, type, text, font_name,font_size,font_type,imageUrl) values(?,?,?,?,?,?,?);");
            ps.setString(1,  text.getElementId());
            ps.setString(2,  "text");
            ps.setString(3,  text.getText());
            ps.setString(4,  text.getFontName());
            ps.setString(5,  text.getFontSize());
            ps.setString(6,  text.getFontType());
            ps.setString(7, null);
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add text: " + e.getMessage());
        }
	}

	public boolean addPageElement(Text text, String locationX, String locationY, String pageId) throws Exception{
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO pageElements (element_id, page_id, location_X, location_Y,width,height) values(?,?,?,?,?,?);");
            ps.setString(1,  text.getElementId());
            ps.setString(2,  pageId);
            ps.setString(3,  locationX);
            ps.setString(4,  locationY);
            ps.setString(5,  null);
            ps.setString(6,  null);
            ps.execute();
            return true;

        } catch (Exception e) {
            throw new Exception("Failed to add pageElement: " + e.getMessage());
        }
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
            throw new Exception("Failed to delete: " + e.getMessage());
        }
	}

}
