package edu.wpi.cs.justice.cardmaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.ListImageResponse;
import edu.wpi.cs.justice.cardmaker.model.ImageUrl;

public class ListImageHandler implements RequestStreamHandler {
    public LambdaLogger logger = null;

    /**
     * Load from RDS, if it exists
     * 
     * @throws Exception
     */
    ArrayList<ImageUrl> getImages() throws Exception {
        if (logger != null) {
            logger.log("in getImages");
        }
        ElementDAO dao = new ElementDAO();

        return dao.getAllImage();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        logger = context.getLogger();
        logger.log("Loading Java Lambda handler to list all cards");

        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type", "application/json"); // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin", "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        ListImageResponse response;
        try {
            ArrayList<ImageUrl> list = getImages();
			response = new ListImageResponse(list, 200);
		} catch (Exception e) {
			response = new ListImageResponse(e.getMessage(), 403);
		}

		// compute proper response
        responseJson.put("body", new Gson().toJson(response));  
        responseJson.put("statusCode", response.statusCode);
        
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
    }

}