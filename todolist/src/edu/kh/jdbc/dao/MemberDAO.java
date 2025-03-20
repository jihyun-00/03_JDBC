package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.kh.jdbc.common.JDBCTemplate;

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

	

}
