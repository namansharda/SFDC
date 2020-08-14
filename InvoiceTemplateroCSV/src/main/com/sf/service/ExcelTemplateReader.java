package com.sf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.sf.model.InvoiceCSV;
import com.sf.utils.IBException;

public interface ExcelTemplateReader {

	public boolean parseExcel(File excel, List<InvoiceCSV> invoiceList) throws IBException;
	
}
