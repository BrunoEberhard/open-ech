package ch.ech.ech0044;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade : die NotEmpty auf officialName, firstName, sex entfernt, damit die Entity auch als "Light" Version
// verwendet werden kann.

// personIdentificationPartner gab es nur 
// - bis Version 2 von ech0044
// - bis Version 5 von ech0011
// - bis Version 4 von ech0021

public class PersonIdentification {
	public static final PersonIdentification $ = Keys.of(PersonIdentification.class);

	public Object id;
	@Size(13)
	public Long vn;
	// bei "Light" nicht zwingend, sonst schon
	public final ch.openech.xml.NamedId localPersonId = new ch.openech.xml.NamedId();
	public List<ch.openech.xml.NamedId> otherPersonId;
	// bei "Light" nicht vorhanden
	public List<ch.openech.xml.NamedId> euPersonId;
	@Size(100)
	@NotEmpty
	public String officialName, firstName;
	@Size(100)
	public String originalName;
	// bei "Light" nicht zwingend, sonst schon
	public Sex sex;
	public final ch.openech.xml.DatePartiallyKnown dateOfBirth = new ch.openech.xml.DatePartiallyKnown();
}