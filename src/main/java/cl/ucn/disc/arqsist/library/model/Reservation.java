package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

/**
 * A member's request to borrow a particular book.
 */
@DatabaseTable(tableName = "reservations")
public final class Reservation {

    /** Database-generated reservation identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Member who made the reservation. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    /** Book reserved by the member. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    /** Reservation creation date stored in ISO-8601 form. */
    @DatabaseField(canBeNull = false)
    private String reservedAt;

    /** Whether the reservation has been converted into a loan. */
    @DatabaseField
    private boolean fulfilled;

    /** Creates an empty reservation for ORMLite. */
    public Reservation() {
    }

    /**
     * Creates a reservation using an ISO-8601 date string.
     *
     * @param member member making the reservation.
     * @param book book being reserved.
     * @param reservedAt reservation creation date.
     */
    public Reservation(Member member, Book book, String reservedAt) {
        this.member = member;
        this.book = book;
        this.reservedAt = reservedAt;
        this.fulfilled = false;
    }

    /**
     * Creates a reservation using a date value.
     *
     * @param member member making the reservation.
     * @param book book being reserved.
     * @param reservedAt reservation creation date.
     */
    public Reservation(Member member, Book book, LocalDate reservedAt) {
        this(member, book, reservedAt.toString());
    }

    /**
     * Returns the database identifier.
     *
     * @return reservation identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database identifier.
     *
     * @param id reservation identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member who made the reservation.
     *
     * @return associated member.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member who made the reservation.
     *
     * @param member associated member.
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Returns the reserved book.
     *
     * @return associated book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the reserved book.
     *
     * @param book associated book.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns the reservation creation date as stored.
     *
     * @return ISO-8601 reservation date.
     */
    public String getReservedAt() {
        return reservedAt;
    }

    /**
     * Sets the reservation creation date as an ISO-8601 string.
     *
     * @param reservedAt ISO-8601 reservation date.
     */
    public void setReservedAt(String reservedAt) {
        this.reservedAt = reservedAt;
    }

    /**
     * Indicates whether the reservation has been fulfilled.
     *
     * @return {@code true} if fulfilled; otherwise {@code false}.
     */
    public boolean isFulfilled() {
        return fulfilled;
    }

    /**
     * Sets whether the reservation has been fulfilled.
     *
     * @param fulfilled {@code true} if fulfilled; otherwise {@code false}.
     */
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
}
