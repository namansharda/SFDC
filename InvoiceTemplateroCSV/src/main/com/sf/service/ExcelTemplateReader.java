package com.sf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.sf.model.InvoiceCSV;

public interface ExcelTemplateReader {

	public void parseExcel(File excel, List<InvoiceCSV> invoiceList) throws IOException;
	
}
