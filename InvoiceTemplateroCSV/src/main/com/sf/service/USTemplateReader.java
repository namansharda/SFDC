//Recursive Java program to print all files 
//in a folder(and sub-folders) 

package com.sf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;
import com.sf.utils.SfUtils;

public class USTemplateReader implements ExcelTemplateReader {

	
	public void parseExcel(File excel, List<InvoiceCSV> invoiceList) throws IOException {
		
		InvoiceCSV invoice = new InvoiceCSV();
		
		// TODO Auto-generated method stub

		// File myFile = new File("E:\\SalseForce\\Community Portal\\Invoice Template");

		FileInputStream fis = new FileInputStream(excel);

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// creating a Sheet object to retrieve the object
		XSSFSheet sheet = wb.getSheetAt(0);

		CellReference cellReference = new CellReference("M38");

		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());

		Double V1 = SfUtils.getCellValueasNumber(sheet, cellReference);

		System.out.println("*************************");

		System.out.println(V1);

		System.out.println("*************************");
		

		invoice.setCurrency(Constants.Currency_USD);
		invoice.setBillingType(Constants.BillingType_Monthly);
		invoice.setInvoiceStatus(Constants.InvoiceStatus_Draft);
		//Account Name
		//Amount
		
		invoiceList.add(invoice);

	}

}
