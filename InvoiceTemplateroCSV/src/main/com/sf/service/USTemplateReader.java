//Recursive Java program to print all files 
//in a folder(and sub-folders) 

package com.sf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;
import com.sf.utils.IBException;
import com.sf.utils.SfUtils;

public class USTemplateReader implements ExcelTemplateReader {

	static Logger logger = Logger.getLogger(USTemplateReader.class.getName());
	
	public boolean parseExcel(File excel, List<InvoiceCSV> invoiceList) throws IBException {
		
		
		boolean readSuccess = false;
		InvoiceCSV invoice = new InvoiceCSV();
		
		FileInputStream fis = null;
		XSSFWorkbook wb;
		try {
			fis = new FileInputStream(excel);
			wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			
			// creating a Sheet object to retrieve the object

			CellReference accountNameReferance = new CellReference("C14");
			String accountName = SfUtils.getCellValueasString(sheet, accountNameReferance);

			CellReference amountReferance = new CellReference("K40");
			Double amount = SfUtils.getCellValueasNumber(sheet, amountReferance);
			
//			CellReference dateReferance = new CellReference("M14");
//			String dateString = SfUtils.getCellValueasString(sheet, dateReferance);

			invoice.setInvoice_External_Id__c(SfUtils.getExternalIdValue());
			
			invoice.setCurrency(Constants.Currency_USD);
			invoice.setBillingType(Constants.BillingType_Monthly);
			invoice.setInvoiceStatus(Constants.InvoiceStatus_Draft);
			
//			String accountId = SfUtils.getAccountId(accountName);
			
			invoice.setAccount_Name(SfUtils.getAccountId(accountName));
			invoice.setAmount(amount);
			invoice.setContact(SfUtils.getContactId(accountName));
//			invoice.setInvoiceDate(dateString);

			invoiceList.add(invoice);
			
			readSuccess = true;
			
		} catch (FileNotFoundException e) {
			readSuccess = false;
			logger.error(e.getStackTrace());
			throw new IBException("FileNotFound Exception In Class " + this.getClass().getName() + " for the file : " + excel.getName());
		} catch (IOException e) {
			readSuccess = false;
			logger.error(e.getStackTrace());
			throw new IBException("IOException In Class " + this.getClass().getName() + " for the file : " + excel.getName());
		}finally {
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				logger.error(e.getStackTrace());
			}
		}
		
		logger.info("CSV ReadSuccess : " + readSuccess);
		return readSuccess;
		
	}

}
