package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Loan use cases: checkout, return, listing and overdue detection.
 * The inventory is always changed through the {@link BookService}.
 */
public final class LoanService {

    /** Persistence of loans. */
    private final LoanDao loanDao;

    /** Persistence of members. */
    private final MemberDao memberDao;

    /** Owner of the copy count of the books. */
    private final BookService bookService;

    /** Source of the current date. */
    private final Clock clock;

    /**
     * Creates the service.
     *
     * @param loanDao the loan DAO.
     * @param memberDao the member DAO.
     * @param bookService the book service.
     * @param clock the clock that gives "today".
     */
    public LoanService(LoanDao loanDao, MemberDao memberDao, BookService bookService, Clock clock) {
        this.loanDao = loanDao;
        this.memberDao = memberDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Lends a book to a member. The copy count and the loan change in one transaction.
     *
     * @param memberId the member identifier.
     * @param bookId the book identifier.
     * @return the new loan.
     * @throws SQLException if the transaction fails for a database reason.
     * @throws NotFoundException if the member or the book does not exist.
     * @throws IllegalStateException if the book has no available copies.
     */
    public Loan checkout(int memberId, int bookId) throws SQLException {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (bookService.findById(bookId) == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }

        LocalDate today = LocalDate.now(clock);
        return loanDao.transaction(() -> {
            bookService.borrow(bookId);
            Loan loan = new Loan(member, bookService.findById(bookId), today, LoanPolicy.dueDate(today));
            loanDao.create(loan);
            return loan;
        });
    }

    /**
     * Lists all loans. Open overdue loans carry their accrued fee.
     *
     * @return every loan.
     */
    public List<Loan> findAll() {
        return withAccruedFees(loanDao.findAll());
    }

    /**
     * Returns a loan and charges the fee when it is late.
     * The loan update and the copy count change in one transaction.
     *
     * @param loanId the loan identifier.
     * @return the returned loan.
     * @throws SQLException if the transaction fails for a database reason.
     * @throws NotFoundException if the loan does not exist.
     * @throws IllegalStateException if the loan was already returned.
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);
        if (loan == null) {
            throw new NotFoundException("Loan not found: " + loanId);
        }
        if (loan.isReturned()) {
            throw new IllegalStateException("Loan already returned: " + loanId);
        }

        LocalDate today = LocalDate.now(clock);
        loan.setReturned(true);
        loan.setReturnDate(today);
        if (today.isAfter(loan.getDueDate())) {
            loan.setOverdueFee(accruedFee(loan, today));
        }

        return loanDao.transaction(() -> {
            loanDao.update(loan);
            bookService.returnCopy(loan.getBook().getId());
            return loan;
        });
    }

    /**
     * Lists the open loans that are past their due date, with their accrued fee.
     *
     * @return the overdue loans.
     */
    public List<Loan> overdueLoans() {
        LocalDate today = LocalDate.now(clock);
        return withAccruedFees(loanDao.findAll()).stream()
                .filter(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today))
                .toList();
    }

    /**
     * Sets the accrued fee on every open overdue loan. The fee is not stored.
     *
     * @param loans the loans to complete.
     * @return the same loans.
     */
    private List<Loan> withAccruedFees(List<Loan> loans) {
        LocalDate today = LocalDate.now(clock);
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getDueDate().isBefore(today)) {
                loan.setOverdueFee(accruedFee(loan, today));
            }
        }
        return loans;
    }

    /**
     * Computes the fee for the days between the due date and today.
     *
     * @param loan the loan.
     * @param today the current day.
     * @return the fee according to {@link LoanPolicy#FEE_PER_DAY}.
     */
    private double accruedFee(Loan loan, LocalDate today) {
        return ChronoUnit.DAYS.between(loan.getDueDate(), today) * LoanPolicy.FEE_PER_DAY;
    }
}