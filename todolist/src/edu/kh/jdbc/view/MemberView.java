package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.View;

import edu.kh.jdbc.dto.Member;
import edu.kh.jdbc.dto.Todo;
import edu.kh.jdbc.service.MemberService;

public class MemberView {

	private Scanner sc = new Scanner(System.in);
	private MemberService service = new MemberService();
	List<Member> loginList = new ArrayList<>();

	public void mainMenu() {

		int num = 0;

		do {
			try {

				System.out.println("\n===== 나의 투두리스트 =====");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 투두리스트 조회");
				System.out.println("4. 투두리스트 추가");
				System.out.println("5. 투두리스트 수정(제목, 내용)");
				System.out.println("6. 완료여부변경(Y<->N)");
				System.out.println("7. 투두리스트 삭제");
				System.out.println("8. 로그아웃");

				System.out.print("원하는 메뉴를 입력하세요 : ");
				num = sc.nextInt();
				sc.nextLine();

				switch (num) {
				case 1: signIn(); break;
				case 2: logIn(); break;
				case 3: System.out.println("\n===== 내 투두리스트 조회 ====="); viewTodoList(); break;
				case 4: System.out.println("\n===== 투두리스트 추가 ====="); addTodoList(); break;
				case 5: updateTodoList(); break;
				case 6: changeYN(); break;
				case 7: deleteTodoList(); break;
				case 8: logout(); break;
				case 0: System.out.println("프로그램이 종료되었습니다."); break;
				default: System.out.println("알맞은 숫자를 입력하세요.");
				}
			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				num = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (num != 0);

	}
	
	/** 1. 회원가입
	 * @throws Exception
	 */
	private void signIn() throws Exception{
		
		// 회원을 입력받아 테이블에 추가 insert into
		
		int result = 0;
		
		System.out.println("\n===== 회원가입 =====");
		System.out.print("가입할 아이디 : ");
		String id = sc.next();
		
		System.out.print("가입할 비밀번호 : ");
		String pw = sc.next();
		
		System.out.print("비밀번호 확인 : ");
		String pw2 = sc.next();
		
		if(!pw.equals(pw2)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return;
		}
		
		System.out.print("이름 : ");
		String name = sc.next();
		
		result = service.signIn(id, pw, name);
		
		if(result>0) {
			System.out.println("회원가입이 완료되었습니다.");
		} else {
			System.out.println("회원가입에 실패했습니다.");
		}
		
		
		
	}

	/** 로그인
	 * @throws Exception
	 */
	private void logIn() throws Exception{
		
		List<Member> memberList = new ArrayList<>();
		
		System.out.println("\n===== 로그인 =====");
		System.out.print("아이디 : ");
		String id = sc.next();
		
		System.out.print("비밀번호 : ");
		String pw = sc.next();
		
		memberList = service.logIn(id, pw);
		
		for(Member m : memberList) {
			if(m.getUserId().equals(id) && m.getUserPw().equals(pw)) {
				System.out.println(m.getUserName() + "님 로그인 되었습니다.\n");
				loginList.add(new Member(m.getUserId(), m.getUserPw(), m.getUserName(), m.getSignupDate()));
			}
//			if(!memberList.getUserId().contains(id) || !(memberList.getUserPw().contains(pw))) {
//				System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
//			}
		}
		if(loginList.isEmpty()) {
			System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
		}
		
		
		
	}
	
	/**
	 * 3. 투두리스트 조회
	 */
	private void viewTodoList() throws Exception {
		
		
		List<Todo> todoList = new ArrayList<Todo>();
		
		if(loginList.isEmpty()) {
			System.out.println("로그인 먼저 진행해주세요.");
			return;
		}

		System.out.println("==== " + loginList.get(0).getUserName() + "님의 리스트 ====");
		todoList = service.viewTodoList(loginList);

		for (Todo t : todoList) {
			System.out.println(t);
			return;
		}

		if (todoList.isEmpty()) {
			System.out.println("등록된 할 일이 없습니다.\n");
		}
		
	}
	
	/** 4. 투두리스트 추가
	 * @throws Exception
	 */
	private void addTodoList() throws Exception { //재확인 null
		
		
		int result = 0;
		
		if(loginList.isEmpty()) {
			System.out.println("로그인 먼저 진행해주세요.");
			return;
		}
		
		System.out.print("할 일 제목 : ");
		String title = sc.nextLine();
		
		
		
		System.out.print("할 일 내용 : ");
		String content = sc.nextLine();
		
		
		result = service.addTodoList(title, content, loginList);
		
		if(result>0) {
			System.out.println("할 일이 추가되었습니다.");
			viewTodoList();
		} else {
			System.out.println("다시 시도해주세요.");
		}
		
		
	}
	
	/** 5. 투두리스트 수정(제목, 내용)
	 * 
	 */
	private void updateTodoList() throws Exception{ //재확인 null
		
		System.out.println("\n===== 투두리스트 수정(제목, 내용) =====\n");
		
		if(loginList.isEmpty()) {
			System.out.println("로그인 먼저 진행해주세요.");
			return;
		}
		
		int result = 0;
		
		viewTodoList();
		
		System.out.print("수정할 리스트의 번호를 선택하세요 : ");
		int num = sc.nextInt();
		
		System.out.print("수정할 제목을 입력하세요 : ");
		String title = sc.nextLine();
		sc.next();
		
		System.out.print("수정할 내용을 입력하세요 : ");
		String content = sc.nextLine();
		sc.next();
		
		result = service.updateTodoList(num, title, content, loginList);
		
		if(result >0) {
			System.out.println("수정되었습니다.");
			viewTodoList();
		} else {
			System.out.println("다시 시도해주세요.");
		}		
		
	}
	
	/**
	 *  6. 완료여부변경(Y<->N)
	 */
	private void changeYN() throws Exception {
		
		System.out.println("\n===== 완료여부변경(Y<->N) =====\n");
		
		if(loginList.isEmpty()) {
			System.out.println("로그인 먼저 진행해주세요.");
			return;
		}
		
		int result = 0;
		
		viewTodoList();
		
		
		
		System.out.print("완료여부를 변경할 리스트의 번호를 선택하세요 : ");
		int num = sc.nextInt();
		
		System.out.print("완료하였습니까? (Y/N) ");
		char yn = sc.next().toUpperCase().charAt(0);
				
		result = service.changeYN(num, yn, loginList);
		
		if(result >0) {
			System.out.println("변경되었습니다.");
			viewTodoList();
		} else {
			System.out.println("다시 시도해주세요.");
		}		
		
		
		
	}
	
	/**
	 *  7. 투두리스트 삭제
	 */
	private void deleteTodoList() throws Exception {
		
		System.out.println("\n===== 투두리스트 삭제 =====\n");
		
		if(loginList.isEmpty()) {
			System.out.println("로그인 먼저 진행해주세요.");
			return;
		}
		
		int result=0;
		
		viewTodoList();
		
		System.out.print("삭제할 리스트의 번호를 입력하세요 : ");
		int num = sc.nextInt();
		
		result = service.deleteTodoList(num, loginList);
		
		if(result>0) {
			System.out.println("삭제되었습니다.");
			viewTodoList();
		} else {
			System.out.println("다시 시도해주세요.");
		}
		
	}
	
	/**
	 * 8. 로그아웃
	 */
	private void logout() {
		
		System.out.println(loginList.get(0).getUserName() + "님 로그아웃 되었습니다.");
		loginList.clear();
	}
}
