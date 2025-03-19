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
	
	/** 1. User 등록 서비스
	 * @param user : 입력받은 id, pw, name이 세팅된 객체
	 * @return 결과 행의 개수
	 * @throws Exception 
	 */
	public int insertUser(User user) throws Exception {
		
		int result =0;
		
		Connection conn = JDBCTemplate.getConnection();
		
		result = dao.insertUser(conn, user);
		
		// DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
				
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	
	/** 2. User 전체 조회 서비스
	 * @return 조회된 User들이 담긴 List
	 */
	public List<User> selectAll() {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> userList = dao.selectAll(conn);
		JDBCTemplate.close(conn);
		
		return userList;
		
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param search : 입력한 키워드
	 * @return userList : 조회된 회원 리스트
	 */
	public List<User> selectName(String search) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> userList = dao.selectName(conn, search);
		
		JDBCTemplate.close(conn);
		
		return userList;
	}

	/** 4. USER_NO를 입력 받아 일치하는 User 조회 서비스
	 * @param userNo
	 * @return user (조회된 회원 정보 또는 null)
	 */
	public User selectUser(int userNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, userNo);
		
		JDBCTemplate.close(conn);
		
		
		return user;
	}

	/** USER_NO를 입력바아 일치하는 User 삭제 서비스
	 * @param userNo
	 * @return result
	 */
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

	/** ID, PW가 일치하는 회원이 있을 경우 이름 수정
	 * @param id
	 * @param pw
	 * @param name
	 * @return
	 */
	public int updateName(String name, int userNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		
		int result = dao.updateName(conn, name, userNo);
		
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	/** ID, PW가 일치하는 회원의 USER_NO 조회 서비스
	 * @param id
	 * @param pw
	 * @return
	 */
	public int selectUserNo(String id, String pw) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int nserNo = dao.selectUserNo(conn, id, pw);
		
		JDBCTemplate.close(conn);
		
		return nserNo;
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

	/** 아이디 중복 확인 서비스
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int idCheck(String userId) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn, userId);
		
		JDBCTemplate.close(conn);
		
		
		return count;
	}

	public int multiInsertUser(List<User> userList) throws Exception {
		
		// 다중 INSERT 방법
		// 1) SQL을 이용한 다중 INSERT
		// 2) Java 반복문을 이용한 다중 INSERT (이거 사용!)
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = 0; // 삽입 성공한 행의 개수
		
		// 1행씩 삽입
		for(User u : userList) {
			int result = dao.insertUser(conn, u);
			count += result;
		}
		
		// 트랜잭션 제어 처리
		// 전체 삽입 성공 시 commit / 아니면 rollback(일부 삽입, 전체 실패)
		if(count == userList.size()) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		
		
		return count;
	}

	
}
