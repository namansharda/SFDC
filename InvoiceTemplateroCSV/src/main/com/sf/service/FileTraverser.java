package com.sf.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Constant;

import com.sf.model.InvoiceCSV;
import com.sf.utils.Constants;

//Traverse Each file in a folder 
public class FileTraverser {
	
	List<InvoiceCSV> invoiceList = new ArrayList<InvoiceCSV>();
	
	TemplateReaderFactory templateFactory = new TemplateReaderFactory();
	
	public static void main(String[] args) {
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
				
				ExcelTemplateReader reader = templateFactory.getTemplateFactory(template);
				if(reader != null)
					reader.parseExcel(thisExcel, invoiceList);
				else
					System.out.println("Not an account specific file name "+ thisExcel.getName());
			
				//new TemplateParser().readExcel(thisExcel, invoiceList);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("**********From FileTraverser.traverseFiles************");
				e.printStackTrace();
				
			}
		}
	}

	private String getTemplateForReder(File thisExcel) {
		String Name = thisExcel.getName();
		System.out.println("name = "+Name);
		// Specfic file name format not decided for implementing logic
		return Constants.TEMPLATE_DUBAI;
	}


}
