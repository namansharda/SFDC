package com.sf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

		fileTraverser.moveFile();
		
		SfUtils.valueStorePropertyLoader(Constants.INVOICE_EXTERNAL_ID, true, externalIdCounter.toString());

		logger.info("******************Execution End****************");
		System.out.println("******************Execution End****************");
	}

	void traverseFiles(File[] arr) {
		//boolean readSuccess = false;
		logger.info("No of templates to be readed:" + arr.length);
		
		for (int i = 0; i < arr.length; i++) {
			logger.info("File No : " + (i+1));
			
			// call the template parser method for parsing logic
			File thisExcel = arr[i];
			logger.info("file  for traversing =:" + thisExcel.getName());
			Properties template = getTemplateProperties(thisExcel);

			try {
				if (template != null) {
					extractValuesFromExcel(template, thisExcel);
					//moveFile(thisExcel, readSuccess);
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
	private void moveFile() {

		for (InvoiceCSV thisInvoice : this.invoiceList) {

			Date date = new Date();
			Path src = Paths.get(thisInvoice.getFilePath());
			Path dest = null;
			if (thisInvoice.isReadFlag() && thisInvoice.isCsvFlag()) {
				dest = Paths.get(
						Constants.SUCCESS_PATH + date.getTime() + Constants.UNDERSCORE + thisInvoice.getFileName());
			} else {
				dest = Paths.get(
						Constants.FAILURE_PATH + date.getTime() + Constants.UNDERSCORE + thisInvoice.getFileName());
			}

			try {

				Files.move(src, dest);
				logger.info(thisInvoice.getFileName() + " file moved successfully to :" + dest.toString());
				
			} catch (FileSystemException e) {
				logger.error("Exception : ", e);
				logger.error(new IBException("Exception Caught while moving the file " + thisInvoice.getFileName()).toString());
			} catch (IOException e) {
				logger.error("Exception : ", e);
				logger.error(new IBException("Exception Caught while moving the file " + thisInvoice.getFileName()).toString());
			}

			
		}
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
		
		accName = accName.trim().replace(" ", "_");
		
		if (!accName.isEmpty()) {
			try {
				template = SfUtils.accountTemplatePropertyLoader(accName);
			} catch (IOException e) {
				logger.debug("Template not found for file" + fileName);
				logger.error("Exception : ", e);
			}
		}
		logger.info("File name : " + fileName + " Extracted Property file Values : " + template + " for account name : " + accName);
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
					
					invoice.setCsvFlag(true);
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
		HSSFWorkbook hssfWb = null;
		String accountName = null;
		Double amount = null;
		
		try {
			fis = new FileInputStream(excel);
			
			if (excel.getName().endsWith(Constants.XLSX_EXTENSION)) {

				wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0);

				// creating a Sheet object to retrieve the object

				CellReference accountNameReferance = new CellReference(templateProps.getProperty("accountNameReference"));
				accountName = SfUtils.getCellValueasString(sheet, accountNameReferance);

				CellReference amountReferance = new CellReference(templateProps.getProperty("amountReference"));
				amount = SfUtils.getCellValueasNumber(sheet, amountReferance);
				
			}else if(excel.getName().endsWith(Constants.XLS_EXTENSION)) {
				
				hssfWb = new HSSFWorkbook(fis);
				HSSFSheet hssfSheet = hssfWb.getSheetAt(0);
				
				CellReference accountNameReferance = new CellReference(templateProps.getProperty("accountNameReference"));
				accountName = SfUtils.getHSSFCellValueasString(hssfSheet, accountNameReferance);

				CellReference amountReferance = new CellReference(templateProps.getProperty("amountReference"));
				amount = SfUtils.getHSSFCellValueasNumber(hssfSheet, amountReferance);
				
				
			}
//			CellReference dateReferance = new CellReference("M14");
//			String dateString = SfUtils.getCellValueasString(sheet, dateReferance);

			invoice.setInvoice_External_Id__c(SfUtils.getExternalIdValue());
			
			invoice.setCurrency(templateProps.getProperty("currency"));
			invoice.setBillingType(Constants.BillingType_Monthly);
			invoice.setInvoiceStatus(Constants.InvoiceStatus_Draft);

//			String accountId = SfUtils.getAccountId(accountName);
			String aName = accountName.replaceAll("\n", "__").split("__")[0];
			aName = aName.replaceAll(" ", "_");
			invoice.setAccount_Name(SfUtils.getAccountId(aName));
			invoice.setAmount(amount);
			invoice.setContact(SfUtils.getContactId(aName));
//			invoice.setInvoiceDate(dateString);

			readSuccess = true;
			
			invoice.setFileName(excel.getName());
			invoice.setFilePath(excel.getPath());
			invoice.setReadFlag(readSuccess);
			
			invoiceList.add(invoice);

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
