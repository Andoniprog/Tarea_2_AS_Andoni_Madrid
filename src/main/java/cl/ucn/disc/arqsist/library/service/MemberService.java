package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.util.List;


public final class MemberService {

    
    private final MemberDao memberDao;

    
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    
    public Member register(Member member) throws SQLException {
        memberDao.create(member);
        return member;
    }

    
    public List<Member> findAll() throws SQLException {
        return memberDao.findAll();
    }
}