package com.sf.model;

public class InvoiceCSV {
	
	String Id;
	Double Amount;
	String Account_Name;
	String Currency;
	String InvoiceStatus;
	String Project;
	String Contact;
	String Invoice_External_Id__c;
	String BillingType;
	String invoiceDate;
	String filePath;
	String fileName;
	boolean readFlag;
	boolean csvFlag;
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public Double getAmount() {
		return Amount;
	}
	public void setAmount(Double amount) {
		Amount = amount;
	}
	public String getAccount_Name() {
		return Account_Name;
	}
	public void setAccount_Name(String account_Name) {
		Account_Name = account_Name;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getInvoiceStatus() {
		return InvoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		InvoiceStatus = invoiceStatus;
	}
	public String getProject() {
		return Project;
	}
	public void setProject(String project) {
		Project = project;
	}
	public String getContact() {
		return Contact;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	public String getInvoice_External_Id__c() {
		return Invoice_External_Id__c;
	}
	public void setInvoice_External_Id__c(String invoice_External_Id__c) {
		Invoice_External_Id__c = invoice_External_Id__c;
	}
	public String getBillingType() {
		return BillingType;
	}
	public void setBillingType(String billingType) {
		BillingType = billingType;
	}
	
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	
//-----------------For Atomicity------------------------------
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isReadFlag() {
		return readFlag;
	}
	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}
	public boolean isCsvFlag() {
		return csvFlag;
	}
	public void setCsvFlag(boolean csvFlag) {
		this.csvFlag = csvFlag;
	}
	
	//-----------------For Atomicity------------------------------	
	
	
	
	@Override
	public String toString() {
		return "InvoiceCSV [Id=" + Id + ", Amount=" + Amount + ", Account_Name=" + Account_Name + ", Currency="
				+ Currency + ", InvoiceStatus=" + InvoiceStatus + ", Project=" + Project + ", Contact=" + Contact
				+ ", Invoice_External_Id__c=" + Invoice_External_Id__c + ", BillingType=" + BillingType
				+ ", invoiceDate=" + invoiceDate + "]";
	}
	

}
