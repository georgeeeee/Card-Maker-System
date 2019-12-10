package edu.wpi.cs.justice.cardmaker.http;

import java.util.ArrayList;

import edu.wpi.cs.justice.cardmaker.model.ImageUrl;



public class ListImageResponse {
	public  ArrayList<ImageUrl> ImgList;
	public final int statusCode;
	public final String error;
	
	public ListImageResponse (ArrayList<ImageUrl> ImgUrl, int code) {
		this.ImgList = ImgUrl;
		this.statusCode = code;
		this.error = "";
	}
	
	public ListImageResponse (String errorMessage, int code) {
		this.ImgList = null;
		this.statusCode = code;
		this.error = errorMessage;
	}
}

