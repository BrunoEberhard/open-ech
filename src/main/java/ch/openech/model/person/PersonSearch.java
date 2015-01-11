package  ch.openech.model.person;

import java.util.Locale;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

import ch.openech.model.EchFormats;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Sex;

public class PersonSearch implements View<Person>, Rendering {

	public static final PersonSearch $ = Keys.of(PersonSearch.class);
	
	//
	
	public Object id;
	
	public final Vn vn = new Vn();
	
	@Required @Size(EchFormats.baseName)
	public String firstName, officialName;
	
	@Required 
	public Sex sex;
	
	@Required 
	public final DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();

	public DwellingAddress dwellingAddress;

	public PersonSearch() {
	}

	@Override
	public String render(RenderType renderType, Locale locale) {
		return firstName + " " + officialName;
	}

	public String getStreet() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "street", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.street;
		} else {
			return null;
		}
	}

	public String getStreetNumber() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "streetNumber", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.houseNumber.houseNumber;
		} else {
			return null;
		}
	}

	public String getTown() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "town", String.class);
		if (dwellingAddress != null && dwellingAddress.mailAddress != null) {
			return dwellingAddress.mailAddress.town;
		} else {
			return null;
		}
	}
	
}