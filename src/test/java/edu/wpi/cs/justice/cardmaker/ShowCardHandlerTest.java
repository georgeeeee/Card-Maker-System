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

public class ShowCardHandlerTest extends LambdaTest{

	void testSuccessInput(String incoming) throws IOException {
		ShowCardHandler handler = new ShowCardHandler();
		
		InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("showCard"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(200, outputNode.get("statusCode").intValue());
	}

	void testFailInput(String incoming, int failureCode) throws IOException {
		ShowCardHandler handler = new ShowCardHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("createCard"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(failureCode, outputNode.get("statusCode").intValue());
    }

	@Test
	public void testShouldBeOk() {
		String SAMPLE_INPUT_STRING = "{\"cardId\":\"xxxxxxxxxxx\"}";
		try {
			testSuccessInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testShouldFail() {
		String SAMPLE_INPUT_STRING = "{\"cardId\":\"xxxxxxxxxxx\"}";
		try {
			testSuccessInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
}
