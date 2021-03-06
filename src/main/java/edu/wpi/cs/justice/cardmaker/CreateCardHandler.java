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
import edu.wpi.cs.justice.cardmaker.http.CreateCardRequest;
import edu.wpi.cs.justice.cardmaker.http.CreateCardResponse;
import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;
import util.Util;

/**
 * Create a new card and store it in RDS

 * @author justice509
 */
public class CreateCardHandler implements RequestStreamHandler {
	LambdaLogger logger;

	/** Load card to RDS
	 *
	 * @param eventType
	 * @param recipient
	 * @param orientation
	 * @return
	 * @throws Exception
	 */
	Card createCard(String eventType, String recipient, String orientation) throws Exception {
		if (logger != null) { logger.log("in createCard"); }
		CardDAO dao = new CardDAO();
		
		final String cardId = Util.generateUniqueId();
		Card card = new Card (cardId, eventType, recipient, orientation);
		
		if (dao.addCard(card)) {
			return card;
		}
		
		return card;
	}

	/** Adds all four pages to a card in the RDS once it is created
	 *
	 * @param card
	 * @throws Exception
	 */
	public void addPageRequest(Card card) throws Exception{
		CardDAO dao = new CardDAO();
		ArrayList<Page> pages = new ArrayList<Page>();
		
		for(String pageName: Util.pageNames){
				pages.add(new Page(card.getCardId(), Util.generateUniqueId(), pageName));
		}
		
		dao.addPages(pages);
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

		CreateCardResponse response = null;

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
			response = new CreateCardResponse(pe.getMessage(), 400);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		
		if (!processed) {
			CreateCardRequest req = new Gson().fromJson(body, CreateCardRequest.class);
			
			try {
				Card card = createCard(req.eventType, req.recipient, req.orientation);
				if (card != null) {
					addPageRequest(card);
					response = new CreateCardResponse(card, 200);
				} else {
//					response = new CreateCardResponse("", );
				}
			} catch (Exception e) {
				response = new CreateCardResponse("Unable to create card: " +  e.getMessage() , 400);
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
