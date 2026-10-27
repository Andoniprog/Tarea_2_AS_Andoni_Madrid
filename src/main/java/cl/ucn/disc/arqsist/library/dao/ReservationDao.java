package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public final class ReservationDao {

    private final Dao<Reservation, Integer> dao;
    private final ConnectionSource connectionSource;

    public ReservationDao(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.dao = DaoManager.createDao(connectionSource, Reservation.class);
    }

    public <T> T transaction(Callable<T> task) throws SQLException {
        return TransactionManager.callInTransaction(connectionSource, task);
    }

    public List<Reservation> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public Reservation findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    public void create(Reservation reservation) throws SQLException {
        dao.create(reservation);
    }

    public void update(Reservation reservation) throws SQLException {
        dao.update(reservation);
    }
}
