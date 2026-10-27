package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * Applies catalog and copy-count operations to books.
 */
public final class BookService {

    /** Persistence component used for book data. */
    private final BookDao dao;

    /**
     * Creates the service with its book persistence component.
     *
     * @param dao DAO used to load and persist books.
     */
    public BookService(BookDao dao) {
        this.dao = dao;
    }

    /**
     * Lists every book in the catalog.
     *
     * @return all persisted books.
     * @throws SQLException if the catalog cannot be loaded.
     */
    public List<Book> listAll() throws SQLException {
        return dao.findAll();
    }

    /**
     * Looks up a book by identifier.
     *
     * @param id book identifier.
     * @return the book, or {@code null} if it does not exist.
     * @throws SQLException if the lookup fails.
     */
    public Book findById(int id) throws SQLException {
        return dao.findById(id);
    }

    /**
     * Creates a book, initializing its available copies to its total copies.
     *
     * @param book book to add to the catalog.
     * @return the created book.
     * @throws SQLException if persistence fails.
     */
    public Book create(Book book) throws SQLException {
        book.setAvailableCopies(book.getTotalCopies());
        dao.create(book);
        return book;
    }

    /**
     * Decreases the available-copy count for a borrowed book.
     *
     * @param bookId identifier of the borrowed book.
     * @throws SQLException if the book cannot be loaded or updated.
     */
    public void borrow(int bookId) throws SQLException {
        Book book = dao.findById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        dao.update(book);
    }

    /**
     * Increases the available-copy count for a returned book.
     *
     * @param bookId identifier of the returned book.
     * @throws SQLException if the book cannot be loaded or updated.
     */
    public void returnCopy(int bookId) throws SQLException {
        Book book = dao.findById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        dao.update(book);
    }
}
