package com.sf.service;

import java.io.File;

//Traverse Each file in a folder 
public class FileTraverser {
	static void traverseFiles(File[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i].getName());
			//call the template parser method for parsing logic
		}
	}

	public static void main(String[] args) {
		String path = "E:\\SalseForce\\Community Portal\\Invoice Template";
		File maindir = new File(path);
		if (maindir.exists() && maindir.isDirectory()) {
			File arr[] = maindir.listFiles();
			traverseFiles(arr);
		}
	}
}
