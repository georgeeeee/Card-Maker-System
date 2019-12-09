package edu.wpi.cs.justice.cardmaker;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.http.*;

public class EditTextHandlerTest extends LambdaTest{

	@Test
	public void testAddAndEditText() throws IOException {
		// Create Card
		// Add Text to Page
		String testText = "TEST2";
		String testFontName = "Arial";
		String testFontType = "Bold";
		String testFontSize = String.valueOf(new Random().nextInt(50));
		String testLocationX = String.valueOf(new Random().nextInt(100));
		String testLocationY = String.valueOf(new Random().nextInt(100));
		String testPageId = "bd86d01aab2a4f889cf76dda7c0c3c0c"; // Inner Right Page of TESTING CARD
		AddTextRequest atr = new AddTextRequest(testText, testFontName, testFontType, testLocationX, testLocationY, testPageId, testFontSize);
		
		String atRequest = new Gson().toJson(atr);
        String jsonRequest = new Gson().toJson(new PostRequest(atRequest));
        
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
		new AddTextHandler().handleRequest(input, output, createContext("addText"));
		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        AddTextResponse resp = new Gson().fromJson(post.body, AddTextResponse.class);
        System.out.println(resp);
        
        Assert.assertEquals(200, resp.statusCode);
        Assert.assertEquals(testText, resp.text.getText());
        Assert.assertEquals(testFontName, resp.text.getFontName());
        Assert.assertEquals(testFontType, resp.text.getFontType());
        Assert.assertEquals(testFontSize, resp.text.getFontSize());
        Assert.assertEquals(testLocationX, resp.text.getLocationX());
        Assert.assertEquals(testLocationY, resp.text.getLocationY());
        
        String testElementId = resp.text.getElementId();
        
        // Edit Text
        EditTextRequest etr = new EditTextRequest(testFontName, testFontType, testFontSize, testLocationX, testLocationY, testPageId, testElementId, "TEST2EDIT");
        
        atRequest = new Gson().toJson(etr);
        jsonRequest = new Gson().toJson(new PostRequest(atRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        
        new EditTextHandler().handleRequest(input, output, createContext("editText"));
        
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        EditTextResponse e_resp = new Gson().fromJson(post.body, EditTextResponse.class);
        Assert.assertEquals(200, e_resp.statusCode);
	}
	
}
