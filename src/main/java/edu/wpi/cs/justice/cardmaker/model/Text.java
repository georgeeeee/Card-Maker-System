package edu.wpi.cs.justice.cardmaker.model;

public class Text{
    final String elementId;
    String text;
    String fontName;
    String fontSize;
    String fontType;

    public Text(String elementId,String text,String fontName,String fontSize,String fontType){
        this.elementId = elementId;
        this.text = text;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontType = fontType;
    }
    public String getFontType() {
        return this.fontType;
    }
    public void setFontType(String fontType) {
        this.fontType = fontType;
    }
    public String getFontSize() {
        return this.fontSize;
    }
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getFontName() {
        return this.fontName;
    }
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }
	public String getElementId() {
		return this.elementId;
	}
}