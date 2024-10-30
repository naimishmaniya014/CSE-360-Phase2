package Utilities;

import models.Group;
import models.HelpArticle;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class HelpArticleDAOTest {

    public static void main(String[] args) {
        HelpArticleDAOTest tester = new HelpArticleDAOTest();
        tester.runTests();
    }

    public void runTests() {
        System.out.println("Running HelpArticleDAO tests...");

        try {
            setup();
            testAddHelpArticle();
            testGetAllHelpArticles();
            testGetHelpArticleById();
            testUpdateHelpArticle();
            testDeleteHelpArticle();
            testDeleteAllHelpArticles();
            testAssociateArticleWithGroup();
            testDissociateArticleFromGroup();
            testGetArticlesByGroupId();
            testGetGroupsByArticleId();
        } catch (SQLException e) {
            System.out.println("Database Error during tests: " + e.getMessage());
        }

        System.out.println("HelpArticleDAO tests completed.");
    }

    /**
     * Sets up the database for testing by resetting it.
     */
    private void setup() throws SQLException {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.resetDatabase();
        System.out.println("Database reset for HelpArticleDAO tests.");
    }

    /**
     * Tests adding a new help article.
     */
    public void testAddHelpArticle() throws SQLException {
        System.out.println("\nTest: Add HelpArticle");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article = new HelpArticle(
                0, "Header1",
                "Java Basics",
                "Introduction to Java",
                Arrays.asList("java", "programming"),
                "Java is a high-level programming language.",
                Arrays.asList("https://java.com")
        );
        helpArticleDAO.addHelpArticle(article);

        if (article.getId() > 0) {
            System.out.println("Passed: HelpArticle ID set correctly after insertion.");
        } else {
            System.out.println("Failed: HelpArticle ID not set.");
        }

        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId());
        if (retrieved != null && retrieved.getTitle().equals("Java Basics")) {
            System.out.println("Passed: HelpArticle retrieved successfully by ID.");
        } else {
            System.out.println("Failed: HelpArticle retrieval by ID unsuccessful.");
        }
    }

    /**
     * Tests retrieving all help articles.
     */
    public void testGetAllHelpArticles() throws SQLException {
        System.out.println("\nTest: Get All HelpArticles");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        helpArticleDAO.addHelpArticle(new HelpArticle(
                0, "Header1",
                "Java Basics",
                "Introduction to Java",
                Arrays.asList("java", "programming"),
                "Java is a high-level programming language.",
                Arrays.asList("https://java.com")
        ));
        helpArticleDAO.addHelpArticle(new HelpArticle(
                0, "Header2",
                "Python Basics",
                "Introduction to Python",
                Arrays.asList("python", "programming"),
                "Python is a versatile scripting language.",
                Arrays.asList("https://python.org")
        ));

        List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles();
        if (articles.size() == 2) {
            System.out.println("Passed: Retrieved correct number of HelpArticles.");
        } else {
            System.out.println("Failed: Incorrect number of HelpArticles retrieved.");
        }
    }

    /**
     * Tests retrieving a help article by its ID.
     */
    public void testGetHelpArticleById() throws SQLException {
        System.out.println("\nTest: Get HelpArticle By ID");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article = new HelpArticle(
                0, "Header3",
                "C++ Basics",
                "Introduction to C++",
                Arrays.asList("c++", "programming"),
                "C++ is a powerful system programming language.",
                Arrays.asList("https://cplusplus.com")
        );
        helpArticleDAO.addHelpArticle(article);

        HelpArticle retrieved = helpArticleDAO.getHelpArticleById(article.getId());
        if (retrieved != null && retrieved.getTitle().equals("C++ Basics")) {
            System.out.println("Passed: Successfully retrieved HelpArticle by ID.");
        } else {
            System.out.println("Failed: Could not retrieve HelpArticle by ID.");
        }
    }

    /**
     * Tests updating an existing help article.
     */
    public void testUpdateHelpArticle() throws SQLException {
        System.out.println("\nTest: Update HelpArticle");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article = new HelpArticle(
                0, "Header4",
                "Java Basics",
                "Introduction to Java",
                Arrays.asList("java", "programming"),
                "Java is a high-level programming language.",
                Arrays.asList("https://java.com")
        );
        helpArticleDAO.addHelpArticle(article);

        article.setTitle("Advanced Java");
        helpArticleDAO.updateHelpArticle(article);

        HelpArticle updated = helpArticleDAO.getHelpArticleById(article.getId());
        if (updated != null && updated.getTitle().equals("Advanced Java")) {
            System.out.println("Passed: HelpArticle updated successfully.");
        } else {
            System.out.println("Failed: HelpArticle update unsuccessful.");
        }
    }

    /**
     * Tests deleting a specific help article.
     */
    public void testDeleteHelpArticle() throws SQLException {
        System.out.println("\nTest: Delete HelpArticle");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article = new HelpArticle(
                0, "Header5",
                "Ruby Basics",
                "Introduction to Ruby",
                Arrays.asList("ruby", "programming"),
                "Ruby is a dynamic, open source programming language.",
                Arrays.asList("https://ruby-lang.org")
        );
        helpArticleDAO.addHelpArticle(article);

        helpArticleDAO.deleteHelpArticle(article.getId());

        HelpArticle deleted = helpArticleDAO.getHelpArticleById(article.getId());
        if (deleted == null) {
            System.out.println("Passed: HelpArticle deleted successfully.");
        } else {
            System.out.println("Failed: HelpArticle deletion unsuccessful.");
        }
    }

    /**
     * Tests deleting all help articles.
     */
    public void testDeleteAllHelpArticles() throws SQLException {
        System.out.println("\nTest: Delete All HelpArticles");
        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        helpArticleDAO.addHelpArticle(new HelpArticle(
                0, "Header6",
                "Go Basics",
                "Introduction to Go",
                Arrays.asList("go", "programming"),
                "Go is an open source programming language designed for simplicity.",
                Arrays.asList("https://golang.org")
        ));
        helpArticleDAO.addHelpArticle(new HelpArticle(
                0, "Header7",
                "Swift Basics",
                "Introduction to Swift",
                Arrays.asList("swift", "programming"),
                "Swift is a powerful and intuitive programming language for iOS.",
                Arrays.asList("https://swift.org")
        ));

        helpArticleDAO.deleteAllHelpArticles();

        List<HelpArticle> articles = helpArticleDAO.getAllHelpArticles();
        if (articles.isEmpty()) {
            System.out.println("Passed: All HelpArticles deleted successfully.");
        } else {
            System.out.println("Failed: Not all HelpArticles were deleted.");
        }
    }

    /**
     * Tests associating articles with groups.
     */
    public void testAssociateArticleWithGroup() throws SQLException {
        System.out.println("\nTest: Associate Articles with Group");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("cs");
        groupDAO.addGroup(group);

        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article1 = new HelpArticle(
                0, "Header8",
                "Java Streams",
                "Working with Java Streams",
                Arrays.asList("java", "streams"),
                "Streams provide a modern way to process collections.",
                Arrays.asList("https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html")
        );
        HelpArticle article2 = new HelpArticle(
                0, "Header9",
                "Python Generators",
                "Understanding Python Generators",
                Arrays.asList("python", "generators"),
                "Generators allow you to iterate over data without storing it all in memory.",
                Arrays.asList("https://docs.python.org/3/howto/generators.html")
        );
        helpArticleDAO.addHelpArticle(article1);
        helpArticleDAO.addHelpArticle(article2);

        helpArticleDAO.associateArticleWithGroup(article1.getId(), group.getId());
        helpArticleDAO.associateArticleWithGroup(article2.getId(), group.getId());

        List<HelpArticle> associatedArticles = helpArticleDAO.getArticlesByGroupId(group.getId());
        if (associatedArticles.size() == 2 &&
                associatedArticles.contains(article1) &&
                associatedArticles.contains(article2)) {
            System.out.println("Passed: Articles associated with group successfully.");
        } else {
            System.out.println("Failed: Articles association with group unsuccessful.");
        }
    }

    /**
     * Tests dissociating an article from a group.
     */
    public void testDissociateArticleFromGroup() throws SQLException {
        System.out.println("\nTest: Dissociate Article from Group");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("math");
        groupDAO.addGroup(group);

        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article1 = new HelpArticle(
                0, "Header10",
                "Calculus I",
                "Introduction to Calculus",
                Arrays.asList("calculus", "math"),
                "Calculus is the mathematical study of continuous change.",
                Arrays.asList("https://en.wikipedia.org/wiki/Calculus")
        );
        HelpArticle article2 = new HelpArticle(
                0, "Header11",
                "Linear Algebra",
                "Basics of Linear Algebra",
                Arrays.asList("linear algebra", "math"),
                "Linear algebra is the branch of mathematics concerning linear equations.",
                Arrays.asList("https://en.wikipedia.org/wiki/Linear_algebra")
        );
        helpArticleDAO.addHelpArticle(article1);
        helpArticleDAO.addHelpArticle(article2);

        helpArticleDAO.associateArticleWithGroup(article1.getId(), group.getId());
        helpArticleDAO.associateArticleWithGroup(article2.getId(), group.getId());

        // Dissociate article2
        helpArticleDAO.dissociateArticleFromGroup(article2.getId(), group.getId());

        List<HelpArticle> associatedArticles = helpArticleDAO.getArticlesByGroupId(group.getId());
        if (associatedArticles.size() == 1 && associatedArticles.contains(article1) && !associatedArticles.contains(article2)) {
            System.out.println("Passed: Article dissociated from group successfully.");
        } else {
            System.out.println("Failed: Article dissociation from group unsuccessful.");
        }
    }

    /**
     * Tests retrieving articles by group ID.
     */
    public void testGetArticlesByGroupId() throws SQLException {
        System.out.println("\nTest: Get Articles By Group ID");
        GroupDAO groupDAO = new GroupDAO();
        Group group = new Group("biology");
        groupDAO.addGroup(group);

        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article1 = new HelpArticle(
                0, "Header12",
                "Genetics",
                "Basics of Genetics",
                Arrays.asList("genetics", "biology"),
                "Genetics is the study of genes, genetic variation, and heredity.",
                Arrays.asList("https://en.wikipedia.org/wiki/Genetics")
        );
        HelpArticle article2 = new HelpArticle(
                0, "Header13",
                "Evolution",
                "Theory of Evolution",
                Arrays.asList("evolution", "biology"),
                "Evolution explains the diversity of life on Earth.",
                Arrays.asList("https://en.wikipedia.org/wiki/Evolution")
        );
        helpArticleDAO.addHelpArticle(article1);
        helpArticleDAO.addHelpArticle(article2);

        helpArticleDAO.associateArticleWithGroup(article1.getId(), group.getId());
        helpArticleDAO.associateArticleWithGroup(article2.getId(), group.getId());

        List<HelpArticle> articles = helpArticleDAO.getArticlesByGroupId(group.getId());
        if (articles.size() == 2 &&
                articles.contains(article1) &&
                articles.contains(article2)) {
            System.out.println("Passed: Retrieved articles by group ID successfully.");
        } else {
            System.out.println("Failed: Retrieving articles by group ID unsuccessful.");
        }
    }

    /**
     * Tests retrieving groups by article ID.
     */
    public void testGetGroupsByArticleId() throws SQLException {
        System.out.println("\nTest: Get Groups By Article ID");
        GroupDAO groupDAO = new GroupDAO();
        Group group1 = new Group("physics");
        Group group2 = new Group("engineering");
        groupDAO.addGroup(group1);
        groupDAO.addGroup(group2);

        HelpArticleDAO helpArticleDAO = new HelpArticleDAO();
        HelpArticle article = new HelpArticle(
                0, "Header14",
                "Thermodynamics",
                "Introduction to Thermodynamics",
                Arrays.asList("thermodynamics", "physics"),
                "Thermodynamics deals with heat and temperature.",
                Arrays.asList("https://en.wikipedia.org/wiki/Thermodynamics")
        );
        helpArticleDAO.addHelpArticle(article);

        helpArticleDAO.associateArticleWithGroup(article.getId(), group1.getId());
        helpArticleDAO.associateArticleWithGroup(article.getId(), group2.getId());

        List<Group> groups = helpArticleDAO.getGroupsByArticleId(article.getId());
        if (groups.size() == 2 &&
                groups.contains(group1) &&
                groups.contains(group2)) {
            System.out.println("Passed: Retrieved groups by article ID successfully.");
        } else {
            System.out.println("Failed: Retrieving groups by article ID unsuccessful.");
        }
    }
}
