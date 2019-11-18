package edu.wpi.cs.justice.cardmaker.http;

public class DeleteTextResponse {
    public final int statusCode;
    public final String error;

    public DeleteTextResponse(int statusCode) {
        this.statusCode = statusCode;
        this.error = "";
    }

    // 200 means success
    public DeleteTextResponse(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.error = errorMessage;
    }

}