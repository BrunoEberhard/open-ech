package ch.ech.ech0020.v3;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0020.v3.EventChildRelationship.Parent;

//handmade
public class EventAdoption {
	public static final EventAdoption $ = Keys.of(EventAdoption.class);

	public Object id;
	@NotEmpty
	public InfostarPerson adoptionPerson;
	public List<Parent> addParent;
	public List<Parent> removeParent;
	public LocalDate adoptionValidFrom;
}