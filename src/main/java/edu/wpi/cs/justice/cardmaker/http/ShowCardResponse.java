package edu.wpi.cs.justice.cardmaker.http;

import java.util.List;

import edu.wpi.cs.justice.cardmaker.model.Card;
import edu.wpi.cs.justice.cardmaker.model.Image;
import edu.wpi.cs.justice.cardmaker.model.Page;

public class ShowCardResponse {
	private Card card;
	private List<Page> pages;
	private List<Text> texts;
	private List<Image> images;
	private final int statusCode;
	private final String errorMessage;
	
	public ShowCardResponse(Card card, List<Page> pages, List<Text> texts, List<Image> images, int statusCode) {
		super();
		this.card = card;
		this.pages = pages;
		this.texts = texts;
		this.images = images;
		this.statusCode = statusCode;
		this.errorMessage = "";
	}

	public ShowCardResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
}
