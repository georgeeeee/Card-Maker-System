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

public class AddImageHandlerTest extends LambdaTest{

	void testSuccessInput(String incoming) throws IOException {
		AddImageHandler handler = new AddImageHandler();
		
		InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("addImage"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
	}
	
	void testFailInput(String incoming, String failureCode) throws IOException {
		AddImageHandler handler = new AddImageHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("addImage"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(failureCode, outputNode.get("statusCode").asText());
    }
	
	@Test
	public void testShouldBeOkay() {
		String SAMPLE_INPUT_STRING = "{\"locationX\": \"0\", \"locationY\": \"0\", \"width\": \"40\", \"height\": \"10\", \"pageId\": \"88f429dfa6274a1194bd90b8da9e2a5c\", \"fileName\": \"test1.png\"}";
		try {
			testSuccessInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
//	@Test
//	public void testShouldBeOkay2() {
//		String SAMPLE_INPUT_STRING = "{\"locationX\": \"0\", \"locationY\": \"0\", \"width\": \"0\", \"height\": \"0\", \"pageId\": \"bd86d01aab2a4f889cf76dda7c0c3c0c\", \"fileName\": \"test1.png\"}";
//		try {
//			testSuccessInput(SAMPLE_INPUT_STRING);
//		} catch (IOException ioe) {
//			Assert.fail("Invalid:" + ioe.getMessage());
//		}
//	}
	
	@Test
	public void testShouldFailWrongLocation() {
		String SAMPLE_INPUT_STRING = "{\"locationX\": \"-50\", \"locationY\": \"-30\", \"width\": \"40\", \"height\": \"10\", \"pageId\": \"88f429dfa6274a1194bd90b8da9e2a5c\", \"fileName\": \"test1.png\"}";
		try {
			testFailInput(SAMPLE_INPUT_STRING, "400");
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testShouldFailWrongDimension() {
		String SAMPLE_INPUT_STRING = "{\"locationX\": \"0\", \"locationY\": \"0\", \"width\": \"-50\", \"height\": \"20\", \"pageId\": \"88f429dfa6274a1194bd90b8da9e2a5c\", \"fileName\": \"test1.png\"}";
		try {
			testFailInput(SAMPLE_INPUT_STRING, "400");
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}

}
