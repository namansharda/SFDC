package com.sf.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sf.model.InvoiceCSV;

//Traverse Each file in a folder 
public class FileTraverser {
	
	static List<InvoiceCSV> invoiceList = new ArrayList<InvoiceCSV>();
	
	static void traverseFiles(File[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i].getName());
			//call the template parser method for parsing logic
			File thisExcel = arr[i];
			
			try {
				
				new TemplateParser().readExcel(thisExcel, invoiceList);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("**********From FileTraverser.traverseFiles************");
				e.printStackTrace();
				
			}
			
		}
	}

	public static void main(String[] args) {
		String path = "D:\\InvoiceTemplatesWithData";
		File maindir = new File(path);
		if (maindir.exists() && maindir.isDirectory()) {
			File arr[] = maindir.listFiles();
			traverseFiles(arr);
		}
	}
}
