package  ch.openech.model.person;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;

import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.DwellingAddress;
import ch.openech.model.person.types.Vn;
import ch.openech.model.types.Sex;

public class PersonSearch implements View<Person>, Rendering {

	public static final PersonSearch $ = Keys.of(PersonSearch.class);
	
	//
	
	public Object id;
	
	public final Vn vn = new Vn();
	
	public String firstName, officialName;
	
	public Sex sex;
	
	public final DatePartiallyKnown dateOfBirth = new DatePartiallyKnown();

	public DwellingAddress dwellingAddress;

	public PersonSearch() {
	}

	@Override
	public String render(RenderType renderType) {
		return firstName + " " + officialName;
	}
	
	//
	
	public String streetAndHouseNumber;

	public String town;
	
}