//Recursive Java program to print all files 
//in a folder(and sub-folders) 

package com.sf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sf.model.InvoiceCSV;
import com.sf.utils.SfUtils;

public class TemplateParser 
{ 
	public void readExcel(File excel, List<InvoiceCSV> invoiceList) throws IOException {
		
		//File myFile = new File("E:\\SalseForce\\Community Portal\\Invoice Template");
		
		FileInputStream fis = new FileInputStream(excel);

		XSSFWorkbook wb=new XSSFWorkbook(fis);   
		//creating a Sheet object to retrieve the object  
		XSSFSheet sheet=wb.getSheetAt(0);
		
		CellReference cellReference = new CellReference("C14");
		
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		
		String V1 = SfUtils.getCellValueasString(sheet, cellReference);
		
		System.out.println("*************************");

		System.out.println(V1);

		System.out.println("*************************");


	}

	
} 
