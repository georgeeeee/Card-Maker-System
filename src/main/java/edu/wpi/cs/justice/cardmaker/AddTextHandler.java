package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.AddTextRequest;
import edu.wpi.cs.justice.cardmaker.http.AddTextResponse;
import edu.wpi.cs.justice.cardmaker.model.Text;
import util.Util;

/**
 * Handler to add text elements to a card
 *
 * @author justice509
 */
public class AddTextHandler implements RequestStreamHandler {
    LambdaLogger logger;

    /**
     * Load Text element to RDS
     *
     * @param textString
     * @param fontName
     * @param fontSize
     * @param locationX
     * @param locationY
     * @param pageId
     * @param fontType
     * @return
     * @throws Exception
     */
    Text AddText(String textString, String fontName, String fontSize, String locationX, String locationY, String pageId, String fontType) throws Exception {
        if (logger != null) {
            logger.log("in addText");
        }
        ElementDAO dao = new ElementDAO();

        final String elementId = Util.generateUniqueId();
        Text text = new Text(elementId, textString, fontName, fontSize, fontType, locationX, locationY);
        if (dao.addText(text)) {
            if (dao.addPageElement(text.getElementId(), locationX, locationY, pageId,null,null)) {
                return text;
            }
        }
        return text;
    }

    /**
     *
     * @param input
     * @param output
     * @param context
     * @throws IOException
     */
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

        AddTextResponse response = null;

        // extract body from incoming HTTP POST request. If any error, then return 422 error
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
            response = new AddTextResponse(pe.getMessage(), 400);  // unable to process input
            responseJson.put("body", new Gson().toJson(response));
            processed = true;
            body = null;
        }

        if (!processed) {
            AddTextRequest req = new Gson().fromJson(body, AddTextRequest.class);

            try {
            	// Ensuring location is valid
            	if ((Integer.valueOf(req.locationX) < 0) || (Integer.valueOf(req.locationY) < 0)) {
            		response = new AddTextResponse("Unable to add text: Invalid location values!", 400);
            	} else if (Integer.valueOf(req.fontSize) <= 0) {
            		response = new AddTextResponse("Unable to add text: Invalid font size values!", 400);
            	} else {
	                Text text = AddText(req.text, req.fontName, req.fontSize, req.locationX, req.locationY, req.pageId, req.fontType);
	                response = new AddTextResponse(text, 200);
            	}
            } catch (Exception e) {
                response = new AddTextResponse("Unable to add text: " + e.getMessage(), 400);
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