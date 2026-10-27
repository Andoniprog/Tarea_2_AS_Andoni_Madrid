package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DatabaseSeedTest {


    private Database db;

   
    private BookDao bookDao;

    
    private MemberDao memberDao;

    
    private LoanDao loanDao;

   
    private ReservationDao reservationDao;

    
    @BeforeEach
    void setUp() throws Exception {
        db = new Database("jdbc:sqlite::memory:");
        bookDao = new BookDao(db.connectionSource());
        memberDao = new MemberDao(db.connectionSource());
        loanDao = new LoanDao(db.connectionSource());
        reservationDao = new ReservationDao(db.connectionSource());
    }

    
    @Test
    void seedCreatesConsistentData() throws Exception {
        db.seedIfEmpty();

        List<Loan> loans = loanDao.findAll();
        assertFalse(loans.isEmpty());
        assertFalse(reservationDao.findAll().isEmpty());
        assertEquals(3, memberDao.findAll().size());
        assertEquals(3, loans.size());
        assertEquals(1, reservationDao.findAll().size());

        LocalDate today = LocalDate.now();
        long overdue = loans.stream()
                .filter(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today))
                .count();
        assertEquals(1, overdue, "exactly one loan must be overdue");

        for (Loan loan : loans) {
            if (!loan.isReturned()) {
                Book book = bookDao.findById(loan.getBook().getId());
                assertTrue(book.getAvailableCopies() < book.getTotalCopies(),
                        "a book with an open loan must have fewer available copies");
            }
        }
    }

    
    @Test
    void seedingTwiceDoesNotDuplicateData() throws Exception {
        db.seedIfEmpty();
        int loans = loanDao.findAll().size();
        int reservations = reservationDao.findAll().size();

        db.seedIfEmpty();

        assertEquals(loans, loanDao.findAll().size());
        assertEquals(reservations, reservationDao.findAll().size());
    }
}