package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {

		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의
		// 이름을 수정(UPDATE)
		
		// 1. PreparedStatement 이용하기
		// 2. commit / rollback 처리하기
		// 3. 성공 시 "수정 성공!" 출력 / 실패 시 "아이디 또는 비밀번호 불일치" 출력
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Scanner sc = new Scanner(System.in);
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh"; // 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection(url, userName, password);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			conn.setAutoCommit(false);
			
			int result = pstmt.executeUpdate();
			
			if(result>0) { //INSERT 성공 시
				System.out.println("수정 성공!");
				conn.commit(); // COMMIT 수행 -> DB에 INSERT 영구 반영
				
			} else { //실패
				System.out.println("아이디 또는 비밀번호 불일치");
				conn.rollback(); // 실패 시 ROLLBACK
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(conn!=null) conn.close();
				if(pstmt!=null) pstmt.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		
	}

}
