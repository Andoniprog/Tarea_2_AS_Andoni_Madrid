package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

@DatabaseTable(tableName = "loans")
public final class Loan {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    @DatabaseField(canBeNull = false)
    private String loanDate;

    @DatabaseField(canBeNull = false)
    private String dueDate;

    @DatabaseField
    private String returnDate;

    @DatabaseField
    private boolean returned;

    @DatabaseField
    private double overdueFee;

    public Loan() {
    }

    public Loan(Member member, Book book, String loanDate, String dueDate) {
        this.member = member;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.overdueFee = 0.0;
    }

    public Loan(Member member, Book book, LocalDate loanDate, LocalDate dueDate) {
        this(member, book, loanDate.toString(), dueDate.toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return LocalDate.parse(loanDate);
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate.toString();
    }

    public LocalDate getDueDate() {
        return LocalDate.parse(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate.toString();
    }

    public LocalDate getReturnDate() {
        return returnDate == null ? null : LocalDate.parse(returnDate);
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate == null ? null : returnDate.toString();
    }

    public boolean isReturned() {
        return returned;
    }

    public boolean isOverdue() {
        return !returned && getDueDate().isBefore(LocalDate.now());
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public double getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(double overdueFee) {
        this.overdueFee = overdueFee;
    }
}
