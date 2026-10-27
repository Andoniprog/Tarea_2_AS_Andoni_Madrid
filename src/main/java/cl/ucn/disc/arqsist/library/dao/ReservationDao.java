package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Provides ORMLite persistence and transaction operations for reservations.
 */
public final class ReservationDao {

    /** ORMLite DAO backing reservation operations. */
    private final Dao<Reservation, Integer> dao;

    /** Connection source used to run reservation transactions. */
    private final ConnectionSource connectionSource;

    /**
     * Creates a reservation DAO using the supplied connection source.
     *
     * @param connectionSource database connections used by ORMLite.
     * @throws SQLException if ORMLite cannot create the DAO.
     */
    public ReservationDao(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.dao = DaoManager.createDao(connectionSource, Reservation.class);
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
     * Retrieves all reservations.
     *
     * @return all persisted reservations.
     * @throws SQLException if the query fails.
     */
    public List<Reservation> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a reservation by its identifier.
     *
     * @param id reservation identifier.
     * @return the matching reservation, or {@code null} if none exists.
     * @throws SQLException if the query fails.
     */
    public Reservation findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Persists a new reservation.
     *
     * @param reservation reservation to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Reservation reservation) throws SQLException {
        dao.create(reservation);
    }

    /**
     * Persists updates to a reservation.
     *
     * @param reservation reservation to update.
     * @throws SQLException if the update fails.
     */
    public void update(Reservation reservation) throws SQLException {
        dao.update(reservation);
    }
}
