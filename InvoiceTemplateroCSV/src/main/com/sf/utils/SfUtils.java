package com.sf.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.sf.service.FileTraverser;

public class SfUtils {

	public static Cell getCell(XSSFSheet sheet, CellReference cellReference) {
		Row row = sheet.getRow(cellReference.getRow());
		return row.getCell(cellReference.getCol());
	}

	public static String getCellValueasString(XSSFSheet sheet, CellReference cellReference) {
		String cellValue = getCell(sheet, cellReference).getStringCellValue();
		return cellValue;
	}

	public static Double getCellValueasNumber(XSSFSheet sheet, CellReference cellReference) {
		Double cellValue = getCell(sheet, cellReference).getNumericCellValue();
		return cellValue;
	}

	public static Properties accountTemplateMappingPropertyLoader() throws IOException {

		FileReader accountTemplate = new FileReader("resource/accountTemplateMapping.properties");
		Properties p = new Properties();
		p.load(accountTemplate);

		return p;
	}

	public static Properties accountMappingPropertyLoader() throws IOException {

		FileReader accountMappings = new FileReader("resource/accountMappings.properties");
		Properties p = new Properties();
		p.load(accountMappings);

		return p;
	}

	public static Properties projectMappingPropertyLoader() throws IOException {

		FileReader projectMappings = new FileReader("resource/projectMappings.properties");
		Properties p = new Properties();
		p.load(projectMappings);

		return p;
	}

	public static Properties accountContactMappingPropertyLoader() throws IOException {

		FileReader accountContactMapping = new FileReader("resource/accountContactMapping.properties");
		Properties p = new Properties();
		p.load(accountContactMapping);

		return p;
	}

	public static Integer valueStorePropertyLoader(String propertyName, Boolean write, String updatedValue) {
		String value = "";
		Integer intValue = null;
		// FileReader valueStore = null;
		FileInputStream valueStoreIn = null;
		Properties p = new Properties();
		try {
			valueStoreIn = new FileInputStream("resource/valueStore.properties");
			p.load(valueStoreIn);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("********** valueStore.properties not found **********");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("********** valueStore.properties I/O Exception **********");
			e.printStackTrace();
		}

		if (!write) {
			value = p.getProperty(propertyName);
		} else {
			try {
				valueStoreIn.close();

				FileOutputStream valueStoreOut = new FileOutputStream("resource/valueStore.properties");
				p.setProperty(propertyName, updatedValue);
				p.store(valueStoreOut, null);
				valueStoreOut.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!value.isEmpty()) {
			intValue = Integer.parseInt(value);
		}
		return intValue;
	}

	public static String getExternalIdValue() {

		FileTraverser.externalIdCounter = FileTraverser.externalIdCounter + 1;
		return FileTraverser.externalIdCounter.toString();

	}

	public static String getAccountId(String accountName) {
		String accountId = "";
		try {
			Properties property = SfUtils.accountMappingPropertyLoader();
			accountId = property.getProperty(accountName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accountId;
	}

	public static String getContactId(String accountName) {
		String accountId = "";
		try {
			Properties property = SfUtils.accountContactMappingPropertyLoader();
			accountId = property.getProperty(accountName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accountId;
	}
}
