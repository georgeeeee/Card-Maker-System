package edu.wpi.cs.justice.cardmaker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CardMakerHandlerTest {

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

//    @Test
//    public void testCardMakerHandler() throws IOException {
//       CreateCardHandler handler = new CreateCardHandler();
//
//        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
//        OutputStream output = new ByteArrayOutputStream();
//
//        handler.handleRequest(input, output, null);
//
//        // TODO: validate output here if needed.
//        String sampleOutputString = output.toString();
//        System.out.println(sampleOutputString);
//        Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
//    }
    
    @Test
	public void testGeneratePresignedUrl() {
		try {
			URL url = util.Util.GeneratePresignedUrl("123.png", "justice509");
			Assert.assertNotNull(url);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
//    @Test
//    public void testDeleteFileInS3() {
//    	try {
//    		Assert.assertTrue(util.Util.DeleteS3File("justice509", "123.png"));
//    	} catch (Exception e) {
//    		System.out.println(e.getMessage());
//    	}
//    }
}
