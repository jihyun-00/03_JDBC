package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {

	public static void main(String[] args) {
		
		// 입력 받은 최소 급여 이상
		// 입력 받은 최대 급여 이하를 받는
		// 사원의 사번, 이름, 급여를 급여 내림차순으로 조회
		// -> 이클립스 콘솔 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// ...
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String type = "jdbc:oracle:thin:@"; //드라이버 종류
			
			String host = "localhost"; // DB 서버 컴퓨터의 IP 또는 도메인주소
										// localhost == 현재 컴퓨터
			String port = ":1521"; // 프로그램 연결을 위한 port 번호
			
			String dbName = ":XE"; // DBMS 이름(XE == eXpress Edition)
			
			// jdbc:oracle:thin:@localhost:1521:XE
			
			String userName = "kh"; // 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection(type + host + port+ dbName, userName, password);
			
			System.out.println(conn);
			
			sc= new Scanner(System.in);
			System.out.print("최소 급여 : ");
			int min = sc.nextInt();
			
			System.out.print("최대 급여 : ");
			int max = sc.nextInt();
			
			
			
			/*String sql = "SELECT EMP_ID, EMP_NAME, SALARY "
					+ "FROM EMPLOYEE "
					+ "WHERE SALARY BETWEEN" + min + " AND " + max
					+ " ORDER BY 3 DESC";  */
			
			// Java 13부터 지원하는 Text Block (""") 문법
			// 자동으로 개행 포함 + 문자열 연결이 처리됨
			// 기존처럼 + 연산자로 문자열을 연결할 필요가 없음
			String sql = """
					SELECT EMP_ID, EMP_NAME, SALARY
					FROM EMPLOYEE
					WHERE SALARY BETWEEN
					""" + min + " AND " + max + " ORDER BY 3 DESC";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_Name");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("%s / %s / %d원\n", empId, empName, salary);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!= null) rs.close();
				if(stmt!= null) stmt.close();
				if(conn!= null) conn.close();
				
				sc.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		

	}

}
