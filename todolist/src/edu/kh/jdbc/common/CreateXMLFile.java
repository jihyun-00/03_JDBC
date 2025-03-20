package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile {

	public static void main(String[] args) {

		try {
		Scanner sc = new Scanner(System.in);
		
		Properties prop = new Properties();
		
		System.out.print("생성할 XML 파일 이름 : ");
		String file = sc.next();
		
		FileOutputStream fos = new FileOutputStream(file + ".xml");
		
		prop.storeToXML(fos, file + ".xml 파일 생성 완료!");
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
