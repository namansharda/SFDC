package com.sf.service;

import java.io.BufferedWriter;
import java.io.File;
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

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;
import com.sf.utils.IBException;
import com.sf.utils.SfUtils;

//Traverse Each file in a folder 
public class FileTraverser {

	static Logger logger = Logger.getLogger(FileTraverser.class.getName());

	public static Integer externalIdCounter = 1;
	List<InvoiceCSV> invoiceList = new ArrayList<InvoiceCSV>();
	TemplateReaderFactory templateFactory = new TemplateReaderFactory();

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
		for (int i = 0; i < arr.length; i++) {

			// call the template parser method for parsing logic
			File thisExcel = arr[i];
			logger.info("file  for traversing =:" + thisExcel.getName());
			String template = getTemplateForReder(thisExcel);

			try {
				if (!template.isEmpty()) {
					ExcelTemplateReader reader = templateFactory.getTemplateFactory(template);
					if (reader != null) {
						logger.debug("Reader class template " + reader.getClass().getName());
						readSuccess = reader.parseExcel(thisExcel, invoiceList);
					} else
						logger.debug("Not an account specific file name " + thisExcel.getName());
				}
				try {
					moveFile(thisExcel, readSuccess);
				} catch (IBException e) {
					logger.debug(e.getMessage());
				}
			} catch (IOException e) {
				logger.debug("Exception caught while traversing file :" + thisExcel.getName());
				logger.error(e.getStackTrace());
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
			logger.error(e.getStackTrace());
			throw new IBException("Exception Caught while moving the file " + thisExcel.getName());
		}
		logger.info(thisExcel.getName() + " file moved successfully to :" + dest.toString());
	}

	// ----- this method returns the specific template for invoice
	private String getTemplateForReder(File thisExcel) {
		String accName = "";
		String template = "";
		String fileName = thisExcel.getName();

		if (validateFile(thisExcel)) {
			String[] delimitedName = fileName.split("_");
			for (String aName : delimitedName) {
				accName = aName.replace(".", "_").split("_")[0];
			}
		}
		if (!accName.isEmpty()) {
			try {
				Properties property = SfUtils.accountTemplateMappingPropertyLoader();
				template = property.getProperty(accName);
			} catch (IOException e) {
				logger.debug("Template not found for file" + fileName);
				logger.error(e.getStackTrace());
				;
			}
		}
		logger.info(
				"File name : " + fileName + "Extracted Template name : " + template + " for account name : " + accName);
		return template;
	}

	private boolean validateFile(File file) {
		String fileName = file.getName();

		if (!fileName.contains("$")) {
			if (fileName.endsWith(Constants.XLSX_EXTENSION))
				;
			return true;
		}

		return false;
	}

	// Creating CSV from InvoiceList
	private void createCSVFromInvoiceList() {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CSVFILE_PATH))) {
			writer.append(
					"Id,Amount,BillingType,Currency,FinancialYear,Month,InvoiceStatus,PaymentTerms,Account,Project,Contact,Invoice_External_Id__c")
					.append(System.lineSeparator());
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
					logger.error(e.getStackTrace());
				}
			});

			logger.info("CSV created Successfully");
		} catch (IOException ex) {
			logger.debug("An exception caught while creating CSV" + ex.getMessage());
			logger.error(ex.getStackTrace());
		}
	}

}
