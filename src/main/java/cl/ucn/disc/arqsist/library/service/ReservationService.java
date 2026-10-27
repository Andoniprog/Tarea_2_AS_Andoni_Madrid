/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles reservation creation and fulfillment as book loans.
 */
public final class ReservationService {

    /** Persistence of reservations. */
    private final ReservationDao reservationDao;

    /** Persistence of members. */
    private final MemberDao memberDao;

    /** Persistence of loans. */
    private final LoanDao loanDao;

    /** Owner of the copy count of the books. */
    private final BookService bookService;

    /** Source of the current date. */
    private final Clock clock;

    /**
     * Creates the service with its persistence, book, and time dependencies.
     *
     * @param reservationDao DAO used to persist reservations.
     * @param memberDao DAO used to verify members.
     * @param loanDao DAO used to create loans when fulfilling reservations.
     * @param bookService service used to manage book availability.
     * @param clock clock used to determine reservation and loan dates.
     */
    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, LoanDao loanDao,
                              BookService bookService, Clock clock) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Creates a reservation for an existing member and book.
     *
     * @param bookId identifier of the reserved book.
     * @param memberId identifier of the reserving member.
     * @return the created reservation.
     * @throws SQLException if a persistence operation fails.
     * @throws NotFoundException if the book or member does not exist.
     */
    public Reservation reserve(int bookId, int memberId) throws SQLException {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        Reservation reservation = new Reservation(member, book, LocalDate.now(clock));
        reservationDao.create(reservation);
        return reservation;
    }

    /**
     * Retrieves all reservations.
     *
     * @return all persisted reservations.
     * @throws SQLException if the query fails.
     */
    public List<Reservation> findAll() throws SQLException {
        return reservationDao.findAll();
    }

    /**
     * Fulfills an open reservation by creating a loan and checking out its book.
     *
     * @param reservationId identifier of the reservation to fulfill.
     * @return the loan created from the reservation.
     * @throws SQLException if a persistence operation fails.
     * @throws NotFoundException if the reservation does not exist.
     * @throws IllegalStateException if the reservation is already fulfilled.
     */
    public Loan fulfill(int reservationId) throws SQLException {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Reservation not found: " + reservationId);
        }
        if (reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation already fulfilled: " + reservationId);
        }

        LocalDate today = LocalDate.now(clock);
        return reservationDao.transaction(() -> {
            reservation.setFulfilled(true);
            reservationDao.update(reservation);

            int bookId = reservation.getBook().getId();
            bookService.borrow(bookId);

            Loan loan = new Loan(reservation.getMember(), bookService.findById(bookId), today, LoanPolicy.dueDate(today));
            loanDao.create(loan);
            return loan;
        });
    }
}