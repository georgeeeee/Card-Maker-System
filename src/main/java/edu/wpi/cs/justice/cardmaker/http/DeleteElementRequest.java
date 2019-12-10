package edu.wpi.cs.justice.cardmaker.http;

public class DeleteElementRequest {
    public final String elementId;
    public final String pageId;

    public DeleteElementRequest(String pageId, String elementId) {
        this.pageId = pageId;
        this.elementId = elementId;
    }

}
