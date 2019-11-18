
package edu.wpi.cs.justice.cardmaker.http;

public class DeleteTextRequest {
    public final String elementId;
    public final String pageId;

    public DeleteTextRequest(String pageId, String elementId) {
        this.pageId = pageId;
        this.elementId = elementId;
    }

}