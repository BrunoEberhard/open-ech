package  ch.openech.model.person;

import java.util.List;

import org.threeten.bp.LocalDate;
import org.minimalj.model.Codes;
import org.minimalj.model.EmptyValidator;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.Address;
import ch.openech.xml.write.EchSchema;

@Sizes(EchFormats.class)
public class Occupation implements Validation {

	public static final Occupation OCCUPATION = Keys.of(Occupation.class);
	
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
