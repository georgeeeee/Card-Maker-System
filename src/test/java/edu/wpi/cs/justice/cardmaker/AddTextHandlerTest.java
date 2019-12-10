package edu.wpi.cs.justice.cardmaker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Assert;
import org.junit.Test;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import edu.wpi.cs.justice.cardmaker.*;

public class AddTextHandlerTest extends LambdaTest {

	void testSuccessInput(String incoming) throws IOException {
		AddTextHandler handler = new AddTextHandler();
		
		InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("addText"));
        
        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals("200", outputNode.get("statusCode").asText());
	}
	
	void testFailInput(String incoming, String failureCode) throws IOException {
		AddTextHandler handler = new AddTextHandler();

        InputStream input = new ByteArrayInputStream(incoming.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("addText"));

        JsonNode outputNode = Jackson.fromJsonString(output.toString(), JsonNode.class);
        Assert.assertEquals(failureCode, outputNode.get("statusCode").asText());
    }
	
	@Test
	public void testShouldBeOkay() {
		String SAMPLE_INPUT_STRING = "{\"locationY\": \"0\", \"fontName\": \"Comic San\", \"fontType\": \"Bold\", \"locationX\": \"0\", \"fontSize\": \"10\", \"pageId\": \"88f429dfa6274a1194bd90b8da9e2a5c\", \"text\": \"TESTING\"}";
		try {
			testSuccessInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testShouldFailWrongLocation() {
		String SAMPLE_INPUT_STRING = "{\"locationY\": \"-10\", \"fontName\": \"GARBAGE\", \"fontType\": \"GARBAGE\", \"locationX\": \"-10\", \"fontSize\": \"10\", \"pageId\": \"GARBAGE\", \"text\": \"GARBAGE\"}";
		try {
			testFailInput(SAMPLE_INPUT_STRING, "400");
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testShouldFailWrongFontSize() {
		String SAMPLE_INPUT_STRING = "{\"locationY\": \"10\", \"fontName\": \"GARBAGE\", \"fontType\": \"GARBAGE\", \"locationX\": \"50\", \"fontSize\": \"-1\", \"pageId\": \"GARBAGE\", \"text\": \"GARBAGE\"}";
		try {
			testFailInput(SAMPLE_INPUT_STRING, "400");
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
}
