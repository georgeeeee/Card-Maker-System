package edu.wpi.cs.justice.cardmaker.http;

public class EditImageRequest {
	public String pageId;
	public String elementId;
	public String locationX;
	public String locationY;
	public String width;
	public String height;
	public String fileName;
	public String isReplaceImage;

	public EditImageRequest(String width, String height, String locationX, String locationY, String pageId,
			String fileName, String elementId, String isReplaceImage) {
		this.width = width;
		this.height = height;
		this.pageId = pageId;
		this.locationX = locationX;
		this.locationY = locationY;
		this.fileName = fileName;
		this.elementId = elementId;
		this.isReplaceImage = isReplaceImage;
	}
}