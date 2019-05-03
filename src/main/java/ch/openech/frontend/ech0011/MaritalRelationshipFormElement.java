package ch.openech.frontend.ech0011;

import java.util.Arrays;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.EnumFormElement;
import org.minimalj.frontend.form.element.FormLookupFormElement;

import ch.ech.ech0021.MaritalRelationship;
import ch.ech.ech0021.TypeOfRelationship;

public class MaritalRelationshipFormElement extends FormLookupFormElement<MaritalRelationship> {
	private static final List<TypeOfRelationship> TYPES = Arrays.asList(TypeOfRelationship._1, TypeOfRelationship._2);

	public MaritalRelationshipFormElement(MaritalRelationship key, boolean editable) {
		super(key, editable);
	}

	@Override
	public Form<MaritalRelationship> createForm() {
		Form<MaritalRelationship> form = new Form<>(2);
		form.line(new EnumFormElement<TypeOfRelationship>(MaritalRelationship.$.typeOfRelationship, TYPES));
		form.line(new IdentificationFormElement(MaritalRelationship.$.partner.identification, false));
		form.line(new MailAddressFormElement(MaritalRelationship.$.partner.address, true));
		return form;
	}

	@Override
	protected MaritalRelationship createObject() {
		MaritalRelationship object = super.createObject();
		if (object.typeOfRelationship == null) {
			object.typeOfRelationship = TypeOfRelationship._1;
		}
		return object;
	}

}
