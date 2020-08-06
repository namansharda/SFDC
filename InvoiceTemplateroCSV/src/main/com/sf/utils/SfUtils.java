package com.sf.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SfUtils {

	public static String getCellValueasString(XSSFSheet sheet, CellReference cellReference) {

		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());

		String cellValue = cell.getStringCellValue();
		
		return cellValue;
	}

}
