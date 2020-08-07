package com.sf.service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
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
		String path = "D:\\TemplateToCSV\\InvoiceTemplatesWithData";
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
	
	
	private void createCSVFromInvoiceList() {

		Writer writer = null;
		try {
			writer = Files.newBufferedWriter(Paths.get("D:\\TemplateToCSV\\CSV\\TransformedInvoiceCSV"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (writer != null) {
			System.out.println("writer != null" );

			ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
			mappingStrategy.setType(InvoiceCSV.class);

			StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(mappingStrategy)
					.withSeparator('#').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			try {
				System.out.println("befor writing csv  ::::::  " + this.invoiceList);

				beanToCsv.write(this.invoiceList);

			} catch (CsvDataTypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvRequiredFieldEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("****************CSV Written***************");

	}

}
