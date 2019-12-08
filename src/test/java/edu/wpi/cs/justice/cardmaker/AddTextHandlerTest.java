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
		String SAMPLE_INPUT_STRING = "{\"locationY\": \"0\", \"fontName\": \"Arial\", \"fontType\": \"Bold\", \"locationX\": \"0\", \"fontSize\": \"10\", \"pageId\": \"0\", \"text\": \"Test\"}";
		try {
			testSuccessInput(SAMPLE_INPUT_STRING);
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
	
	@Test
	public void testShouldFail() {
		String SAMPLE_INPUT_STRING = "{\"locationY\": \"0\", \"fontName\": \"Arial\", \"fontType\": \"Bold\", \"locationX\": \"0\", \"fontSize\": \"10\", \"pageId\": \"0\", \"text\": \"Test\"}";
		try {
			testFailInput(SAMPLE_INPUT_STRING, "400");
		} catch (IOException ioe) {
			Assert.fail("Invalid:" + ioe.getMessage());
		}
	}
}
