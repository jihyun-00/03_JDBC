package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)Service : 비지니스 로직을 처리하는 계층,
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행
public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();
	
	// 메서드
	/** 전달받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input (입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 담긴 User 객체,
	 * 		   없으면 null 반환
	 */
	public User selectId(String input) {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공(할게 없으면 넘어감)
		
		// 3. DAO 메서드 호출 결과 반환
		User user = dao.selectId(conn, input);
		
		// 4. DML(commit/rollback)
		
		// 5. 다쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과를 view 리턴
		return user;
		
		
		
	}
	
	public int insertUser(User user) {
		
		int result =0;
		
		Connection conn = JDBCTemplate.getConnection();
		
		result = dao.insertUser(conn, user);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
				
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	
	public List<User> selectAll() {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> userList = dao.selectAll(conn);
		JDBCTemplate.close(conn);
		
		return userList;
		
	}

	public List<User> selectName(String search) {
		
		Connection conn = null;
		
		List<User> userList = dao.selectName(conn, search);
		
		JDBCTemplate.close(conn);
		
		return userList;
	}

	public User selectUser(int userNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, userNo);
		
		JDBCTemplate.close(conn);
		
		
		return user;
	}

	public int deleteUser(int userNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = 0;
		
		result = dao.deleteUser(conn, userNo);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
		
		
		
	}

	public int updateName(String id, String pw, String name) {
		
		int result = 0;
		
		Connection conn = JDBCTemplate.getConnection();
		
		
		result = dao.updateName(conn, id, pw, name);
		
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public int insertUser2(String id, String pw, String name) {
		
		int result = 0;
		
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 가공 데이터
		
		result = dao.insertUser2(conn, id, pw, name);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public int multiInsertUser(User user) {
		
		int result = 0;
		
		Connection conn = JDBCTemplate.getConnection();
		
		result = dao.multiInsertUser(conn, user);
		
		JDBCTemplate.close(conn);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

}
