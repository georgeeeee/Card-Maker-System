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

public class ListImageHandlerTest extends LambdaTest{

	@Test
	public void testListImage() throws IOException {
		String stub = "";
		InputStream input = new ByteArrayInputStream(stub.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
		new ListImageHandler().handleRequest(input, output, createContext("listImage"));
		PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
        ListImageResponse resp = new Gson().fromJson(post.body, ListImageResponse.class);
        System.out.println(resp);
        
        Assert.assertEquals(200, resp.statusCode);
	}
}
