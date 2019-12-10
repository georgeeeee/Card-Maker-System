package edu.wpi.cs.justice.cardmaker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.http.*;

public class DeleteCardHandlerTest extends LambdaTest{

	@Test
	public void testCreateAndDeleteCard() throws IOException {
		// Create Card
		CreateCardRequest ccr = new CreateCardRequest("Test Event", "Test Recipient", "Portrait");
		
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
        
        // Delete Card
        DeleteCardRequest dcr = new DeleteCardRequest(cardId);
        
        ccRequest = new Gson().toJson(dcr);
        jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        
        new DeleteCardHandler().handleRequest(input, output, createContext("deleteCard"));
        
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        DeleteCardResponse d_resp = new Gson().fromJson(post.body, DeleteCardResponse.class);
        Assert.assertEquals(200, d_resp.statusCode);
        
        // Try deleting again, should fail
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        
        new DeleteCardHandler().handleRequest(input, output, createContext("deleteCard"));
        
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        d_resp = new Gson().fromJson(post.body, DeleteCardResponse.class);
        Assert.assertEquals(400, d_resp.statusCode);
	}

}
