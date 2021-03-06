package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.CardDAO;
import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.DuplicateCardRequest;
import edu.wpi.cs.justice.cardmaker.http.DuplicateCardResponse;
import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;
import util.Util;

/** Duplicate a card based on cardID and assign it to a new recipient
 *
 * @author justice509
 */
public class DuplicateCardHandler implements RequestStreamHandler {
	LambdaLogger logger;

	/** Load the card from RDS and all its pages and elements
	 * Create a duplicate the card for a new recipient to the RDS
	 *
	 * @param cardId
	 * @param recipientName
	 * @return
	 * @throws Exception
	 */
	Card duplicateCard(String cardId, String recipientName) throws Exception {
        try {
            if (logger != null) { logger.log("!duplicateCard"); }
        CardDAO cardDAO = new CardDAO();
        ElementDAO elementDAO =new ElementDAO();

        Card duplicateCard = cardDAO.duplicateCard(cardId,recipientName);
        ArrayList<Page>originPage = cardDAO.getPage(cardId);
        for (Page page : originPage){
            page.setTexts(elementDAO.getTexts(page.getPageId()));
            page.setImages(elementDAO.getImages(page.getPageId()));
        }

        if(cardDAO.duplicatePage(duplicateCard.getCardId(),originPage)){
            return duplicateCard;
        }
        } catch (Exception e) {
            throw new Exception("Failed to duplicate card: " + e.getMessage());
        }
        return null; // shouldn't be here
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
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin",  "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DuplicateCardResponse response = null;

		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
	        
			body = (String)event.get("body");
			if (body == null) {
				body = event.toJSONString();  // this is only here to make testing easier
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new DuplicateCardResponse(pe.getMessage(), 400);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		
		if (!processed) {
			DuplicateCardRequest req = new Gson().fromJson(body, DuplicateCardRequest.class);
			
			try {
				Card card = duplicateCard(req.cardId,req.recipientName);
				if (card != null) {
					response = new DuplicateCardResponse(card, 200);
				} else {
//					response = new CreateCardResponse("", );
				}
			} catch (Exception e) {
				response = new DuplicateCardResponse("Unable to duplicate card: " +  e.getMessage() , 400);
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
