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

public class EditImageHandlerTest extends LambdaTest{

	@Test
	public void testEditImage() throws IOException {
		// Edit existing image with changing image name
		String testImage = "1234.png"; // Original image: test1.png
		String testLocationX = String.valueOf(new Random().nextInt(100));
		String testLocationY = String.valueOf(new Random().nextInt(100));
		String testWidth = String.valueOf(new Random().nextInt(50));
		String testHeight = String.valueOf(new Random().nextInt(50));
		String testPageId = "bd86d01aab2a4f889cf76dda7c0c3c0c"; // Inner Right Page of TESTING CARD
		String testElementId = "dd76607b63804d5897aa81d9cfe0f506";

        EditImageRequest etr = new EditImageRequest(testWidth, testHeight, testLocationX, testLocationY, testPageId, testImage, testElementId, "true");
        
        String eiRequest = new Gson().toJson(etr);
        String jsonRequest = new Gson().toJson(new PostRequest(eiRequest));
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        new EditImageHandler().handleRequest(input, output, createContext("editImage"));
        
        PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        EditImageResponse e_resp = new Gson().fromJson(post.body, EditImageResponse.class);
        Assert.assertEquals(200, e_resp.statusCode);
        
        // Now edit existing image without changing image name
        etr = new EditImageRequest(testWidth, testHeight, testPageId, testLocationX, testLocationY, "test1.png", testElementId, "false");
        
        eiRequest = new Gson().toJson(etr);
        jsonRequest = new Gson().toJson(new PostRequest(eiRequest));
        
        input = new ByteArrayInputStream(jsonRequest.getBytes());
        output = new ByteArrayOutputStream();
        
        new EditImageHandler().handleRequest(input, output, createContext("editImage"));
        
        post = new Gson().fromJson(output.toString(), PostResponse.class);
        e_resp = new Gson().fromJson(post.body, EditImageResponse.class);
        Assert.assertEquals(200, e_resp.statusCode);
	}

}
