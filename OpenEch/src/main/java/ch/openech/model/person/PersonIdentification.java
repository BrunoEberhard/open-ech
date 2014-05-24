package  ch.openech.model.person;

import org.joda.time.ReadablePartial;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.ViewOf;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.TechnicalIds;
import  ch.openech.model.person.types.Vn;
import  ch.openech.model.types.Sex;

public class PersonIdentification implements ViewOf<Person> {

	public static final PersonIdentification PERSON_IDENTIFICATION = Keys.of(PersonIdentification.class);
	
	//
	
	public long id;
	
	public final TechnicalIds technicalIds = new TechnicalIds();

	public final Vn vn = new Vn();
	
	@Required @Size(EchFormats.baseName)
	public String firstName, officialName;
	
	@Required 
	public Sex sex;
	
	@Required 
	public ReadablePartial dateOfBirth;
	
	//
	
	public PersonIdentification() {
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
	
	public String display() {
		return toHtml();
	}
	
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, firstName, officialName);
		StringUtils.appendLine(s, DateUtils.formatPartialCH(dateOfBirth));
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

	public String idAsString() {
		return "" + id;
	}

}