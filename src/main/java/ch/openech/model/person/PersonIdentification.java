package  ch.openech.model.person;

import java.util.Locale;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.TechnicalIds;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Sex;

public class PersonIdentification implements View<Person>, Rendering {

	public static final PersonIdentification $ = Keys.of(PersonIdentification.class);
	
	//
	
	public Object id;
	
	public final TechnicalIds technicalIds = new TechnicalIds();

	public final Vn vn = new Vn();
	
	@Required @Size(EchFormats.baseName)
	public String firstName, officialName;
	
	@Required 
	public Sex sex;
	
	@Required 
	public final DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();
	
	//
	
	public PersonIdentification() {
	}
	
	public boolean isEmpty() {
		return StringUtils.isBlank(firstName) && StringUtils.isBlank(officialName);
	}
	
	public String display() {
		return toHtml();
	}
	
	@Deprecated
	public String toHtml() {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString();
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, firstName, officialName);
		StringUtils.appendLine(s, dateOfBirth.toString());
	}
	
	@Override
	public String render(RenderType renderType, Locale locale) {
		return toHtml();
	}

	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.HMTL;
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
