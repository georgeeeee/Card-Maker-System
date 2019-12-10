package edu.wpi.cs.justice.cardmaker.http;

public class DeleteElementResponse {
    public final int statusCode;
    public final String error;

    public DeleteElementResponse(int statusCode) {
        this.statusCode = statusCode;
        this.error = "";
    }

    // 200 means success
    public DeleteElementResponse(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

}
