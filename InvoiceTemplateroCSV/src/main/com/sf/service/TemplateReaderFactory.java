package com.sf.service;

public class TemplateReaderFactory {
	
	public ExcelTemplateReader getTemplateFactory(String templateName) {
		
		if(templateName == null) {
			return null;
		}else if(templateName == "Template-US") {
			return new USTemplateReader();
		}/*else if(templateName == "Template-Dubai") {
			return new USTemplateReader();
		}else if(templateName == "Template-Indore SEZ Unit 1") {
			return new USTemplateReader();
		}*/
		
		return null;
	}

}
