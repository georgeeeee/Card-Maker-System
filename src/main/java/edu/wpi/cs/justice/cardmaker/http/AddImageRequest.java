package edu.wpi.cs.justice.cardmaker.http;

public class AddImageRequest {
	public String pageId;
	public String locationX;
	public String locationY;
	public String width;
	public String height;
	public String imgUrl;

	public AddImageRequest(String width, String height, String locationX, String locationY, String pageId,
			String imgUrl) {
		this.width = width;
		this.height = height;
		this.pageId = pageId;
		this.locationX = locationX;
		this.locationY = locationY;
		this.imgUrl = imgUrl;
	}
}