package models;

import java.io.Serializable;
import java.util.List;

public class GroupWithArticles implements Serializable {
    private static final long serialVersionUID = 1L;

    private Group group;
    private List<HelpArticle> articles;

    public GroupWithArticles(Group group, List<HelpArticle> articles) {
        this.group = group;
        this.articles = articles;
    }

    public Group getGroup() {
        return group;
    }

    public List<HelpArticle> getArticles() {
        return articles;
    }
}
