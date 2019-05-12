package ch.ech.ech0021;

import java.time.LocalDate;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;

// handmade
public class ParentalRelationship implements Rendering {
	public static final ParentalRelationship $ = Keys.of(ParentalRelationship.class);

	public final RelationshipPartner partner = new RelationshipPartner();
	public LocalDate relationshipValidFrom;
	@NotEmpty
	public TypeOfRelationship typeOfRelationship;
	@NotEmpty
	public Care care;

	@Override
	public CharSequence render() {
		String identification = partner.identification.render().toString();
		if (identification != null && typeOfRelationship != null) {
			identification += " (" + EnumUtils.getText(typeOfRelationship) + ")";
		}
		return identification;
	}
}