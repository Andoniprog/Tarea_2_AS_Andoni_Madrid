package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Member;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides ORMLite persistence operations for members.
 */
public final class MemberDao {

    /** ORMLite DAO backing member operations. */
    private final Dao<Member, Integer> dao;

    /**
     * Creates a member DAO using the supplied connection source.
     *
     * @param connectionSource database connections used by ORMLite.
     * @throws SQLException if ORMLite cannot create the DAO.
     */
    public MemberDao(ConnectionSource connectionSource) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Member.class);
    }

    /**
     * Retrieves all members.
     *
     * @return all persisted members.
     * @throws SQLException if the query fails.
     */
    public List<Member> findAll() throws SQLException {
        return dao.queryForAll();
    }

    /**
     * Finds a member by its identifier.
     *
     * @param id member identifier.
     * @return the matching member, or {@code null} if none exists.
     * @throws SQLException if the query fails.
     */
    public Member findById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    /**
     * Persists a new member.
     *
     * @param member member to create.
     * @throws SQLException if the insert fails.
     */
    public void create(Member member) throws SQLException {
        dao.create(member);
    }

    /**
     * Persists updates to a member.
     *
     * @param member member to update.
     * @throws SQLException if the update fails.
     */
    public void update(Member member) throws SQLException {
        dao.update(member);
    }
}
