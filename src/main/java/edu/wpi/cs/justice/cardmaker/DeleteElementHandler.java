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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.DeleteElementRequest;
import edu.wpi.cs.justice.cardmaker.http.DeleteElementResponse;

/** Delete image or text elements in a page from RDS
 *
 *  @author justice509
 */
public class DeleteElementHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;

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
		logger.log("Loading Java Lambda handler to delete text");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json"); // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "DELETE,GET,POST,OPTIONS");
		headerJson.put("Access-Control-Allow-Origin", "*");

		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		DeleteElementResponse response = null;

		// extract body from incoming HTTP DELETE request. If any error, then return 422
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
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new DeleteElementResponse(400, "Bad Request:" + pe.getMessage()); // unable to process input
			responseJson.put("body", new Gson().toJson(response));
			processed = true;
			body = null;
		}

		if (!processed) {
			DeleteElementRequest req = new Gson().fromJson(body, DeleteElementRequest.class);
			logger.log(req.toString());

			ElementDAO dao = new ElementDAO();

			// See how awkward it is to call delete with an object, when you only
			// have one part of its information?
			String elementId = req.elementId;
			String pageId = req.pageId;

			try {
				if (dao.deletePageElement(elementId, pageId)) {
					dao.deleteText(elementId);
					response = new DeleteElementResponse(200);
				} else {
					response = new DeleteElementResponse(400,"unable to delete text");
				}
			} catch (Exception e) {
				response = new DeleteElementResponse(403, e.getMessage());
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
