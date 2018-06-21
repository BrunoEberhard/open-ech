package ch.ech.ech0021;

import java.util.Set;
import java.util.TreeSet;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class Relationship {
	public static final Relationship $ = Keys.of(Relationship.class);

	@NotEmpty
	public TypeOfRelationship typeOfRelationship;
	public final Set<BasedOnLaw> basedOnLaw = new TreeSet<>(); // in version 3 noch ein Set in 4 nicht mehr
	@Size(100)
	public String basedOnLawAddOn; // in 4 nicht mehr enthalten
	public ch.openech.xml.YesNo care;
	public static class Partner {

		public ch.ech.ech0044.PersonIdentification personIdentification;
		public ch.ech.ech0044.PersonIdentificationPartner personIdentificationPartner;
		public ch.ech.ech0011.PartnerIdOrganisation partnerIdOrganisation;
		public ch.ech.ech0010.MailAddress address;
	}
	public final Partner partner = new Partner();
}