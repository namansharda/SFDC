package com.sf.service;

import java.io.File;
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
		System.out.println("fileNmae = "+fileName);
		String[] delimitedName = fileName.split("_");
		for(String aName : delimitedName) {
			if(aName.contains(Constants.XLSX_EXTENSION)) {
				System.out.println("aName = "+aName);
				
				 accName = aName.replace(".", "_").split("_")[0];
			}
		}
		if(!accName.isEmpty()) {
			try {
				Properties property = SfUtils.accountTemplateMappingPropertyLoader();
				template = property.getProperty(accName);
			} catch (IOException e) {
				System.out.println("Template not found for file"+ fileName);
				e.printStackTrace();
			}
		}
		return template;
	}


}
