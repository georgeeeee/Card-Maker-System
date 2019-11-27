package edu.wpi.cs.justice.cardmaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;

import edu.wpi.cs.justice.cardmaker.http.AddImageResponse;

public class AddImageHandler implements RequestStreamHandler{
	private AmazonS3 s3;
	LambdaLogger logger;
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();

        // set up response
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type", "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin", "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);
        
        AddImageResponse httpResponse = null;
	}
}