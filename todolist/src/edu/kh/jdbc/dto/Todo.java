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
@ToString
public class Todo {

	private int todoNo;
	private String userId;
	private String todoTitle;
	private String todoContent;
	private String completeYN;
	private String writeDate;

}
