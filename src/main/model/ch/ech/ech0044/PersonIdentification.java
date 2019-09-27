package ch.ech.ech0044;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.StringUtils;

// handmade : die NotEmpty auf officialName, firstName, sex entfernt, damit die Entity auch als "Light" Version
// verwendet werden kann.

// personIdentificationPartner gab es nur 
// - bis Version 2 von ech0044
// - bis Version 5 von ech0011
// - bis Version 4 von ech0021

public class PersonIdentification implements Rendering {
	public static final PersonIdentification $ = Keys.of(PersonIdentification.class);

	public Object id;
	@Size(13)
	public Long vn;
	// bei "Light" nicht zwingend, sonst schon
	public final ch.openech.model.NamedId localPersonId = new ch.openech.model.NamedId();
	public List<ch.openech.model.NamedId> otherPersonId;
	// bei "Light" nicht vorhanden
	public List<ch.openech.model.NamedId> euPersonId;
	@Size(100)
	@NotEmpty
	public String officialName, firstName;
	@Size(100)
	public String originalName;
	// bei "Light" nicht zwingend, sonst schon
	public Sex sex;
	public final ch.openech.model.DatePartiallyKnown dateOfBirth = new ch.openech.model.DatePartiallyKnown();

	public boolean isLight() {
		return EmptyObjects.isEmpty(localPersonId) || sex == null || EmptyObjects.isEmpty(dateOfBirth);
	}

	@Override
	public CharSequence render() {
		StringBuilder s = new StringBuilder();
		boolean empty = true;
		if (!StringUtils.isEmpty(firstName)) {
			s.append(firstName);
			empty = false;
		}
		if (!StringUtils.isEmpty(officialName)) {
			if (!empty) {
				s.append(' ');
			}
			s.append(officialName);
			empty = false;
		}
		if (!EmptyObjects.isEmpty(dateOfBirth)) {
			if (!empty) {
				s.append(' ');
			}
			s.append('(').append(dateOfBirth.toString()).append(')');
		}
		return s;
	}
}