package edu.wpi.cs.justice.cardmaker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import edu.wpi.cs.justice.cardmaker.db.CardDAO;
import edu.wpi.cs.justice.cardmaker.db.ElementDAO;
import edu.wpi.cs.justice.cardmaker.http.*;
import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Page;
import edu.wpi.cs.justice.cardmaker.model.Text;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;

/** Edit a text element and update it in the RDS
 * Edit either the text, locationX, locationY, fontName, fontType, fontSize
 *
 *  @author justice509
 */
public class EditTextHandler implements RequestStreamHandler{
    LambdaLogger logger;
    ElementDAO elementDAO;
    CardDAO cardDAO;

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
        elementDAO = new ElementDAO();
        cardDAO = new CardDAO();

        // set up response
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type", "application/json");  // not sure if needed anymore?
        headerJson.put("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        headerJson.put("Access-Control-Allow-Origin", "*");

        JSONObject responseJson = new JSONObject();
        responseJson.put("headers", headerJson);

        EditTextResponse response = null;

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
            response = new EditTextResponse(pe.getMessage(), 400);  // unable to process input
            responseJson.put("body", new Gson().toJson(response));
            processed = true;
            body = null;
        }

        if (!processed) {
            EditTextRequest req = new Gson().fromJson(body, EditTextRequest.class);

            try {
                elementDAO.UpdateText(new Text(req.elementId, req.text, req.fontName, req.fontSize, req.fontType, req.locationX, req.locationY), req.pageId);
                Card card = cardDAO.getCard(cardDAO.getCardId(req.pageId));
                ArrayList<Page> pages = cardDAO.getPage(card.getCardId());
                for (Page page : pages){
                    page.setTexts(elementDAO.getTexts(page.getPageId()));
                    page.setImages(elementDAO.getImages(page.getPageId()));
                }
                card.setPages(pages);

                response = new EditTextResponse(card, 200);
            } catch (Exception e) {
                response = new EditTextResponse("Unable to update text: " + e.getMessage(), 400);
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