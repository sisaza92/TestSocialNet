package com.accenture.automatization.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.accenture.automatization.dto.Credential;

public class DataDrivenUser {
		
	public final String SHEET_NAME = "Sheet1";//"Test Credentials";
	
	public FileInputStream fileInputStream = null;
	private FileOutputStream fileOutputStream = null;
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	XSSFRow row = null;

	private String xslFilePath;


		
	
	public DataDrivenUser(String xslFilePath) throws IOException {
		
		this.xslFilePath = xslFilePath;
		
	}
	
	public Credential getCredential(int rowNum)
    {
		Credential credential = null;
		
		XSSFRow row = null;
		XSSFCell usernameCell = null;
		XSSFCell passwordCell = null;
		
		String username = "";
		String password = "";
		
		try {
			
			fileInputStream = new FileInputStream(xslFilePath);
			workbook = new XSSFWorkbook(fileInputStream);
			
	        sheet = workbook.getSheet(SHEET_NAME);
	        row = sheet.getRow(rowNum);
	        usernameCell = row.getCell(0);
	        passwordCell = row.getCell(1);
	        
	        username = usernameCell.getStringCellValue();
	        password = passwordCell.getStringCellValue();
	        
	        credential = new Credential(username, password);
	        
	        fileInputStream.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return credential;
        
    }
	
	public void setStatus(int rowNum, String statusMessage){
		
		XSSFRow row = null;
		XSSFCell statusCell = null;
		
		try {
			
			fileOutputStream  = new FileOutputStream(xslFilePath);
						
	        sheet = workbook.getSheet(SHEET_NAME);
	        row = sheet.getRow(rowNum);
	        statusCell = row.createCell(2);	        
	        statusCell.setCellValue(statusMessage);	        
	        workbook.write(fileOutputStream);
	        
	        fileOutputStream.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
