package models;

import java.io.Serializable;
import java.util.List;

public class HelpArticle implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String header;
    private String title;
    private String shortDescription;
    private List<String> keywords;
    private String body;
    private List<String> referenceLinks;

    // Constructors
    public HelpArticle() {}

    public HelpArticle(long id, String header, String title, String shortDescription,
                      List<String> keywords, String body, List<String> referenceLinks) {
        this.id = id;
        this.header = header;
        this.title = title;
        this.shortDescription = shortDescription;
        this.keywords = keywords;
        this.body = body;
        this.referenceLinks = referenceLinks;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) { // Ensure this method exists
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) { // Ensure this method exists
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { // Ensure this method exists
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) { // Ensure this method exists
        this.shortDescription = shortDescription;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) { // Ensure this method exists
        this.keywords = keywords;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) { // Ensure this method exists
        this.body = body;
    }

    public List<String> getReferenceLinks() {
        return referenceLinks;
    }

    public void setReferenceLinks(List<String> referenceLinks) { // Ensure this method exists
        this.referenceLinks = referenceLinks;
    }

    // toString method for display purposes
    @Override
    public String toString() {
        return title;
    }
}
