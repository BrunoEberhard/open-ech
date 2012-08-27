package ch.openech.client.ewk.event;

import static ch.openech.dm.person.PlaceOfOrigin.PLACE_OF_ORIGIN;
import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.UndoCitizenEvent.UndoCitizenData;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PlaceOfOrigin;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.edit.fields.AbstractEditField;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.BusinessRule;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;


public class UndoCitizenEvent extends PersonEventEditor<UndoCitizenData> {
	private UndoCitizenField originField;

	public UndoCitizenEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
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
		originField.setValues(getPerson().placeOfOrigin);
		return data;
	}

	@Override
	protected List<String> getXml(Person person, UndoCitizenData data, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.undoCitizen(person.personIdentification, data.placeOfOrigin, data.expatriationDate));
	}
	
	@Override
	public void validate(UndoCitizenData data, List<ValidationMessage> resultList) {
		Person.validateEventNotBeforeBirth(resultList, getPerson().personIdentification, data.expatriationDate, PLACE_OF_ORIGIN.expatriationDate);
		validateExpatriationNotBeforeNaturalization(data.placeOfOrigin, resultList);
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
		private final ComboBox<PlaceOfOrigin> comboBox;
		
		public UndoCitizenField(Object key) {
			super(Constants.getConstant(key), true);
			comboBox = ClientToolkit.getToolkit().createComboBox(listener());
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
	
	public static class UndoCitizenData {
		public static final UndoCitizenData UNDO_CITIZEN_DATA = Constants.of(UndoCitizenData.class);
		
		@Required
		public PlaceOfOrigin placeOfOrigin;
		@Required @Is(Date)
		public String expatriationDate;
	}

}
