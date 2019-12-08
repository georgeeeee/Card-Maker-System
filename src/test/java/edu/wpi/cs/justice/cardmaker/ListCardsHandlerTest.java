package edu.wpi.cs.justice.cardmaker;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.http.*;

public class ListCardsHandlerTest extends LambdaTest{

	@Test
	public void testListCards() throws IOException {
		String stub = "";
		InputStream input = new ByteArrayInputStream(stub.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
		new ListCardsHandler().handleRequest(input, output, createContext("listCards"));
		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        ListCardsResponse resp = new Gson().fromJson(post.body, ListCardsResponse.class);
        System.out.println(resp);
        
        Assert.assertEquals(200, resp.statusCode);
	}
}
