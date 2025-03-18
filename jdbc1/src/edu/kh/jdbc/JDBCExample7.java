package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {

		// EMPLOYEE	테이블에서
		// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
				
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
				
		// [실행화면]
		// 조회할 성별(M/F) : F -> 소문자로 작성해도 인식되게 작성
		// 급여 범위(최소, 최대 순서로 작성) : ->입력 두번 받게 설정
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부

		
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Scanner sc = new Scanner(System.in);
		
		try {
			
			//Class.forName("oracle.jdbc.dirver.OracleDriver");
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			
			String user = "kh";
			String pass = "kh1234";
			
			conn = DriverManager.getConnection(url, user, pass);
			
			System.out.print("조회할 성별(M/F) : ");
			String gender = sc.next();
			
			System.out.println("급여 범위(최소, 최대 순서로 작성) : ");
			int min = sc.nextInt();
			int max = sc.nextInt();
			
			System.out.print("급여 정렬(1.ASC, 2.DESC) : ");
			int order = sc.nextInt();
			
			String sql = """
					SELECT EMP_ID 사번, EMP_NAME 이름,
					 DESCODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') GENDER,
					 SALARY 급여, JOB_NAME 직급명, NVL(DEPT_TITLE, '없음') 부서명
					FROM EMPLOYEE
					JOIN JOB USING(JOB_CODE)
					LEFT JOIN DEPARTMENT ON(DEPT_ID=DEPT_CODE)
					WHERE DESCODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') = ?
					AND SALARY BETWEEN ? AND ?
					AND ORDER BY SALARY 
					""";

			/*
			 * SELECT DESCODE(ENP_NO, 8, 1) 성별
			 * FROM EMPLOYEE
			 * WHERE 성별 = ?
			 *  */
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, gender);
			pstmt.setInt(2, min);
			pstmt.setInt(3, max);
			
			if(order==1) {
				sql += "ASC";
				
			} else if(order==2) {
				sql += "DESC";
				
			}else {
				System.out.println("잘못된 값을 입력하였습니다.");
			}
			
			//conn.setAutoCommit(false);
			
			rs = pstmt.executeQuery(sql);
			
			System.out.println("사번 | 이름   | 성별 | 급여    | 직급명 | 부서명");
			
			while(rs.next()) {
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n",
						rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6));
			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
