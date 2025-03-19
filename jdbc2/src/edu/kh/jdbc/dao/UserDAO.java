package edu.kh.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명()이 아닌 메서드명()만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.*;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)DAO (Data Access Object) -> 데이터 접근 객체
// 데이터가 저장된 곳에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환 받는 역할
public class UserDAO {
	
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 메서드
	/** 전달받은 Connection을 이용해서 DB에 접근하여
	 *  전달받은 아이디(input)와 일치하는 Uesr 정보를 DB 조회하기
	 * @param conn : Service에서 생성한 Connection
	 * @param input : View에서 입력받은 아이디
	 * @return 아이디가 일치하는 회원의 User 또는 null
	 */
	public User selectId(Connection conn, String input) {
		
		// 1. 결과 저장용 변수 선언
		User user = null;
		
		try {
			
			// 2. SQL 작성
			String sql = "SELECT * FROM TB_USER WHERE USER_ID = ?";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 에 알맞은 값 세팅
			pstmt.setString(1, input);
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회결과가 있을 경우
			// + 중복되는 아이디가 없다고 가정
			// -> 1행만 조회되기 때문에 while문보다는 if를 사용하는 게 효과적
			if(rs.next()) {
				// 첫 행의 데이터가 존재한다면
				
				// 각 컬럼의 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				
				Date enrollDate = rs.getDate("ENROLL_DATE");
				
				// 조회된 컬럼값을 이용해서 User 객체 생성
				user = new User(userNo, userId, userPw, userName,
						enrollDate.toString() );
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			// 사용한 JDBC 객체 자원 반환(close)
			// JDBCTemplate.close(rs);
			// JDBCTemplate.close(pstmt);
			
			close(rs);
			close(pstmt);
			
			// Connection 객체는 생성된 Service에서 close!
			
		}

		return user; // 결과 반환 (생성된 User 객체 또는 null)
	}

	/** 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return : INSERT 결과 행의 개수
	 */
	public int insertUser(Connection conn, User user) throws Exception{
		
		// SQL 수행 중 발생하는 예외를
		// catch로 처리하지 않고, throws를 이용해서 호출부로 던져 처리
		// -> catch 문 필요없다!
		
		/*
		 * INSERT INTO TB_USER 
		 * VALUES(SEQ_USER_NO.NEXTVAL, 'user01', 'pass01', '유저일', DEFAULT );
		 */
		
		//1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER 
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
					""";

			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 에 알맞은 값 세팅
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL 수행 후 결과 반환 받기
			result = pstmt.executeUpdate();
			
			
			
		} /*catch(Exception e) {
			e.printStackTrace();
			
		} */finally {
			close(pstmt);
			
		}
		return result;

	}

	public List<User> selectAll(Connection conn) {
		
		List<User> userList = new ArrayList<User>();
		
		try {
			
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME, 
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					ORDER BY 1
					""";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			boolean flag = true;
			
			while(rs.next()) {
				flag = false;
				
				userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여 문자열로 변환해 조회했기 때문에
			}
			
			if(flag) {
				System.out.println("조회값 없음");
			}
			
			//return userList;
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
			
		}
			
			return userList;
			
			
		}

	public List<User> selectName(Connection conn, String search) {
		
		List<User> userList = new ArrayList<User>();
		
		try {
			
			String sql = """
					SELECT * FROM TB_USER WHERE USER_NAME LIKE """ + "'%" + search + "%'";
			 		// SELECT * FROM TB_USER WHERE USER_NAME LIKE '%' || ? || '%'; ->db에서는 이렇게 작성
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			boolean flag = true;
			
			while(rs.next()) {
				flag = false;
				userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
			
			if(flag) {
				System.out.println("일치하는 값이 없습니다.");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			
		}
		return userList;
		
	}

	/** 4. USER_NO를 입력 받아 일치하는 User 조회 dao
	 * @param conn
	 * @param userNo
	 * @return user 객체 or null
	 */
	public User selectUser(Connection conn, int userNo) {
		
		User user = null;
		
		try {
			String sql = """
				SELECT * FROM TB_USER WHERE USER_NO = 
				""" + userNo;
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
			user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		

		return user;
	}

	/** USER_NO를 입력바아 일치하는 User 삭제 dao
	 * @param conn
	 * @param userNo
	 * @return
	 */
	public int deleteUser(Connection conn, int userNo) {
		
		int result = 0;
		
		try {
			String sql = """
				DELETE FROM TB_USER
				WHERE USER_NO = 
				""" + userNo;
			
			pstmt = conn.prepareStatement(sql);
			
			result = pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		
		
		return result;
	}

	/**일치하는 회원의 이름 수정 DAO
	 * @param conn
	 * @param id
	 * @param pw
	 * @param name
	 * @return result
	 */
	public int updateName(Connection conn, String name, int userNo) {
		
		int result = 0;
		
		try {
			
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_NO = ?
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setInt(2, userNo);
			
			result = pstmt.executeUpdate();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int insertUser2(Connection conn, String id, String pw, String name) {
		
		int result = 0;
		
		try {
			
			
			String testsql = """
					SELECT USER_ID
					FROM TB_USER
					WHERE USER_ID = """ + "'" + id + "'";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(testsql);
			
			if (!rs.next()) {

				String sql = """
						INSERT INTO TB_USER
						VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
						""";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				pstmt.setString(3, name);

				result = pstmt.executeUpdate();

			}
			
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * @param conn
	 * @param id
	 * @param pw
	 * @return
	 */
	public int selectUserNo(Connection conn, String id, String pw) {
		
		int userNo = 0;
		
		try {
			String sql = """
					SELECT USER_NO
					FROM TB_USER
					WHERE USER_ID =?
					AND USER_PW = ? 
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			rs = pstmt.executeQuery();
			
			// 조회된 1행이 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return userNo;
	}

	/** 아이디 중복 확인 dao
	 * @param conn
	 * @param userId
	 * @return
	 */
	public int idCheck(Connection conn, String userId) throws Exception {
		
		int count = 0;
		
		try {
			
			String sql = """
					SELECT COUNT(*)
					FROM TB_USER
					WHERE USER_ID = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		
		
		return count;
	}

	
		
}
