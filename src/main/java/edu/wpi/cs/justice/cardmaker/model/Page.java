package edu.wpi.cs.justice.cardmaker.model;

import java.util.List;

public class Page {
	final String cardId;
	final String pageId;
	String name;
	List<Text> texts;
	List<Image> images;

	public Page(String cardId, String pageId, String name) {
		super();
		this.cardId = cardId;
		this.pageId = pageId;
		this.name = name;
		this.texts = null;
		this.images = null;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
