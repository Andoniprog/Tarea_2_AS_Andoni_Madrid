package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides ORMLite persistence and transaction operations for loans.
 */
public final class LoanDao {

    /** ORMLite DAO backing loan operations. */
    private final Dao<Loan, Integer> dao;

    /** Connection source used to run loan transactions. */
    private final ConnectionSource connectionSource;

    /**
     * Creates a loan DAO using the supplied connection source.
     *
     * @param connectionSource database connections used by ORMLite.
     * @throws SQLException if ORMLite cannot create the DAO.
     */
    public LoanDao(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.dao = DaoManager.createDao(connectionSource, Loan.class);
    }

    /**
     * Runs a task within a database transaction.
     *
     * @param task operation to execute atomically.
     * @param <T> result type returned by the task.
     * @return the task result.
     * @throws SQLException if the task or transaction fails.
     */
    public <T> T transaction(Callable<T> task) throws SQLException {
        return TransactionManager.callInTransaction(connectionSource, task);
    }

    /**
     * Retrieves all loans.
     *
     * @return all persisted loans.
     * @throws SQLException if the query fails.
     */
    public List<Loan> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a loan by its identifier.
     *
     * @param id loan identifier.
     * @return the matching loan, or {@code null} if none exists.
     * @throws SQLException if the query fails.
     */
    public Loan findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Persists a new loan.
     *
     * @param loan loan to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Loan loan) throws SQLException {
        dao.create(loan);
    }

    /**
     * Persists updates to a loan.
     *
     * @param loan loan to update.
     * @throws SQLException if the update fails.
     */
    public void update(Loan loan) throws SQLException {
        dao.update(loan);
    }
}
