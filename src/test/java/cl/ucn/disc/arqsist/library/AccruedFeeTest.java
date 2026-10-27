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


class AccruedFeeTest {

    
    private LoanDao loanDao;

   
    private LoanService loanService;

    
    private Book book;

   
    private Member member;

    
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

    
    @Test
    void openOverdueLoanShowsAccruedFee() {
        Loan overdue = new Loan(member, book, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 22));
        Loan active = new Loan(member, book, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 31));
        loanDao.create(overdue);
        loanDao.create(active);

        List<Loan> loans = loanService.findAll();

        assertEquals(24.0, feeOf(loans, overdue.getId()));
        assertEquals(0.0, feeOf(loans, active.getId()));
        assertEquals(0.0, loanDao.findById(overdue.getId()).getOverdueFee(), "the accrued fee must not be stored");
    }

    private static double feeOf(List<Loan> loans, int id) {
        return loans.stream().filter(loan -> loan.getId() == id).findFirst().orElseThrow().getOverdueFee();
    }
}