package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A catalog book and its total and available copy counts.
 */
@DatabaseTable(tableName = "books")
public final class Book {

    /** Database-generated book identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Book title. */
    @DatabaseField(canBeNull = false)
    private String title;

    /** Book author or authors. */
    @DatabaseField(canBeNull = false)
    private String author;

    /** International Standard Book Number. */
    @DatabaseField(canBeNull = false)
    private String isbn;

    /** Total number of copies owned by the library. */
    @DatabaseField(canBeNull = false)
    private int totalCopies;

    /** Number of copies currently available for checkout. */
    @DatabaseField(canBeNull = false)
    private int availableCopies;

    /** Creates an empty book for ORMLite. */
    public Book() {
    }

    /**
     * Creates a book and initializes its available copies to the total copies.
     *
     * @param title book title.
     * @param author book author or authors.
     * @param isbn book ISBN.
     * @param totalCopies total number of copies owned.
     */
    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    /**
     * Returns the database identifier.
     *
     * @return book identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database identifier.
     *
     * @param id book identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the book title.
     *
     * @return book title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the book title.
     *
     * @param title book title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the book author or authors.
     *
     * @return author or authors.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the book author or authors.
     *
     * @param author author or authors.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the book ISBN.
     *
     * @return book ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the book ISBN.
     *
     * @param isbn book ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the total number of copies owned.
     *
     * @return total copy count.
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Sets the total number of copies owned.
     *
     * @param totalCopies total copy count.
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * Returns the number of copies available for checkout.
     *
     * @return available copy count.
     */
    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Sets the number of copies available for checkout.
     *
     * @param availableCopies available copy count.
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
