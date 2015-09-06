package ch.openech.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.NamedId;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Sex;

public class PersonIdentificationLight implements Rendering {

	public static final PersonIdentificationLight $ = Keys.of(PersonIdentificationLight.class);
	
	public Object id;
	
	public final List<NamedId> otherId = new ArrayList<NamedId>();
	
	public final Vn vn = new Vn();
	
	@NotEmpty @Size(EchFormats.baseName)
	public String firstName, officialName;
	
	public Sex sex;
	
	public final DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();
	
	public boolean isEmpty() {
		return StringUtils.isBlank(officialName);
	}
	
	public void toHtml(StringBuilder s) {
		StringUtils.appendLine(s, firstName, officialName);
		StringUtils.appendLine(s, dateOfBirth.toString());
	}
	
	@Override
	public String render(RenderType renderType) {
		StringBuilder s = new StringBuilder();
		toHtml(s);
		return s.toString().replace("<br>", "\n");
	}

	@Override
	public RenderType getPreferredRenderType(RenderType firstType, RenderType... otherTypes) {
		return RenderType.PLAIN_TEXT;
	}
	
}
