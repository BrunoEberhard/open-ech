package  ch.openech.model.person;

import java.time.LocalDate;
import java.util.List;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.model.validation.EmptyValidator;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Address;
import ch.openech.model.person.types.KindOfEmployment;
import ch.openech.xml.write.EchSchema;

@Sizes(EchFormats.class)
public class Occupation implements Validation, Rendering {

	public static final Occupation $ = Keys.of(Occupation.class);
	
	private final transient EchSchema echSchema;
	
	@Size(100) // Wird tatsächlich in xsd nicht als Type definiert, daher nicht in EchFormats
	public String jobTitle, employer;
	public KindOfEmployment kindOfEmployment;
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
	
	@Override
	public String render(RenderType renderType) {
		return toHtml();
	}

	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.HMTL;
	}

	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		
		if (!StringUtils.isBlank(jobTitle)) {
			StringUtils.appendLine(s, "Bezeichnung:", jobTitle);
		}

		if (kindOfEmployment != null) {
			StringUtils.appendLine(s, "Erwerbsart:", EnumUtils.getText(kindOfEmployment));
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
			s.append("Gültig bis ").append(DateUtils.format(occupationValidTill));
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
			StringUtils.appendLine(s, EnumUtils.getText(kindOfEmployment));
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
			EmptyValidator.validate(resultList, this, $.kindOfEmployment);
		}
	}
	
	
}
