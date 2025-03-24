package edu.kh.jdbc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Todo {

	private int todoNo;
	private String userId;
	private String todoTitle;
	private String todoContent;
	private String completeYN;
	private String writeDate;
	
	
	@Override
	public String toString() {
		return "번호 : " + todoNo + /*" /  아이디 : " + userId + */ " /  제목 : " + todoTitle + " /  내용 : "
				+ todoContent + " /  완료여부 : " + completeYN + " /  작성일 : " + writeDate;
	}

}



