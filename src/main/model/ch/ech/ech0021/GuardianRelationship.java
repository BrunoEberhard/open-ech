package ch.ech.ech0021;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

// handmade
public class GuardianRelationship implements Rendering {
	public static final GuardianRelationship $ = Keys.of(GuardianRelationship.class);

	@NotEmpty
	@Size(36)
	public String guardianRelationshipId;

	public final RelationshipPartner partner = new RelationshipPartner();

	@NotEmpty
	public TypeOfRelationship typeOfRelationship;
	public final GuardianMeasureInfo guardianMeasureInfo = new GuardianMeasureInfo();
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