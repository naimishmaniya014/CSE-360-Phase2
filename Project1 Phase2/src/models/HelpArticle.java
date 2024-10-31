package models;

import java.io.Serializable;
import java.util.List;

/**
 * <p> Title: HelpArticle Class </p>
 * 
 * <p> Description: This class represents a HelpArticle entity within the application.
 * It encapsulates all relevant details about a help article, including its ID, header, title, 
 * short description, keywords, body content, and reference links. The class provides constructors, 
 * getters, setters, and a utility method for string representation. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class HelpArticle implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String header;
    private String title;
    private String shortDescription;
    private List<String> keywords;
    private String body;
    private List<String> referenceLinks;

    /**
     * Default constructor for HelpArticle.
     */
    public HelpArticle() {}

    /**
     * Constructs a HelpArticle with all fields specified.
     *
     * @param id              The unique identifier for the article.
     * @param header          The header of the article.
     * @param title           The title of the article.
     * @param shortDescription A brief description of the article.
     * @param keywords        A list of keywords associated with the article.
     * @param body            The main content of the article.
     * @param referenceLinks  A list of reference links related to the article.
     */
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

    /**
     * Constructs a HelpArticle without specifying the ID.
     *
     * @param header          The header of the article.
     * @param title           The title of the article.
     * @param shortDescription A brief description of the article.
     * @param keywords        A list of keywords associated with the article.
     * @param body            The main content of the article.
     * @param referenceLinks  A list of reference links related to the article.
     */
    public HelpArticle(String header, String title, String shortDescription,
                      List<String> keywords, String body, List<String> referenceLinks) {
        this.header = header;
        this.title = title;
        this.shortDescription = shortDescription;
        this.keywords = keywords;
        this.body = body;
        this.referenceLinks = referenceLinks;
    }

    /**
     * Retrieves the ID of the help article.
     *
     * @return The article's ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the help article.
     *
     * @param id The article's ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the header of the help article.
     *
     * @return The article's header.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header of the help article.
     *
     * @param header The article's header.
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Retrieves the title of the help article.
     *
     * @return The article's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the help article.
     *
     * @param title The article's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the short description of the help article.
     *
     * @return The article's short description.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the short description of the help article.
     *
     * @param shortDescription The article's short description.
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Retrieves the list of keywords associated with the help article.
     *
     * @return The list of keywords.
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Sets the list of keywords associated with the help article.
     *
     * @param keywords The list of keywords.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Retrieves the body content of the help article.
     *
     * @return The article's body content.
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body content of the help article.
     *
     * @param body The article's body content.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Retrieves the list of reference links related to the help article.
     *
     * @return The list of reference links.
     */
    public List<String> getReferenceLinks() {
        return referenceLinks;
    }

    /**
     * Sets the list of reference links related to the help article.
     *
     * @param referenceLinks The list of reference links.
     */
    public void setReferenceLinks(List<String> referenceLinks) {
        this.referenceLinks = referenceLinks;
    }

    /**
     * Returns a string representation of the help article.
     *
     * @return The article's title.
     */
    @Override
    public String toString() {
        return title;
    }
}
