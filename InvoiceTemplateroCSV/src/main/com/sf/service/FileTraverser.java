package com.sf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;
import com.sf.utils.IBException;
import com.sf.utils.SfUtils;

//Traverse Each file in a folder 
public class FileTraverser {

	static Logger logger = Logger.getLogger(FileTraverser.class.getName());

	public static Integer externalIdCounter = 1;
	List<InvoiceCSV> invoiceList = new ArrayList<InvoiceCSV>();

	public static void main(String[] args) {

		System.out.println("******************Execution Start****************");
		logger.info("******************Execution Start****************");

		externalIdCounter = SfUtils.valueStorePropertyLoader(Constants.INVOICE_EXTERNAL_ID, false, null);
		File maindir = new File(Constants.EXCELTEMPLATE_PATH);
		FileTraverser fileTraverser = new FileTraverser();
		if (maindir.exists() && maindir.isDirectory()) {
			File arr[] = maindir.listFiles();
			fileTraverser.traverseFiles(arr);
		}

		fileTraverser.createCSVFromInvoiceList();
		SfUtils.valueStorePropertyLoader(Constants.INVOICE_EXTERNAL_ID, true, externalIdCounter.toString());

		logger.info("******************Execution End****************");
		System.out.println("******************Execution End****************");
	}

	void traverseFiles(File[] arr) {
		boolean readSuccess = false;
		logger.info("No of templates to be readed:" + arr.length);
		
		for (int i = 0; i < arr.length; i++) {
			logger.info("File No : " + (i+1));
			
			// call the template parser method for parsing logic
			File thisExcel = arr[i];
			logger.info("file  for traversing =:" + thisExcel.getName());
			Properties template = getTemplateProperties(thisExcel);

			try {
				if (template != null) {
					readSuccess = extractValuesFromExcel(template, thisExcel);
					moveFile(thisExcel, readSuccess);
				}

			} catch (IBException e) {
				logger.debug("Exception caught while traversing file :" + thisExcel.getName());
				logger.error("Exception : ", e);
				logger.error(e.getMessage());
			}
		}
	}

	/*
	 * This method will move the succeed files to success and failed files to the
	 * failure folder
	 */
	private void moveFile(File thisExcel, boolean readSuccess) throws IBException {
		Date date = new Date();
		Path src = Paths.get(thisExcel.getPath());
		Path dest = null;
		if (readSuccess) {
			dest = Paths.get(Constants.SUCCESS_PATH + date.getTime() + Constants.UNDERSCORE + thisExcel.getName());
		} else {
			dest = Paths.get(Constants.FAILURE_PATH + date.getTime() + Constants.UNDERSCORE + thisExcel.getName());
		}

		try {
			Files.move(src, dest);
		} catch (IOException e) {
			logger.error("Exception : ", e);
			throw new IBException("Exception Caught while moving the file " + thisExcel.getName());
		}
		logger.info(thisExcel.getName() + " file moved successfully to :" + dest.toString());
	}

	// ----- this method returns the specific template for invoice
	private Properties getTemplateProperties(File thisExcel) {
		String accName = "";
		Properties template = null;
		String fileName = thisExcel.getName();

		if (validateFile(thisExcel)) {
			String[] delimitedName = fileName.split("_");
			for (String aName : delimitedName) {
				accName = aName.replace(".", "_").split("_")[0];
			}
		}
		if (!accName.isEmpty()) {
			try {
				template = SfUtils.accountTemplatePropertyLoader(accName);
			} catch (IOException e) {
				logger.debug("Template not found for file" + fileName);
				logger.error("Exception : ", e);
			}
		}
		logger.info("File name : " + fileName + "Extracted Template name : " + template + " for account name : " + accName);
		return template;
	}

	
	private boolean validateFile(File file) {
		String fileName = file.getName();

		if (!fileName.contains(Constants.DOLLAR)) {
			if (fileName.endsWith(Constants.XLSX_EXTENSION));
			return true;
		}
		return false;
	}

	// Creating CSV from InvoiceList
	private void createCSVFromInvoiceList() {

		logger.info("Invoice List Size " + this.invoiceList.size());
		logger.info("Invoice List Before Creating CSV" + this.invoiceList);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CSVFILE_PATH))) {
			writer.append("Id,Amount,BillingType,Currency,FinancialYear,Month,InvoiceStatus,PaymentTerms,Account,Project,Contact,Invoice_External_Id__c").append(System.lineSeparator());
			invoiceList.forEach(invoice -> {
				try {
					writer.append("").append(Constants.SEPRATOR).append(invoice.getAmount().toString())
							.append(Constants.SEPRATOR).append(invoice.getBillingType()).append(Constants.SEPRATOR)
							.append(invoice.getCurrency()).append(Constants.SEPRATOR).append("")
							.append(Constants.SEPRATOR).append("").append(Constants.SEPRATOR)
							.append(invoice.getInvoiceStatus()).append(Constants.SEPRATOR).append("")
							.append(Constants.SEPRATOR).append(invoice.getAccount_Name()).append(Constants.SEPRATOR)
							.append("").append(Constants.SEPRATOR).append(invoice.getContact())
							.append(Constants.SEPRATOR).append(invoice.getInvoice_External_Id__c())
							.append(System.lineSeparator());
				} catch (IOException e) {
					logger.error("Exception : ", e);
				}
			});

			logger.info("CSV created Successfully");
		} catch (IOException ex) {
			logger.debug("An exception caught while creating CSV" + ex.getMessage());
			logger.error("Exception : ", ex);
		}
	}

	private boolean extractValuesFromExcel(Properties templateProps, File excel) throws IBException{
		boolean readSuccess = false;
		InvoiceCSV invoice = new InvoiceCSV();

		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		
		try {
			fis = new FileInputStream(excel);
			wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);

			// creating a Sheet object to retrieve the object

			CellReference accountNameReferance = new CellReference(templateProps.getProperty("accountNameReference"));
			System.out.println(templateProps.getProperty("accountNameReference"));
			String accountName = SfUtils.getCellValueasString(sheet, accountNameReferance);

			CellReference amountReferance = new CellReference(templateProps.getProperty("amountReference"));
			System.out.println(templateProps.getProperty("amountReference"));
			Double amount = SfUtils.getCellValueasNumber(sheet, amountReferance);

//			CellReference dateReferance = new CellReference("M14");
//			String dateString = SfUtils.getCellValueasString(sheet, dateReferance);

			invoice.setInvoice_External_Id__c(SfUtils.getExternalIdValue());
			
			invoice.setCurrency(templateProps.getProperty("currency"));
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
			logger.error("Exception : ", e);
			throw new IBException("FileNotFound Exception In Class " + this.getClass().getName() + " for the file : " + excel.getName());
		} catch (IOException e) {
			readSuccess = false;
			logger.error("Exception : ", e);
			throw new IBException("IOException In Class " + this.getClass().getName() + " for the file : " + excel.getName());
		} catch (Exception e) {
			readSuccess = false;
			logger.error("Exception : ", e);
			throw new IBException("Exception In Class " + this.getClass().getName() + " for the file : " + excel.getName());
		}
		finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				logger.error("Exception : ", e);
			}
		}
		
		return readSuccess;
	}
}
