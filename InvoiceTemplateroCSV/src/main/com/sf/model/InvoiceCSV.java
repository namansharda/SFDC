package com.sf.model;

public class InvoiceCSV {
	
	String Id;
	String Account_Name;
	String Currency;
	String InvoiceStatus;
	String Project;
	String Contact;
	String Invoice_External_Id__c;
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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

}
