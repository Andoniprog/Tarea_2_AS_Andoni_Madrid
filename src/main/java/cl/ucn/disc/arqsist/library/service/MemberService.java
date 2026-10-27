package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles member registration and retrieval.
 */
public final class MemberService {

    /** Persistence component used for member data. */
    private final MemberDao memberDao;

    /**
     * Creates the service with its member persistence component.
     *
     * @param memberDao DAO used to load and persist members.
     */
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    /**
     * Registers and returns a member.
     *
     * @param member member to register.
     * @return the registered member.
     * @throws SQLException if persistence fails.
     */
    public Member register(Member member) throws SQLException {
        memberDao.create(member);
        return member;
    }

    /**
     * Retrieves all registered members.
     *
     * @return all persisted members.
     * @throws SQLException if the query fails.
     */
    public List<Member> findAll() throws SQLException {
        return memberDao.findAll();
    }
}