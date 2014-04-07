package ch.openech.client.ewk.event;

import static ch.openech.dm.person.PlaceOfOrigin.*;

import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.client.ewk.event.UndoCitizenEvent.UndoCitizenData;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.Validation;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.annotation.Required;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.BusinessRule;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class UndoCitizenEvent extends PersonEventEditor<UndoCitizenData> {
	private UndoCitizenField originField;

	public UndoCitizenEvent(EchSchema ech, Person person) {
		super(ech, person);
	}
	
	@Override
	protected void fillForm(Form<UndoCitizenData> formPanel) {
		originField = new UndoCitizenField(UndoCitizenData.UNDO_CITIZEN_DATA.placeOfOrigin);
	    formPanel.line(originField);
	    formPanel.line(UndoCitizenData.UNDO_CITIZEN_DATA.expatriationDate);
	}

	@Override
	public UndoCitizenData load() {
		UndoCitizenData data = new UndoCitizenData();
		data.person = getPerson();
		originField.setValues(getPerson().placeOfOrigin);
		return data;
	}

	@Override
	protected List<String> getXml(Person person, UndoCitizenData data, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoCitizen(person.personIdentification(), data.placeOfOrigin, data.expatriationDate));
	}
	
	private class UndoCitizenField extends AbstractEditField<PlaceOfOrigin> {
		private final ComboBox<PlaceOfOrigin> comboBox;
		
		public UndoCitizenField(PropertyInterface property) {
			super(property, true);
			comboBox = ClientToolkit.getToolkit().createComboBox(listener());
		}

		public UndoCitizenField(PlaceOfOrigin key) {
			this(Keys.getProperty(key));
		}

		@Override
		public IComponent getComponent() {
			return comboBox;
		}

		@Override
		public PlaceOfOrigin getObject() {
			return comboBox.getSelectedObject();
		}

		@Override
		public void setObject(PlaceOfOrigin object) {
			comboBox.setSelectedObject(object);
		}
		
		public void setValues(List<PlaceOfOrigin> placeOfOrigin) {
			comboBox.setObjects(placeOfOrigin);
		}
	}
	
	public static class UndoCitizenData implements Validation {
		public static final UndoCitizenData UNDO_CITIZEN_DATA = Keys.of(UndoCitizenData.class);
		
		public Person person;
		@Required
		public PlaceOfOrigin placeOfOrigin;
		@Required
		public LocalDate expatriationDate;
		
		@Override
		public void validate(List<ValidationMessage> resultList) {
			Person.validateEventNotBeforeBirth(resultList, person, expatriationDate, PLACE_OF_ORIGIN.expatriationDate);
			validateExpatriationNotBeforeNaturalization(placeOfOrigin, resultList);
		}
		
		@BusinessRule("Datum Bürgerrechtsentlassung kann nicht vor dem Datum der Einbürgerung sein")
		private static void validateExpatriationNotBeforeNaturalization(PlaceOfOrigin placeOfOrigin, List<ValidationMessage> validationMessages) {
			if (placeOfOrigin == null || placeOfOrigin.naturalizationDate == null) return;
			LocalDate date = placeOfOrigin.expatriationDate;
			if (date == null) return;
			if (date.compareTo(placeOfOrigin.naturalizationDate)  < 0) {
				validationMessages.add(new ValidationMessage(PLACE_OF_ORIGIN.expatriationDate, "Kann nicht vor Datum der Einbürgerung sein"));
			}
		}
	}

}
