package ch.openech.dm.person;

import org.joda.time.LocalDate;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.dm.person.types.Vn;
import ch.openech.dm.types.Sex;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class PersonIdentification {

	public static final PersonIdentification PERSON_IDENTIFICATION = Constants.of(PersonIdentification.class);
	
	//
	
	public final TechnicalIds technicalIds = new TechnicalIds();

	// TODO Make Class
	// Ist erstaunlicherweise wirklich nicht zwingend
	public final Vn vn = new Vn();
	
//	@Validation(Vn.class)
//	public String vn;
	
	@Required @Size(EchFormats.baseName)
	public String firstName, officialName;
	
	@Required 
	public Sex sex;
	
	@Required 
	// @Date(partialAllowed = true)
	public LocalDate dateOfBirth;
	
	//
	
	public PersonIdentification() {
	}
	
	public void clear() {
		vn.value = firstName = officialName = null;
		dateOfBirth = null;
		sex = null;
		technicalIds.clear();
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(firstName) && StringUtils.isBlank(officialName);
	}
	
	public String getId() {
		if (technicalIds.localId.openEch()) {
			return technicalIds.localId.personId;
		} else {
			return null;
		}
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		s.append("<HTML>");
		toHtml(s);
		s.append("</HTML>");
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, firstName, officialName);
		StringUtils.appendLine(s, DateUtils.formatCH(dateOfBirth));
	}
	
	public boolean isMale() {
		return Sex.maennlich.equals(sex);
	}

	public boolean isFemale() {
		return Sex.weiblich.equals(sex);
	}

	public boolean isEqual(PersonIdentification partner) {
		return partner != null && StringUtils.equals(partner.vn.value, vn.value);
	}

}
