package com.sf.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SfUtils {

	public static String getCellValueasString(XSSFSheet sheet, CellReference cellReference) {

		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		String cellValue = cell.getStringCellValue();
		
		return cellValue;
	}
	
	public static Double getCellValueasNumber(XSSFSheet sheet, CellReference cellReference) {
		
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		Double cellValue = cell.getNumericCellValue();
		
		return cellValue;
	}
	

	public static Properties accountTemplateMappingPropertyLoader () throws IOException {
		
		FileReader accountTemplate =new FileReader("accountTemplateMapping.properties");
		Properties p= new Properties();
		p.load(accountTemplate);
		
		return p;
	}
	
	public static Properties accountMappingsPropertyLoader () throws IOException {
		
		FileReader accountMappings =new FileReader("accountMappings.properties");
		Properties p= new Properties();
		p.load(accountMappings);
		
		return p;
	}
	
	public static Properties projectMappingPropertyLoader () throws IOException {
		
		FileReader projectMappings =new FileReader("projectMappings.properties");
		Properties p= new Properties();
		p.load(projectMappings);
		
		return p;
	}
	
	public static Properties valueStorePropertyLoader () throws IOException {
		
		FileReader valueStore =new FileReader("valueStore.properties");
		Properties p= new Properties();
		p.load(valueStore);
		
		return p;
	}
	
}
