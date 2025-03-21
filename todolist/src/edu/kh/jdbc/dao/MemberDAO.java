package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.Member;

public class MemberDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public int signIn(Connection conn, String id, String pw, String name) throws Exception{
		
		int result=0;
		try {
		JDBCTemplate.getConnection();
		
		String sql = """
				INSERT INTO TB_MEMBER
				VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
				""";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		pstmt.setString(3, name);
		
		result = pstmt.executeUpdate();
		
		
		}finally {
			JDBCTemplate.close(conn);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public Member logIn(Connection conn, Member member) {
		
		JDBCTemplate.getConnection();
		
		String sql = """
				SELECT MEMBER_ID, MEMBER_PW
				FROM TB_MEMBER
				WHERE MEMBER_ID = ?
				AND MEMBER_PW = ?
				""";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, member.getUserId());

		return null;
	}

	


	

	

}
