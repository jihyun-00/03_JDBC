package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.dao.MemberDAO;
import edu.kh.jdbc.dto.Member;

public class MemberService {
	
	MemberDAO dao = new MemberDAO();

	public int signIn(String id, String pw, String name)throws Exception {
		
		Connection conn = null;
		
		int result = dao.signIn(conn, id, pw, name);
		
		return result;
	}

	public Member logIn(Member member) {
		
		Connection conn = null;
		
		member = dao.logIn(conn,member);
		
		return member;
		
	}

	

	

	

}
