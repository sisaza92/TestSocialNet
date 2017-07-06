package com.accenture.automatization.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.accenture.automatization.dto.Credential;

public class DataDrivenUser {
		
	public final String SHEET_NAME = "Sheet1";//"Test Credentials";
	
	public FileInputStream fileInputStream = null;
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;

	
	
	public DataDrivenUser(String xslFilePath) throws IOException {
		
		fileInputStream = new FileInputStream(xslFilePath);
        workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
		
	}
	
	public Credential getCredential(int rowNum)
    {
		XSSFRow row = null;
		XSSFCell usernameCell = null;
		XSSFCell passwordCell = null;
		
		Credential credential = null;
		String username = "";
		String password = "";
		
        sheet = workbook.getSheet(SHEET_NAME);
        row = sheet.getRow(rowNum);
        usernameCell = row.getCell(0);
        passwordCell = row.getCell(1);
        
        username = usernameCell.getStringCellValue();
        password = passwordCell.getStringCellValue();
        
        credential = new Credential(username, password);
        
        return credential;
        
    }
	
	public void setStatuss(String statuss){
		
	}
	
	
	

}
