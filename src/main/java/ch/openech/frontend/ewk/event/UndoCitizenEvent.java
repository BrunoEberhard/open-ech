package ch.openech.frontend.ewk.event;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.edit.fields.AbstractEditField;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.Input;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.BusinessRule;

import ch.openech.frontend.ewk.event.UndoCitizenEvent.UndoCitizenData;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class UndoCitizenEvent extends PersonEventEditor<UndoCitizenData> {
	private final Person person;

	public UndoCitizenEvent(EchSchema ech, Person person) {
		super(ech, person);
		this.person = person;
	}
	
	@Override
	protected void fillForm(Form<UndoCitizenData> formPanel) {
	    formPanel.line(new UndoCitizenField(UndoCitizenData.$.placeOfOrigin));
	    formPanel.line(UndoCitizenData.$.expatriationDate);
	}

	@Override
	protected List<String> getXml(Person person, UndoCitizenData data, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoCitizen(person.personIdentification(), data.placeOfOrigin, data.expatriationDate));
	}
	
	private class UndoCitizenField extends AbstractEditField<PlaceOfOrigin> {
		private final Input<PlaceOfOrigin> comboBox;
		
		public UndoCitizenField(PropertyInterface property) {
			super(property, true);
			comboBox = ClientToolkit.getToolkit().createComboBox(person.placeOfOrigin, listener());
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
			return comboBox.getValue();
		}

		@Override
		public void setObject(PlaceOfOrigin object) {
			comboBox.setValue(object);
		}
	}
	
	public static class UndoCitizenData implements Validation {
		public static final UndoCitizenData $ = Keys.of(UndoCitizenData.class);
		
		public Person person;
		@Required
		public PlaceOfOrigin placeOfOrigin;
		@Required
		public LocalDate expatriationDate;
		
		@Override
		public void validate(List<ValidationMessage> resultList) {
			Person.validateEventNotBeforeBirth(resultList, person, expatriationDate, PlaceOfOrigin.$.expatriationDate);
			validateExpatriationNotBeforeNaturalization(placeOfOrigin, resultList);
		}
		
		@BusinessRule("Datum Bürgerrechtsentlassung kann nicht vor dem Datum der Einbürgerung sein")
		private static void validateExpatriationNotBeforeNaturalization(PlaceOfOrigin placeOfOrigin, List<ValidationMessage> validationMessages) {
			if (placeOfOrigin == null || placeOfOrigin.naturalizationDate == null) return;
			LocalDate date = placeOfOrigin.expatriationDate;
			if (date == null) return;
			if (date.compareTo(placeOfOrigin.naturalizationDate)  < 0) {
				validationMessages.add(new ValidationMessage(PlaceOfOrigin.$.expatriationDate, "Kann nicht vor Datum der Einbürgerung sein"));
			}
		}
	}

}
