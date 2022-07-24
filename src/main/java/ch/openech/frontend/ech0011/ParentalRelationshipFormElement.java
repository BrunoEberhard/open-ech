package ch.openech.frontend.ech0011;

import java.util.Arrays;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.EnumFormElement;
import org.minimalj.frontend.form.element.ListFormElement;

import ch.ech.ech0021.Care;
import ch.ech.ech0021.ParentalRelationship;
import ch.ech.ech0021.TypeOfRelationship;

public class ParentalRelationshipFormElement extends ListFormElement<ParentalRelationship> {
	private static final List<TypeOfRelationship> TYPES = Arrays.asList(TypeOfRelationship._3, TypeOfRelationship._4, TypeOfRelationship._5, TypeOfRelationship._6);

	public ParentalRelationshipFormElement(List<ParentalRelationship> key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<ParentalRelationship> createForm(boolean newObject) {
		Form<ParentalRelationship> form = new Form<>(2);
		form.line(new EnumFormElement<TypeOfRelationship>(ParentalRelationship.$.typeOfRelationship, TYPES));
		form.line(new IdentificationFormElement(ParentalRelationship.$.partner.identification, false));
		form.line(new MailAddressFormElement(ParentalRelationship.$.partner.address, true));
		form.line(ParentalRelationship.$.relationshipValidFrom);
		form.line(ParentalRelationship.$.care);
		return form;
	}

	@Override
	protected ParentalRelationship createEntry() {
		ParentalRelationship relationship = new ParentalRelationship();
		relationship.care = Care._1;
		relationship.typeOfRelationship = TypeOfRelationship._3;
		return relationship;
	}

}
