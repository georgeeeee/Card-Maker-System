package edu.wpi.cs.justice.cardmaker.http;

import java.util.ArrayList;



public class ListImageResponse {
	public  ArrayList<String> ImgUrl;
	public final int statusCode;
	public final String error;
	
	public ListImageResponse (ArrayList<String> ImgUrl, int code) {
		this.ImgUrl = ImgUrl;
		this.statusCode = code;
		this.error = "";
	}
	
	public ListImageResponse (String errorMessage, int code) {
		this.ImgUrl = null;
		this.statusCode = code;
		this.error = errorMessage;
	}
}

