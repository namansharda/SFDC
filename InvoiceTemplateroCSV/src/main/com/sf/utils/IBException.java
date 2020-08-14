package com.sf.utils;

//This custom exception class represents use-defined exception  
public class IBException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2865693303370981849L;

	public IBException(){
		super();
	}
	
	public IBException(String message){
		super(message);
	}
	
	public IBException(String message, Exception e){
		super(message,e);
	}
}
