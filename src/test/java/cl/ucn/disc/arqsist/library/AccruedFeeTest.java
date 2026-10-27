package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.BookService;
import cl.ucn.disc.arqsist.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Verifies that overdue fees are accrued for display without being persisted.
 */
class AccruedFeeTest {

    /** DAO used to create and inspect test loans. */
    private LoanDao loanDao;

    /** Service whose fee-calculation behavior is under test. */
    private LoanService loanService;

    /** Book associated with the test loans. */
    private Book book;

    /** Member associated with the test loans. */
    private Member member;

    /**
     * Creates an in-memory database, service, and base test records.
     *
     * @throws Exception if database setup or persistence fails.
     */
    @BeforeEach
    void setUp() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");
        BookDao bookDao = new BookDao(db.connectionSource());
        MemberDao memberDao = new MemberDao(db.connectionSource());
        loanDao = new LoanDao(db.connectionSource());

        Clock clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
        loanService = new LoanService(loanDao, memberDao, new BookService(bookDao), clock);

        book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 3);
        bookDao.create(book);
        member = new Member("Ada Lovelace", "ada@example.com");
        memberDao.create(member);
    }

    /**
     * Checks that only overdue open loans accrue a fee and that the fee is not stored.
     *
     * @throws Exception if persisting or loading test loans fails.
     */
    @Test
    void openOverdueLoanShowsAccruedFee() throws Exception {
        Loan overdue = new Loan(member, book, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 22));
        Loan active = new Loan(member, book, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 31));
        loanDao.create(overdue);
        loanDao.create(active);

        List<Loan> loans = loanService.findAll();

        assertEquals(24.0, feeOf(loans, overdue.getId()));
        assertEquals(0.0, feeOf(loans, active.getId()));
        assertEquals(0.0, loanDao.findById(overdue.getId()).getOverdueFee(), "the accrued fee must not be stored");
    }

    /**
     * Finds the accrued fee for a loan in a result list.
     *
     * @param loans loans returned by the service.
     * @param id identifier of the loan to find.
     * @return the matching loan's overdue fee.
     */
    private static double feeOf(List<Loan> loans, int id) {
        return loans.stream().filter(loan -> loan.getId() == id).findFirst().orElseThrow().getOverdueFee();
    }
}