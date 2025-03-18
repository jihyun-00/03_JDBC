package study;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCEx1 {

	public static void main(String[] args) {

		
		// 1. 입력 최소 하나
		// 2. PreparedStatement
		// 3. AutoCommit 끄고 Rollback 하기
		
		// 문제1. 사원의 이름을 입력받고
		// 그 사원이 속해있는 부서원들 조회
		// 본인 제외
		// 사번, 사원명, 전화번호, 고용일, 부서명
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Scanner sc = new Scanner(System.in);
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(url, user, pw);
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, PHONE, HIRE_DATE, DEPT_TITLE
					FROM EMPLOYEE E
					LEFT JOIN DEPARTMENT D ON(D.DEPT_ID=E.DEPT_CODE)
					WHERE DEPT_CODE=(SELECT DEPT_CODE
									FROM EMPLOYEE
									WHERE EMP_NAME=?)
					AND EMP_NAME!= ?
					""";
			
			ps = conn.prepareStatement(sql);
			
			System.out.print("이름 입력 : ");
			String name = sc.next();
			
			ps.setString(1, name);
			ps.setString(2, name);
			
			rs = ps.executeQuery();
			
			if(!rs.isBeforeFirst()) { //비어있다면
				
				System.out.println("실패!");
				return;
			}
			
			while(rs.next()) {
				System.out.printf("%s, %s, %s, %s, %s\n", rs.getString(1),  rs.getString(2),  rs.getString(3),  rs.getString(4),  rs.getString(5));
				
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(conn!=null) conn.close();
				
				
				if(sc!=null) sc.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
