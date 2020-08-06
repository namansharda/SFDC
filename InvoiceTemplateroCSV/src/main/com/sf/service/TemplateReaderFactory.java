package com.sf.service;

import com.sf.utils.Constants;

public class TemplateReaderFactory {
	
	public ExcelTemplateReader getTemplateFactory(String templateName) {
		
		if(templateName == null) {
			return null;
		}else if(Constants.TEMPLATE_US.equalsIgnoreCase(templateName)) {
			return new USTemplateReader();
		}else if(Constants.TEMPLATE_DUBAI.equalsIgnoreCase(templateName)) {
			return new DubaiTemplateReader();
		}/*else if(templateName == "Template-Dubai") {
			return new USTemplateReader();
		}else if(templateName == "Template-Indore SEZ Unit 1") {
			return new USTemplateReader();
		}*/
		
		return null;
	}

}
