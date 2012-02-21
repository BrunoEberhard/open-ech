package ch.openech.dm.person;

import ch.openech.dm.code.EchCodes;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.util.StringUtils;

public class Occupation {

	public static final Occupation OCCUPATION = Constants.of(Occupation.class);
	
	public String jobTitle;
	public String employer;
	public String kindOfEmployment;
	
	public Address placeOfWork;
	public Address placeOfEmployer;
	
	//
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		
		if (!StringUtils.isBlank(jobTitle)) {
			StringUtils.appendLine(s, "Bezeichnung:", jobTitle);
		}

		if (!StringUtils.isBlank(kindOfEmployment)) {
			String text = EchCodes.kindOfEmployment.getText(kindOfEmployment);
			if (!StringUtils.isBlank(text)) StringUtils.appendLine(s, "Erwerbsart:", text);
			else StringUtils.appendLine(s, "Erwerbsart:", kindOfEmployment);
		}

		if (!StringUtils.isBlank(employer)) {
			StringUtils.appendLine(s, "Arbeitgeber:", employer);
		}
		
		if (placeOfWork != null && !placeOfWork.isEmpty()) {
			StringUtils.appendLine(s, "Arbeitsort:");
			placeOfWork.toHtml(s);
		}

		if (placeOfEmployer != null && !placeOfEmployer.isEmpty()) {
			StringUtils.appendLine(s, "Arbeitgeberort:");
			placeOfEmployer.toHtml(s);
		}

		s.append("&nbsp;</HTML>");
		return s.toString();
	}
	
}