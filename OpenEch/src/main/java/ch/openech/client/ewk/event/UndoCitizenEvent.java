package ch.openech.client.ewk.event;

import static ch.openech.dm.person.PlaceOfOrigin.PLACE_OF_ORIGIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;


public class UndoCitizenEvent extends PersonEventEditor<PlaceOfOrigin> {
	private UndoCitizenField originField;

	public UndoCitizenEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}
	
	@Override
	protected void fillForm(AbstractFormVisual<PlaceOfOrigin> formPanel) {
		originField = new UndoCitizenField();
	    formPanel.line(originField);
	    formPanel.line(new DateField(PLACE_OF_ORIGIN.expatriationDate, DateField.REQUIRED));
	}

	@Override
	public PlaceOfOrigin load() {
		originField.setValues(getPerson().placeOfOrigin);
		return new PlaceOfOrigin();
	}

	@Override
	protected List<String> getXml(Person person, PlaceOfOrigin placeOfOrigin, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoCitizen(person.personIdentification, originField.getObject(), placeOfOrigin.expatriationDate));
	}
	
	@Override
	public void validate(PlaceOfOrigin placeOfOrigin, List<ValidationMessage> resultList) {
		Person.validateEventNotBeforeBirth(resultList, getPerson().personIdentification, placeOfOrigin.expatriationDate, PLACE_OF_ORIGIN.expatriationDate);
		validateExpatriationNotBeforeNaturalization(placeOfOrigin, resultList);
	}

	@BusinessRule("Datum Bürgerrechtsentlassung kann nicht vor dem Datum der Einbürgerung sein")
	private void validateExpatriationNotBeforeNaturalization(PlaceOfOrigin placeOfOrigin, List<ValidationMessage> validationMessages) {
		if (placeOfOrigin == null || StringUtils.isBlank(placeOfOrigin.naturalizationDate)) return;
		String date = placeOfOrigin.expatriationDate;
		if (StringUtils.isBlank(date)) return;
		if (date.compareTo(placeOfOrigin.naturalizationDate)  < 0) {
			validationMessages.add(new ValidationMessage(PLACE_OF_ORIGIN.expatriationDate, "Kann nicht vor Datum der Einbürgerung sein"));
		}
	}


	private class UndoCitizenField extends AbstractEditField<PlaceOfOrigin> {
		private final ComboBox comboBox;
		
		public UndoCitizenField() {
			super(PLACE_OF_ORIGIN.originName);
			comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		}

		@Override
		public IComponent getComponent0() {
			return comboBox;
		}

		@Override
		public PlaceOfOrigin getObject() {
			return (PlaceOfOrigin)comboBox.getSelectedObject();
		}

		@Override
		public void setObject(PlaceOfOrigin object) {
			comboBox.setSelectedObject(object);
		}
		
		@Override
		public boolean isEmpty() {
			return getObject() == null;
		}

		public void setValues(List<PlaceOfOrigin> placeOfOrigin) {
			List<PlaceOfOrigin> placeOfOrigins = new ArrayList<PlaceOfOrigin>();
			for (PlaceOfOrigin origin : placeOfOrigin) {
				if (StringUtils.isBlank(origin.expatriationDate)) {
					placeOfOrigins.add(origin);
				}
			}
			comboBox.setObjects(placeOfOrigins);
		}
	}

}
