package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.Sex;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class PersonIdentification {

	public static final PersonIdentification PERSON_IDENTIFICATION = Constants.of(PersonIdentification.class);
	
	//
	
	public final TechnicalIds technicalIds = new TechnicalIds();

	public String vn;
	
	@Required @FormatName(EchFormats.baseName) 
	public String firstName, officialName;
	
	@Required 
	public String sex = Sex.getDefault();
	
	@Required @Date(partialAllowed = true)
	public String dateOfBirth;
	
	//
	
	public PersonIdentification() {
	}
	
	public void clear() {
		vn = firstName = dateOfBirth = officialName = null;
		sex = "1";
		technicalIds.clear();
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(firstName) && StringUtils.isBlank(officialName);
	}
	
	public String getId() {
		if (technicalIds.localId.isOpenEch()) {
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
		return Sex.Maennlich.getKey().equals(sex);
	}

	public boolean isFemale() {
		return Sex.Weiblich.getKey().equals(sex);
	}

	public boolean isEqual(PersonIdentification partner) {
		return partner != null && StringUtils.equals(partner.vn, vn);
	}

}
