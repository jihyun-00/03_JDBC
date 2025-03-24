package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.Member;
import edu.kh.jdbc.dto.Todo;

public class MemberDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public int signIn(Connection conn, String id, String pw, String name) throws Exception{
		
		int result=0;
		try {
		
		String sql = """
				INSERT INTO TB_MEMBER
				VALUES(?, ?, ?, DEFAULT)
				""";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		pstmt.setString(3, name);
		
		result = pstmt.executeUpdate();
		
		
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public List<Member> logIn(Connection conn, String id, String pw) throws Exception {
		
		List<Member> memberList = new ArrayList<Member>();
		
		try {
			String sql = """
					SELECT * FROM V_MEMBER
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				
				memberList.add(new Member(rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5)));
			}
			
			
			
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(stmt);
			JDBCTemplate.close(conn);
			
		}
		
		
		
		return memberList;
	}

	public List<Todo> viewTodoList(Connection conn, List<Member> loginList) throws Exception{
		
		List<Todo> todoList = new ArrayList<Todo>();
		
		try {
			String sql = """
					SELECT 
						    ROW_NUMBER() OVER (ORDER BY WRITE_DATE) AS row_num,
						    MEMBER_ID, 
						    TODO_TITLE, 
						    TODO_CONTENT, 
						    COMPLETE_YN, 
						    WRITE_DATE
					FROM TB_TODO
					WHERE MEMBER_ID=?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, loginList.get(0).getUserId());
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				todoList.add(new Todo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
				
			}
			
			
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return todoList;
	}

	public int addTodoList(Connection conn, String title, String content, List<Member> loginList) throws Exception {
		
		int result = 0;
		
		try {
			
			String sql = """
					INSERT INTO TB_TODO
					VALUES(?, ?, ?, DEFAULT, DEFAULT )
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, loginList.get(0).getUserId());
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			
			result = pstmt.executeUpdate();
	
			
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		return result;
	}





	public int updateTodoList(Connection conn, int num, String title, String content, List<Member> loginList)throws Exception{
		
		int result = 0;
		
		try {

			String sql = """
					UPDATE TB_TODO
					SET TODO_TITLE = ?,
					    TODO_CONTENT = ?
					WHERE MEMBER_ID = ?
					AND ROWID = (
					      SELECT ROWID
					      FROM (
					          SELECT ROWID, ROW_NUMBER() OVER (ORDER BY WRITE_DATE) AS ROW_NUM
					          FROM V_TODO
					          WHERE MEMBER_ID = ?
					      )
					      WHERE ROW_NUM = ?
					  )
										""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setString(3, loginList.get(0).getUserId());
			pstmt.setString(4, loginList.get(0).getUserId());
			pstmt.setInt(5, num);
			
			result = pstmt.executeUpdate();
			
		}finally {
			
			JDBCTemplate.close(pstmt);
			
		}
		
		return result;
	}

	public int changeYN(Connection conn, int num, char yn, List<Member> loginList) throws Exception {
		
		int result = 0;
		
		try {
			
			String sql = """
					UPDATE TB_TODO
					SET COMPLETE_YN = ?
					WHERE MEMBER_ID = ?
					AND ROWID = (
					      SELECT ROWID
					      FROM (
					          SELECT ROWID, ROW_NUMBER() OVER (ORDER BY WRITE_DATE) AS ROW_NUM
					          FROM V_TODO
					          WHERE MEMBER_ID = ?
					      )
					      WHERE ROW_NUM = ?
					  )
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			
			
			pstmt.setObject(1, yn);
			pstmt.setString(2, loginList.get(0).getUserId());
			pstmt.setString(3, loginList.get(0).getUserId());
			pstmt.setInt(4, num);
			
			result = pstmt.executeUpdate();
			
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		
		
		return result;
	}

	public int deleteTodoList(Connection conn, int num, List<Member> loginList) throws Exception {
		
		int result = 0;
		
		try {
			
			String sql = """
					DELETE FROM TB_TODO
					WHERE MEMBER_ID = ?
					AND ROWID = (SELECT ROWID
								FROM (SELECT ROWID, ROW_NUMBER() OVER (ORDER BY WRITE_DATE) AS ROW_NUM
									FROM V_TODO
									WHERE MEMBER_ID = ?)
								WHERE ROW_NUM = ?)
					
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, loginList.get(0).getUserId());
			pstmt.setString(2, loginList.get(0).getUserId());
			pstmt.setInt(3, num);
			
			result = pstmt.executeUpdate();
			
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		
		
		return result;
	}
}
