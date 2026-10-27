package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public final class LoanDao {

    private final Dao<Loan, Integer> dao;
    private final ConnectionSource connectionSource;

    public LoanDao(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        this.dao = DaoManager.createDao(connectionSource, Loan.class);
    }

    public <T> T transaction(Callable<T> task) throws SQLException {
        return TransactionManager.callInTransaction(connectionSource, task);
    }

    public List<Loan> findAll() throws SQLException {
        return dao.queryForAll();
    }

    public Loan findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    public void create(Loan loan) throws SQLException {
        dao.create(loan);
    }

    public void update(Loan loan) throws SQLException {
        dao.update(loan);
    }
}
