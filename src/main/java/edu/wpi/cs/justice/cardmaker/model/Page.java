package edu.wpi.cs.justice.cardmaker.model;

public class Page {
	final String cardId;
	final String pageId;
	String name;

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

}
