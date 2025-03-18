package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
//		  입력을 받고 결과를 출력하는 역할
public class UserView {
	
	// 필드
	private Scanner sc = new Scanner(System.in);
	private UserService service = new UserService();
	private int ArrayList;
	
	
	// 메서드
	/**
	 *  JDBCTemplate 사용 테스트
	 */
	public void test() {
		
		// 입력된 ID와 일치하는 USER 정보 조회
		System.out.print("ID 입력 : ");
		String input = sc.next();
		
		// 서비스 호출 후 결과 반환 받기
		User user = service.selectId(input);
		
		// 결과에 따라 사용자에게 보여줄 응답화면 결정
		if(user==null) {
			System.out.println("없습니다.");
		} else {
			System.out.println(user);
		}
	}

	
	/* User 관리 프로그램 메인 메뉴 */
	public void mainMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch(input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				
				case 0 : System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		}while(input != 0);
		
	} // mainMenu() 종료
	
	private void insertUser() {
		
		
		int result = 0;
		
		
		System.out.print("추가할 아이디 : ");
		String id = sc.next();
		
		System.out.print("추가할 비밀번호 : ");
		String pw = sc.next();
		
		System.out.print("추가할 이름 : ");
		String name = sc.next();
		
		User user = new User(0, id, pw, name, "");
		
		result = service.insertUser(user);
		
		if(result>0) {
			System.out.println(name + "추가되었습니다.");
		} else {
			System.out.println("추가되지 않았습니다.");
		}
		
		
		
		
		
		
		
		
	}
	
	private void selectAll() {
		
		List<User> userList = service.selectAll();
		
		for(User u : userList) {
			System.out.println(u);
		}
		
		
	}
	
	private void selectName() {
		
		System.out.print("검색어를 입력하세요 : ");
		String search = sc.next();
		
		List<User> userList = service.selectName(search);
		
		
		
		for(User u : userList) {
			System.out.println(u);
		}
		
		
	}
	
	/**
	 *  4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)
	 */
	private void selectUser() {
		
		System.out.print("검색할 유저의 번호를 입력하세요 : ");
		int userNo = sc.nextInt();
		
		User user = service.selectUser(userNo);
		
		if(user!=null) {
			System.out.println(user);
			
		} else {
			System.out.println("해당하는 유저가 없습니다.");
		}
		
		
	}
	
	/**
	 * 5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)
	 */
	private void deleteUser() {
		
		int result = 0;
		
		System.out.print("삭제하고 싶은 유저번호를 입력하세요 :");
		int userNo = sc.nextInt();
		
		result = service.deleteUser(userNo);
		
		if(result>0) {
			System.out.print(userNo + "번 유저가 삭제되었습니다.");
			
		} else {
			System.out.println("해당하는 유저가 없습니다.");
		}
		
		
		
	}
	
	/**
	 * 6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)
	 */
	private void updateName() {
		
		int result =0;
		
		System.out.print("ID를 입력하세요 : ");
		String id = sc.next();
		
		System.out.print("PW를 입력하세요 : ");
		String pw = sc.next();
		
		System.out.print("수정할 이름을 입력하세요 : ");
		String name = sc.next();
		
		
		result = service.updateName(id, pw, name);
		
		if(result>0) {
			System.out.println(id + "님의 이름이 수정되었습니다.");
		} else {
			System.out.println("일치하는 회원이 없습니다.");
		}
				
	}
	
	/**
	 * 7. User 등록(아이디 중복 검사)
	 */
	private void insertUser2() {
		
		int result = 0;
		
		System.out.print("등록할 아이디를 입력하세요 : ");
		String id = sc.next();
		
		System.out.print("등록할 비밀번호를 입력하세요 : ");
		String pw = sc.next();
		
		System.out.print("등록할 이름을 입력하세요 : ");
		String name = sc.next();
		
		result = service.insertUser2(id, pw, name);
		
		if(result>0) {
			System.out.println(name + "님이 추가되었습니다.");
		} else {
			System.out.println("아이디가 중복됩니다.");
		}
		
		
		
	}
	
	/**
	 * 8. 여러 User 등록하기
	 */
	private void multiInsertUser() {
			
			int result = 0;
			
			while(true) {
			System.out.print("추가할 아이디 : ");
			String id = sc.next();
			
			System.out.print("추가할 비밀번호 : ");
			String pw = sc.next();
			
			System.out.print("추가할 이름 : ");
			String name = sc.next();
			
			User user = new User(0, id, pw, name, "");
			
			result = service.multiInsertUser(user);
			
			if(result>0) {
				System.out.println(name + "추가되었습니다.");
			} else {
				System.out.println("추가되지 않았습니다.");
			}
			
			System.out.print("더 추가하시겠습니까?(Y/N) : ");
			String answer = sc.next();
			
			if(answer.equalsIgnoreCase("y")) {
				continue;
			} else if(answer.equalsIgnoreCase("n")) {
				break;
			}else {
				System.out.println("알맞은 대답을 입력하세요.");
				break;
			}
			
			}
			
			
		
		
		
	}
	
	

}
