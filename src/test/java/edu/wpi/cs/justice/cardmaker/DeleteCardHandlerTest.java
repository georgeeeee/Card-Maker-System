package edu.wpi.cs.justice.cardmaker;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.http.*;

public class DeleteCardHandlerTest extends LambdaTest{

	@Test
	public void testCreateAndDeleteCard() throws IOException {
		// Create Card
		CreateCardRequest ccr = new CreateCardRequest("Test Event", "Test Recipient", "portrait");
		
		String ccRequest = new Gson().toJson(ccr);
        String jsonRequest = new Gson().toJson(new PostRequest(ccRequest));
        
		InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
		new CreateCardHandler().handleRequest(input, output, createContext("createCard"));
		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        CreateCardResponse resp = new Gson().fromJson(post.body, CreateCardResponse.class);
        System.out.println(resp);
        
        
	}

}
