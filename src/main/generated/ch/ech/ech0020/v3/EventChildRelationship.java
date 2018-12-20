package ch.ech.ech0020.v3;

import java.time.LocalDate;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

import ch.ech.ech0021.TypeOfRelationship;

//handmade
public class EventChildRelationship {
	public static final EventChildRelationship $ = Keys.of(EventChildRelationship.class);

	public Object id;
	@NotEmpty
	public InfostarPerson childRelationshipPerson;
	public static class Parent {

		public static class Partner {

			public ch.ech.ech0044.PersonIdentification personIdentification;
			public ch.ech.ech0044.PersonIdentification personIdentificationPartner; // Light
			public ch.ech.ech0010.PersonMailAddress address;
		}
		public final Partner partner = new Partner();
		public LocalDate relationshipValidFrom;

		@NotEmpty
		public TypeOfRelationship typeOfRelationship;
		@NotEmpty
		public ch.ech.ech0021.Care care;
		public ch.ech.ech0021.NameOfParent nameOfParentAtEvent;
	}
	public List<Parent> addParent;
	public List<Parent> removeParent;
	public LocalDate childRelationshipValidFrom;
}