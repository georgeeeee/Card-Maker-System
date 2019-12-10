package edu.wpi.cs.justice.cardmaker;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.http.*;

public class DuplicateCardHandlerTest extends LambdaTest{

	@Test
	public void testCreateAndDuplicateCard() throws IOException {
		// Create Card
		String event = "Test Event";
        String recipient = "Test Recipient";
        String orientation = "Portrait";
		CreateCardRequest ccr = new CreateCardRequest(event, recipient, orientation);
		
		String ccRequest = new Gson().toJson(ccr);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
		new CreateCardHandler().handleRequest(input, output, createContext("createCard"));
		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateCardResponse resp = new Gson().fromJson(post.body, CreateCardResponse.class);
        System.out.println(resp);
        
        Assert.assertEquals(200, resp.statusCode);
        String cardId = resp.card.getCardId();
        
        // Duplicate Card
        DuplicateCardRequest dcr = new DuplicateCardRequest(cardId, recipient);
        
        ccRequest = new Gson().toJson(dcr);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        
        new DuplicateCardHandler().handleRequest(input, output, createContext("duplicateCard"));
        
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        DuplicateCardResponse d_resp = new Gson().fromJson(post.body, DuplicateCardResponse.class);
        Assert.assertEquals(200, d_resp.statusCode);
        Assert.assertEquals(event, d_resp.card.getEventType());
        Assert.assertEquals(recipient, d_resp.card.getRecipient());
        Assert.assertEquals(orientation, d_resp.card.getOrientation());
	}

}
