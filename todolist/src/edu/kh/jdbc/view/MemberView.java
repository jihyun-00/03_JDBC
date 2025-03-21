package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.dto.Member;
import edu.kh.jdbc.service.MemberService;

public class MemberView {

	private Scanner sc = new Scanner(System.in);
	private MemberService service = new MemberService();

	public void mainMenu() {

		try {

			while (true) {
				System.out.println("===== 나의 투두리스트 =====");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 투두리스트 조회");
				System.out.println("4. 투두리스트 추가");
				System.out.println("5. 투두리스트 수정(제목, 내용)");
				System.out.println("6. 완료여부변경(Y<->N)");
				System.out.println("7. 투두리스트 삭제");
				System.out.println("8. 로그아웃");

				System.out.print("원하는 메뉴를 입력하세요 : ");
				int num = sc.nextInt();

				switch (num) {
				case 1: signIn(); break;
				case 2: logIn(); break;
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 0: break;
				default:
					System.out.println("알맞은 숫자를 입력하세요.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void signIn() throws Exception{
		
		// 회원을 입력받아 테이블에 추가 insert into
		
		int result = 0;
		
		System.out.println("\n===== 회원가입 =====");
		System.out.print("가입할 아이디 : ");
		String id = sc.next();
		
		System.out.print("가입할 비밀번호 : ");
		String pw = sc.next();
		
		System.out.println("비밀번호 확인 : ");
		String pw2 = sc.next();
		
		System.out.print("이름 : ");
		String name = sc.next();
		
		result = service.signIn(id, pw, name);
		
		if(result>0) {
			System.out.println("회원가입이 완료되었습니다.");
		} else {
			System.out.println("회원가입에 실패했습니다.");
		}
		
		
		
	}

	private void logIn() throws Exception{
		
		//List<Member> memberList = new ArrayList<>();
		
		System.out.println("\n===== 로그인 =====");
		System.out.print("아이디 : ");
		String id = sc.next();
		
		System.out.print("비밀번호 : ");
		String pw = sc.next();
		
		Member member = new Member(id, pw);
		service.logIn(member);
		
		
		
	}
}
