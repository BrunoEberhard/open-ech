package ch.openech.frontend.ewk.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.CloneHelper;

import ch.openech.frontend.ewk.event.UndoCitizenEvent.UndoCitizenData;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.model.person.PlaceOfOrigin;
import ch.openech.util.BusinessRule;
import ch.openech.xml.write.WriterEch0020;


public class UndoCitizenEvent extends PersonEventEditor<UndoCitizenData> {

	private List<PlaceOfOrigin> existingPlaceOfOrigin;
	
	public UndoCitizenEvent(PersonPage personPage) {
		super(personPage);
	}
	
	@Override
	protected UndoCitizenData createObject() {
		return new UndoCitizenData();
	}
	
	@Override
	protected void fillForm(Form<UndoCitizenData> formPanel) {
	    formPanel.line(new UndoCitizenField(UndoCitizenData.$.placeOfOrigin));
	    formPanel.line(UndoCitizenData.$.expatriationDate);
	}
	
	@Override
	public Person getPerson() {
		Person person = super.getPerson();
		this.existingPlaceOfOrigin = CloneHelper.clone(person.placeOfOrigin);
		return person;
	}
	
	@Override
	protected List<String> getXml(Person person, UndoCitizenData data, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoCitizen(person.personIdentification(), data.placeOfOrigin, data.expatriationDate));
	}
	
	private class UndoCitizenField extends AbstractFormElement<PlaceOfOrigin> {
		private final Input<PlaceOfOrigin> comboBox;
		
		public UndoCitizenField(PropertyInterface property) {
			super(property);
			comboBox = Frontend.getInstance().createComboBox(existingPlaceOfOrigin, listener());
		}

		public UndoCitizenField(PlaceOfOrigin key) {
			this(Keys.getProperty(key));
		}

		@Override
		public IComponent getComponent() {
			return comboBox;
		}

		@Override
		public PlaceOfOrigin getValue() {
			return comboBox.getValue();
		}

		@Override
		public void setValue(PlaceOfOrigin object) {
			comboBox.setValue(object);
		}
	}
	
	public static class UndoCitizenData implements Validation {
		public static final UndoCitizenData $ = Keys.of(UndoCitizenData.class);
		
		public Person person;
		@NotEmpty
		public PlaceOfOrigin placeOfOrigin;
		@NotEmpty
		public LocalDate expatriationDate;
		
		@Override
		public List<ValidationMessage> validate() {
			List<ValidationMessage> resultList = new ArrayList<>();
			Person.validateEventNotBeforeBirth(resultList, person, expatriationDate, PlaceOfOrigin.$.expatriationDate);
			validateExpatriationNotBeforeNaturalization(placeOfOrigin, resultList);
			return resultList;
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
