package edu.kh.jdbc.service;

import java.sql.Connection;

import edu.kh.jdbc.dao.MemberDAO;

public class MemberService {
	
	MemberDAO dao = new MemberDAO();

	public int signIn(String id, String pw, String name)throws Exception {
		
		Connection conn = null;
		
		int result = dao.signIn(conn, id, pw, name);
		
		return result;
	}

}
