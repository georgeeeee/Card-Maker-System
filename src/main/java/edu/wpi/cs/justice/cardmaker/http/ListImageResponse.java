package edu.wpi.cs.justice.cardmaker.http;

import java.util.ArrayList;

public class ListImageResponse {
	public  ArrayList<String> ImgList;
	public final int statusCode;
	public final String error;
	
	public ListImageResponse (ArrayList<String> list, int code) {
		this.ImgList = list;
		this.statusCode = code;
		this.error = "";
	}
	
	public ListImageResponse (String errorMessage, int code) {
		this.ImgList = null;
		this.statusCode = code;
		this.error = errorMessage;
	}
}

