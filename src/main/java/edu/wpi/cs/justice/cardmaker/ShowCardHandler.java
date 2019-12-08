package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.wpi.cs.justice.cardmaker.db.CardDAO;
import edu.wpi.cs.justice.cardmaker.db.ElementDAO;

import edu.wpi.cs.justice.cardmaker.http.ShowCardRequest;
import edu.wpi.cs.justice.cardmaker.http.ShowCardResponse;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;


public class ShowCardHandler implements RequestStreamHandler{
	private LambdaLogger logger = null;
	private CardDAO cardDao = new CardDAO();
	private ElementDAO elementDAO = new ElementDAO();

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to show card");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
		ShowCardResponse response = null;

		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());

			body = event.get("body").toString();
			if (body == null) {
				body = event.toJSONString();  // this is only here to make testing easier
			}
		} catch (org.json.simple.parser.ParseException pe) {
			logger.log(pe.toString());
			response = new ShowCardResponse(pe.getMessage(), 400);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		
		if (!processed) {
			ShowCardRequest req = new Gson().fromJson(body, ShowCardRequest.class);
			String cardId = req.getCardId();
			
			try {
				Card card = cardDao.getCard(cardId);
				ArrayList<Page> pages = cardDao.getPage(cardId);
				for (Page page : pages){
					page.setTexts(elementDAO.getTexts(page.getPageId()));
					page.setImages(elementDAO.getImages(page.getPageId()));
				}
				card.setPages(pages);

				response = new ShowCardResponse(card, 200);
			} catch (Exception e) {
				response = new ShowCardResponse("Unable to show card: " +  e.getMessage() , 400);
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
