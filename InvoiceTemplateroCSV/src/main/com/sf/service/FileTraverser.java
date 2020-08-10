package com.sf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;
import com.sf.utils.SfUtils;

//Traverse Each file in a folder 
public class FileTraverser {

//	public static Integer externalIdCounter = 1;
	List<InvoiceCSV> invoiceList = new ArrayList<InvoiceCSV>();
	
	TemplateReaderFactory templateFactory = new TemplateReaderFactory();
	
	public static void main(String[] args) {
//		try {
//			SfUtils.valueStorePropertyLoader("invoice_External_Id", false, null);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String path = "E:\\SalseForce\\Community Portal\\Invoice Template\\sample template";
		File maindir = new File(path);
		FileTraverser fileTraverser = new FileTraverser();
		if (maindir.exists() && maindir.isDirectory()) {
			File arr[] = maindir.listFiles();
			fileTraverser.traverseFiles(arr);
		}
		fileTraverser.createCSVFromInvoiceList();
	}
	
	void traverseFiles(File[] arr) {
		
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i].getName());
			//call the template parser method for parsing logic
			File thisExcel = arr[i];
			
			String template = getTemplateForReder(thisExcel);
			
			try {
				if(!template.isEmpty()) {
					ExcelTemplateReader reader = templateFactory.getTemplateFactory(template);
					if(reader != null)
						reader.parseExcel(thisExcel, invoiceList);
					else
						System.out.println("Not an account specific file name "+ thisExcel.getName());
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("**********From FileTraverser.traverseFiles************");
				e.printStackTrace();
				
			}
			
		}
		
		
	}

	//----- this method returns the specific template for invoice
	private String getTemplateForReder(File thisExcel) {
		String accName = "";
		String template = "";
		String fileName = thisExcel.getName();
		System.out.println("fileNmae = " + fileName);

		if (!fileName.contains("$")) {
			String[] delimitedName = fileName.split("_");
			for (String aName : delimitedName) {
				if (aName.contains(Constants.XLSX_EXTENSION)) {
					System.out.println("aName = " + aName);

					accName = aName.replace(".", "_").split("_")[0];
				}
			}
		}
		if (!accName.isEmpty()) {
			try {
				Properties property = SfUtils.accountTemplateMappingPropertyLoader();
				template = property.getProperty(accName);
			} catch (IOException e) {
				System.out.println("Template not found for file" + fileName);
				e.printStackTrace();
			}
		}
		return template;
	}
	
	
//	private void createCSVFromInvoiceList() {
//
//		Writer writer = null;
//		try {
//			writer = Files.newBufferedWriter(Paths.get("E:/SalseForce/Community Portal/Invoice Template/generatedCSV/data.csv"));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		if (writer != null) {
//			System.out.println("writer != null" );
//
//			ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
//			mappingStrategy.setType(InvoiceCSV.class);
//
//			StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(mappingStrategy)
//					.withSeparator('#').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
//
//			try {
//				System.out.println("befor writing csv  ::::::  " + this.invoiceList);
//
//				beanToCsv.write(this.invoiceList);
//
//			} catch (CsvDataTypeMismatchException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (CsvRequiredFieldEmptyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		System.out.println("****************CSV Written***************");
//
//	}
	
	private void createCSVFromInvoiceList() {
		
		final char seprator = ','; // it could be a comma or a semi colon

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("E:/SalseForce/Community Portal/Invoice Template/generatedCSV/data.csv"))) {
			writer.append("Id,Amount,BillingType,Currency,FinancialYear,Month,InvoiceStatus,PaymentTerms,Account,Project,Contact,Invoice_External_Id__c").append(System.lineSeparator());
			invoiceList.forEach(invoice -> {
		    
					try {
						writer.append("").append(seprator)
						      .append(invoice.getAmount().toString()).append(seprator)
						      .append(invoice.getBillingType()).append(seprator)
						      .append(invoice.getCurrency()).append(seprator)
						      .append("").append(seprator)
						      .append("").append(seprator)
						      .append(invoice.getInvoiceStatus()).append(seprator)
						      .append("").append(seprator)
						      .append(invoice.getAccount_Name()).append(seprator)
						      .append("").append(seprator)
						      .append("").append(seprator)
						      .append("").append(System.lineSeparator());
					} catch (IOException e) {
						e.printStackTrace();
					}
		    });
			System.out.println("CSV Created");
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
	}

}
