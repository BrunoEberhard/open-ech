package ch.openech.frontend.ech0011;

import java.util.Arrays;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.EnumFormElement;
import org.minimalj.frontend.form.element.ListFormElement;

import ch.ech.ech0021.GuardianRelationship;
import ch.ech.ech0021.TypeOfRelationship;

public class GuardianRelationshipFormElement extends ListFormElement<GuardianRelationship> {
	private static final List<TypeOfRelationship> TYPES = Arrays.asList(TypeOfRelationship._7, TypeOfRelationship._8, TypeOfRelationship._9, TypeOfRelationship._10);

	public GuardianRelationshipFormElement(List<GuardianRelationship> key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<GuardianRelationship> createForm() {
		Form<GuardianRelationship> form = new Form<>(2);
		form.line(new EnumFormElement<TypeOfRelationship>(GuardianRelationship.$.typeOfRelationship, TYPES));
		form.line(new IdentificationFormElement(GuardianRelationship.$.partner.identification, false));
		form.line(new MailAddressFormElement(GuardianRelationship.$.partner.address, true));
		form.line(GuardianRelationship.$.guardianMeasureInfo.basedOnLawAddOn);
		form.line(GuardianRelationship.$.guardianMeasureInfo.guardianMeasureValidFrom);
		form.line(GuardianRelationship.$.care);

		return form;
	}

}
