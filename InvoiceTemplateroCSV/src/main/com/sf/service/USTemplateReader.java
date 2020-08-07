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
		FileInputStream fis = new FileInputStream(excel);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// creating a Sheet object to retrieve the object
		XSSFSheet sheet = wb.getSheetAt(0);

		CellReference accountNameReferance = new CellReference("C14");
		String accountName = SfUtils.getCellValueasString(sheet, accountNameReferance);

		CellReference amountReferance = new CellReference("K40");
		Double amount = SfUtils.getCellValueasNumber(sheet, amountReferance);

		invoice.setCurrency(Constants.Currency_USD);
		invoice.setBillingType(Constants.BillingType_Monthly);
		invoice.setInvoiceStatus(Constants.InvoiceStatus_Draft);
		
		String accountId = SfUtils.getAccountId(accountName);
		invoice.setAccount_Name(accountId);
		invoice.setAmount(amount);

		invoiceList.add(invoice);
		System.out.println(invoiceList);
	}

}
