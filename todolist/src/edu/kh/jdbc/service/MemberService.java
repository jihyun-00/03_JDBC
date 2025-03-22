package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.MemberDAO;
import edu.kh.jdbc.dto.Member;
import edu.kh.jdbc.dto.Todo;

public class MemberService {
	
	MemberDAO dao = new MemberDAO();

	public int signIn(String id, String pw, String name)throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.signIn(conn, id, pw, name);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public List<Member> logIn(String id, String pw) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<Member> memberList = new ArrayList<Member>();
		
		memberList = dao.logIn(conn, id, pw);
		
		return memberList;
	}

	public List<Todo> viewTodoList(List<Member> loginList) throws Exception {

		Connection conn = JDBCTemplate.getConnection();
		
		List<Todo> todoList = dao.viewTodoList(conn, loginList);
		
		return todoList;
	}

	public int addTodoList(String title, String content, List<Member> loginList) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.addTodoList(conn, title, content, loginList);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		
		return result;
	}

	public int updateTodoList(int num, String title, String content, List<Member> loginList) throws Exception {

		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateTodoList(conn, num, title, content, loginList);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public int changeYN(int num, char yn, List<Member> loginList) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.changeYN(conn, num, yn, loginList);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public int deleteTodoList(int num, List<Member> loginList) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteTodoList(conn, num, loginList);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}








	

	

	

}
