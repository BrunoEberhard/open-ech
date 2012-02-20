package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e11.OriginPanel;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class NaturalizeSwissEvent extends PersonEventEditor<PlaceOfOrigin> {
	
	public NaturalizeSwissEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	@Override
	public FormVisual<PlaceOfOrigin> createForm() {
		return new OriginPanel(true, false); // true -> withReasonAndDate, false -> no expirationDate
	}

	@Override
	protected void fillForm(AbstractFormVisual<PlaceOfOrigin> formPanel) {
		// not used
	}

	@Override
	public PlaceOfOrigin load() {
		PlaceOfOrigin placeOfOrigin = new PlaceOfOrigin();
		placeOfOrigin.reasonOfAcquisition = "3"; // Auf Einbürgerung setzen
		return placeOfOrigin;
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.naturalizeSwiss(person.personIdentification, placeOfOrigin));
	}

	@Override
	public void validate(PlaceOfOrigin object, List<ValidationMessage> resultList) {
		Person.validateEventNotBeforeBirth(resultList, getPerson().personIdentification, object.naturalizationDate, PlaceOfOrigin.PLACE_OF_ORIGIN.naturalizationDate);
		if (object.naturalizationDate == null) {
			resultList.add(new ValidationMessage(PlaceOfOrigin.PLACE_OF_ORIGIN.naturalizationDate, "Erwerbsdatum muss gesetzt sein"));
		}
	}

}
