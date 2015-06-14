package ch.openech.frontend.ewk.event;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.form.Form;
import org.minimalj.model.validation.ValidationMessage;

import ch.openech.frontend.e11.OriginPanel;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.model.person.types.ReasonOfAcquisition;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class NaturalizeSwissEvent extends PersonEventEditor<PlaceOfOrigin> {
	
	public NaturalizeSwissEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	@Override
	public Form<PlaceOfOrigin> createForm() {
		return new OriginPanel(true, false); // true -> withReasonAndDate, false -> no expirationDate
	}

	@Override
	protected void fillForm(Form<PlaceOfOrigin> formPanel) {
		// not used
	}

	@Override
	public PlaceOfOrigin createObject() {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		placeOfOrigin.reasonOfAcquisition = ReasonOfAcquisition.Einbuergerung;
		return placeOfOrigin;
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.naturalizeSwiss(person.personIdentification(), placeOfOrigin));
	}

	@Override
	public void validate(PlaceOfOrigin object, List<ValidationMessage> resultList) {
		super.validate(object, resultList);
		Person.validateEventNotBeforeBirth(resultList, getPerson(), object.naturalizationDate, PlaceOfOrigin.$.naturalizationDate);
		if (object.naturalizationDate == null) {
			resultList.add(new ValidationMessage(PlaceOfOrigin.$.naturalizationDate, "Erwerbsdatum erforderlich"));
		}
	}

}
