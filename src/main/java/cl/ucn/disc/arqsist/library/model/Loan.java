package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

/**
 * A book checkout linking a member and a book over a loan period.
 */
@DatabaseTable(tableName = "loans")
public final class Loan {

    /** Database-generated loan identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Member who borrowed the book. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    /** Book that was borrowed. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    /** Loan start date stored in ISO-8601 form. */
    @DatabaseField(canBeNull = false)
    private String loanDate;

    /** Loan due date stored in ISO-8601 form. */
    @DatabaseField(canBeNull = false)
    private String dueDate;

    /** Return date stored in ISO-8601 form, or {@code null} while open. */
    @DatabaseField
    private String returnDate;

    /** Whether the loan has been returned. */
    @DatabaseField
    private boolean returned;

    /** Late fee charged when the loan is returned after its due date. */
    @DatabaseField
    private double overdueFee;

    /** Creates an empty loan for ORMLite. */
    public Loan() {
    }

    /**
     * Creates a loan using ISO-8601 date strings.
     *
     * @param member member borrowing the book.
     * @param book book being borrowed.
     * @param loanDate loan start date.
     * @param dueDate due date.
     */
    public Loan(Member member, Book book, String loanDate, String dueDate) {
        this.member = member;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.overdueFee = 0.0;
    }

    /**
     * Creates a loan using date values.
     *
     * @param member member borrowing the book.
     * @param book book being borrowed.
     * @param loanDate loan start date.
     * @param dueDate due date.
     */
    public Loan(Member member, Book book, LocalDate loanDate, LocalDate dueDate) {
        this(member, book, loanDate.toString(), dueDate.toString());
    }

    /**
     * Returns the database identifier.
     *
     * @return loan identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the database identifier.
     *
     * @param id loan identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the borrowing member.
     *
     * @return member associated with this loan.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the borrowing member.
     *
     * @param member member associated with this loan.
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Returns the borrowed book.
     *
     * @return book associated with this loan.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the borrowed book.
     *
     * @param book book associated with this loan.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns the loan start date.
     *
     * @return loan start date.
     */
    public LocalDate getLoanDate() {
        return LocalDate.parse(loanDate);
    }

    /**
     * Sets the loan start date.
     *
     * @param loanDate loan start date.
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate.toString();
    }

    /**
     * Returns the loan due date.
     *
     * @return due date.
     */
    public LocalDate getDueDate() {
        return LocalDate.parse(dueDate);
    }

    /**
     * Sets the loan due date.
     *
     * @param dueDate due date.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate.toString();
    }

    /**
     * Returns the date the loan was returned.
     *
     * @return return date, or {@code null} if the loan is still open.
     */
    public LocalDate getReturnDate() {
        return returnDate == null ? null : LocalDate.parse(returnDate);
    }

    /**
     * Sets the loan return date.
     *
     * @param returnDate return date, or {@code null} if the loan is open.
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate == null ? null : returnDate.toString();
    }

    /**
     * Indicates whether the loan has been returned.
     *
     * @return {@code true} if returned; otherwise {@code false}.
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Indicates whether this open loan is overdue according to the system date.
     *
     * @return {@code true} if the loan is open and past due; otherwise {@code false}.
     */
    public boolean isOverdue() {
        return !returned && getDueDate().isBefore(LocalDate.now());
    }

    /**
     * Sets whether the loan has been returned.
     *
     * @param returned {@code true} if returned; otherwise {@code false}.
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Returns the late fee stored for this loan.
     *
     * @return stored overdue fee.
     */
    public double getOverdueFee() {
        return overdueFee;
    }

    /**
     * Sets the late fee stored for this loan.
     *
     * @param overdueFee overdue fee amount.
     */
    public void setOverdueFee(double overdueFee) {
        this.overdueFee = overdueFee;
    }
}
