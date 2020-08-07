package com.sf.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SfUtils {
	
	public static Cell getCell(XSSFSheet sheet, CellReference cellReference) {
		Row row = sheet.getRow(cellReference.getRow());
		return row.getCell(cellReference.getCol());
	}
	
	public static String getCellValueasString(XSSFSheet sheet, CellReference cellReference) {
		String cellValue = getCell(sheet, cellReference).getStringCellValue();
		return cellValue;
	}
	
	public static Double getCellValueasNumber(XSSFSheet sheet, CellReference cellReference) {
		Double cellValue = getCell(sheet, cellReference).getNumericCellValue();
		return cellValue;
	}
	

	public static Properties accountTemplateMappingPropertyLoader () throws IOException {
		
		FileReader accountTemplate =new FileReader("resource/accountTemplateMapping.properties");
		Properties p= new Properties();
		p.load(accountTemplate);
		
		return p;
	}
	
	public static Properties accountMappingsPropertyLoader () throws IOException {
		
		FileReader accountMappings =new FileReader("resource/accountMappings.properties");
		Properties p= new Properties();
		p.load(accountMappings);
		
		return p;
	}
	
	public static Properties projectMappingPropertyLoader () throws IOException {
		
		FileReader projectMappings =new FileReader("resource/projectMappings.properties");
		Properties p= new Properties();
		p.load(projectMappings);
		
		return p;
	}
	
	public static String valueStorePropertyLoader (String propertyName, Boolean write, String updatedValue) throws IOException {
		String value ="";
		FileReader valueStore =new FileReader("resource/valueStore.properties");
		Properties p= new Properties();
		p.load(valueStore);
		
		if(!write) {
			value = p.getProperty(propertyName);
		}else {
			p.setProperty(propertyName, updatedValue);
		}
		return value;
	}
	
	public static String getAccountId(String accountName) {
		String accountId = "";
		try {
			Properties property = SfUtils.accountMappingsPropertyLoader();
			 accountId= property.getProperty(accountName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accountId;
	}
	
	
}
