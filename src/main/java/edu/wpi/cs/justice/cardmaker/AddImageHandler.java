package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.AddImageRequest;
import edu.wpi.cs.justice.cardmaker.http.AddImageResponse;
import edu.wpi.cs.justice.cardmaker.http.AddTextRequest;
import edu.wpi.cs.justice.cardmaker.model.Image;
import util.Util;

public class AddImageHandler implements RequestStreamHandler {
	private AmazonS3 s3;
	LambdaLogger logger;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();

		// set up response
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		AddImageResponse httpResponse = null;

		// extract body from incoming HTTP POST request. If any error, then return 422
		// error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			body = (String) event.get("body");
			if (body == null) {
				body = event.toJSONString(); // this is only here to make testing easier
			}
		} catch (org.json.simple.parser.ParseException pe) {
			logger.log(pe.toString());
			httpResponse = new AddImageResponse(pe.getMessage(), 400); // unable to process input
			responseJson.put("body", new Gson().toJson(httpResponse));
			processed = true;
			body = null;
		}

		if (!processed) {
			AddImageRequest req = new Gson().fromJson(body, AddImageRequest.class);

            try {
				URL url = GeneratePresignedUrl(req.fileName, "justice509");
				ElementDAO elementDAO = new ElementDAO();
				String newImgId= util.Util.generateUniqueId();
				Image newImage = new Image(newImgId, url.toString(), req.locationX, req.locationY, req.width, req.height);
				elementDAO.addImage(newImage);
				elementDAO.addPageElement(newImgId, req.locationX, req.locationY, req.pageId, req.width, req.height);
    			httpResponse = new AddImageResponse(url, 200);
            } catch (Exception e) {
            	httpResponse = new AddImageResponse("Unable to add image: " + e.getMessage(), 400);
            }
		}

		responseJson.put("body", new Gson().toJson(httpResponse));
		responseJson.put("statusCode", httpResponse.statusCode);

		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
	}

	public URL GeneratePresignedUrl(String objectKey, String bucketName) throws Exception {
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
				.withRegion(Regions.US_EAST_1).build();

		// Set the pre-signed URL to expire after one hour.
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);

		// Generate the pre-signed URL.
		System.out.println("Generating pre-signed URL.");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
				.withMethod(HttpMethod.PUT).withExpiration(expiration);
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

		System.out.println("Pre-Signed URL: " + url.toString());

		return url;

	}
}