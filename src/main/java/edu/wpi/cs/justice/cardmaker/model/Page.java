package edu.wpi.cs.justice.cardmaker.model;

import java.util.ArrayList;

public class Page {
	final String cardId;
	final String pageId;
	String name;
	public ArrayList<Text> texts;
	public ArrayList<Image> images;

	public Page(String cardId, String pageId, String name) {
		super();
		this.cardId = cardId;
		this.pageId = pageId;
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public String getPageId() {
		return pageId;
	}

	public String getPageName() {
		return name;
	}

	public void setPageName(String name) {
		this.name = name;
	}
	public ArrayList<Text> getTexts() {
		return this.texts;
	}
	public void setTexts(ArrayList<Text> texts) {
		this.texts = texts;
	}
	public ArrayList<Image> getImages() {
		return this.images;
	}
	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}	

}
