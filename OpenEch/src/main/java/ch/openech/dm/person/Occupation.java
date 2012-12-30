package ch.openech.dm.person;

import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.Address;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.EmptyValidator;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.Codes;
import ch.openech.mj.model.annotation.Code;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.annotation.Sizes;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;

@Sizes(EchFormats.class)
public class Occupation implements Validation {

	public static final Occupation OCCUPATION = Constants.of(Occupation.class);
	
	private final transient EchSchema echSchema;
	
	@Size(100) // Wird tatsächlich in xsd nicht als Type definiert, daher nicht in EchFormats
	public String jobTitle, employer;
	@Code
	public String kindOfEmployment;
	public LocalDate occupationValidTill;
	
	public Address placeOfWork;
	public Address placeOfEmployer;
	
	//

	public Occupation() {
		this.echSchema = null;
	}

	public Occupation(EchSchema echSchema) {
		this.echSchema = echSchema;
	}

	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		
		if (!StringUtils.isBlank(jobTitle)) {
			StringUtils.appendLine(s, "Bezeichnung:", jobTitle);
		}

		if (kindOfEmployment != null) {
			StringUtils.appendLine(s, "Erwerbsart:", Codes.getCode(OCCUPATION.kindOfEmployment).getText(kindOfEmployment));
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
		
		if (occupationValidTill != null) {
			s.append("Gültig bis "); s.append(DateUtils.formatCH(occupationValidTill));
		}
		
		s.append("&nbsp;</HTML>");
		return s.toString();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (!StringUtils.isBlank(jobTitle)) {
			StringUtils.appendLine(s, "Bezeichnung:", jobTitle);
		}

		if (kindOfEmployment != null) {
			StringUtils.appendLine(s, Codes.getCode(OCCUPATION.kindOfEmployment).getText(kindOfEmployment));
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
		return s.toString().replace("<br>", "\n");
	}

	@Override
	public void validate(List<ValidationMessage> resultList) {
		if (echSchema.kindOfEmploymentMandatory()) {
			EmptyValidator.validate(resultList, this, OCCUPATION.kindOfEmployment);
		}
	}
	
	
}
