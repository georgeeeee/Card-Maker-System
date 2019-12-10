package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.CardDAO;
import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.EditImageRequest;
import edu.wpi.cs.justice.cardmaker.http.EditImageResponse;

public class EditImageHandler implements RequestStreamHandler{
	LambdaLogger logger;
    ElementDAO elementDAO;
    CardDAO cardDAO;

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
        elementDAO = new ElementDAO();
        cardDAO = new CardDAO();

        // set up response
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type", "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin", "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        EditImageResponse response = null;
        
        String body;
        boolean processed = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            JSONParser parser = new JSONParser();
            JSONObject event = (JSONObject) parser.parse(reader);
            logger.log("event:" + event.toJSONString());

            body = (String) event.get("body");
            if (body == null) {
                body = event.toJSONString();  // this is only here to make testing easier
            }
        } catch (org.json.simple.parser.ParseException pe) {
            logger.log(pe.toString());
            response = new EditImageResponse(pe.getMessage(), 400);  // unable to process input
            responseJson.put("body", new Gson().toJson(response));
            processed = true;
            body = null;
        }

        if (!processed) {
            EditImageRequest req = new Gson().fromJson(body, EditImageRequest.class);
            URL presignedUrl = null;
            
            try {
            	if(Boolean.parseBoolean(req.isReplaceImage)) {
            		String imageUrl = util.Util.generateS3BucketUrl(req.fileName);
            		elementDAO.UpdateImageUrl(imageUrl, req.elementId);         		
            		presignedUrl = util.Util.GeneratePresignedUrl(req.fileName, "justice509");
            		System.out.println("Presigned url: " + presignedUrl.toString());
            	}
            	elementDAO.UpdateImage(req.elementId, req.pageId, req.locationX, req.locationY, req.width, req.height);  	
            	
            	response = new EditImageResponse(200, presignedUrl);
            } catch (Exception e) {
                response = new EditImageResponse("Unable to update image: " + e.getMessage(), 400);
            }
        }

        responseJson.put("body", new Gson().toJson(response));
        responseJson.put("statusCode", response.statusCode);

        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();	
	}	
}