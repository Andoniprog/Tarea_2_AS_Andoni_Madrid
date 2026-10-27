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
 * Implements checkout, return, overdue, and late-fee operations for loans.
 */
public final class LoanService {

    /** Persistence component used for loan data. */
    private final LoanDao loanDao;

    /** Persistence component used to verify members. */
    private final MemberDao memberDao;

    /** Service that manages book-copy availability. */
    private final BookService bookService;

    /** Clock used to determine current loan dates. */
    private final Clock clock;

    /**
     * Creates the service with its persistence, book, and time dependencies.
     *
     * @param loanDao DAO used to persist loans.
     * @param memberDao DAO used to verify borrowers.
     * @param bookService service used to manage book availability.
     * @param clock clock used to determine the current date.
     */
    public LoanService(LoanDao loanDao, MemberDao memberDao, BookService bookService, Clock clock) {
        this.loanDao = loanDao;
        this.memberDao = memberDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Checks out an available book to an existing member.
     *
     * @param memberId identifier of the borrowing member.
     * @param bookId identifier of the borrowed book.
     * @return the created loan.
     * @throws SQLException if a persistence operation fails.
     * @throws NotFoundException if the member or book does not exist.
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
     * Retrieves all loans, calculating current fees for open overdue loans.
     *
     * @return all loans with any applicable accrued fees.
     */
    public List<Loan> findAll() {
        return withAccruedFees(loadAllLoans());
    }

    /**
     * Returns an open loan and makes its book copy available again.
     *
     * @param loanId identifier of the loan to return.
     * @return the updated loan.
     * @throws SQLException if a persistence operation fails.
     * @throws NotFoundException if the loan does not exist.
     * @throws IllegalStateException if the loan has already been returned.
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
     * Retrieves open loans whose due dates have passed.
     *
     * @return overdue, unreturned loans with accrued fees.
     */
    public List<Loan> overdueLoans() {
        LocalDate today = LocalDate.now(clock);
        return withAccruedFees(loadAllLoans()).stream()
                .filter(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today))
                .toList();
    }

    /**
     * Loads all loans and converts database errors to an unchecked service failure.
     *
     * @return all persisted loans.
     * @throws IllegalStateException if loans cannot be loaded.
     */
    private List<Loan> loadAllLoans() {
        try {
            return loanDao.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load loans", e);
        }
    }

    /**
     * Calculates accrued fees for each open overdue loan without persisting them.
     *
     * @param loans loans whose accrued fees may need updating in memory.
     * @return the supplied list with applicable accrued fees set.
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
     * Calculates the fee for a loan through the supplied date.
     *
     * @param loan loan whose due date determines the overdue period.
     * @param today date through which to accrue the fee.
     * @return the accrued fee.
     */
    private double accruedFee(Loan loan, LocalDate today) {
        return ChronoUnit.DAYS.between(loan.getDueDate(), today) * LoanPolicy.FEE_PER_DAY;
    }
}