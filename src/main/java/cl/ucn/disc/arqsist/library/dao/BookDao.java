package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Book;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides ORMLite persistence operations for books.
 */
public final class BookDao {

    /** ORMLite DAO backing book operations. */
    private final Dao<Book, Integer> dao;

    /**
     * Creates a book DAO using the supplied connection source.
     *
     * @param connectionSource database connections used by ORMLite.
     * @throws SQLException if ORMLite cannot create the DAO.
     */
    public BookDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Book.class);
    }

    /**
     * Retrieves all books.
     *
     * @return all persisted books.
     * @throws SQLException if the query fails.
     */
    public List<Book> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a book by its identifier.
     *
     * @param id book identifier.
     * @return the matching book, or {@code null} if none exists.
     * @throws SQLException if the query fails.
     */
    public Book findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Persists a new book.
     *
     * @param book book to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Book book) throws SQLException {
        dao.create(book);
    }

    /**
     * Persists updates to a book.
     *
     * @param book book to update.
     * @throws SQLException if the update fails.
     */
    public void update(Book book) throws SQLException {
        dao.update(book);
    }

    /**
     * Deletes a book.
     *
     * @param book book to delete.
     * @throws SQLException if the deletion fails.
     */
    public void delete(Book book) throws SQLException {
        dao.delete(book);
    }
}
