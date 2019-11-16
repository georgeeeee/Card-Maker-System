package edu.wpi.cs.justice.cardmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;

import edu.wpi.cs.justice.cardmaker.db.CardDAO;
import edu.wpi.cs.justice.cardmaker.http.ShowCardRequest;
import edu.wpi.cs.justice.cardmaker.http.ShowCardResponse;
import edu.wpi.cs.justice.cardmaker.model.Card;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class ShowCardHandler implements RequestStreamHandler{
	public LambdaLogger logger = null;
	
	Card ShowCard(String cardId)throws Exception{
		if (logger != null) { logger.log("in showCard"); }
		CardDAO dao = new CardDAO();
		
        return dao.getCard(cardId);
	}

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
					
			body = (String)event.get("body");
			if (body == null) {
			body = event.toJSONString();  // this is only here to make testing easier
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new ShowCardResponse(pe.getMessage(), 400);  // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}
		if (!processed) {
			ShowCardRequest req = new Gson().fromJson(body, ShowCardRequest.class);
			
			try {
				Card card = ShowCard(req.getCardId());
				if (card != null) {
					response = new ShowCardResponse(card, 200);
				} else {
//					response = new CreateCardResponse("", );
				}
			} catch (Exception e) {
				response = new ShowCardResponse("Unable to show card: " +  e.getMessage() , 400);
			}
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
