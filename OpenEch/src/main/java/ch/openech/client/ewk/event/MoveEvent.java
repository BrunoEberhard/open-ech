package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e07.MunicipalityField;
import ch.openech.client.e10.AddressField;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.model.Keys;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public class MoveEvent extends PersonEventEditor<MoveEvent.MoveEventData> {
	
	public MoveEvent(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}

	public static class MoveEventData {
		public final DwellingAddress dwellingAddress = new DwellingAddress();
		public MunicipalityIdentification reportingMunicipality;
	}

	private static final MoveEventData MED = Keys.of(MoveEventData.class);
	
	@Override
	protected void fillForm(Form<MoveEventData> formPanel) {
		formPanel.line(MED.dwellingAddress.EGID);
		formPanel.line(MED.dwellingAddress.EWID);
		formPanel.line(MED.dwellingAddress.householdID);
		formPanel.line(new AddressField(MED.dwellingAddress.mailAddress, true, false, false));
		formPanel.line(MED.dwellingAddress.typeOfHousehold);
		formPanel.line(new MunicipalityField(MED.reportingMunicipality, true));
	}

	@Override
	public MoveEventData load() {
		MoveEventData data = new MoveEventData();
		if (getPerson().dwellingAddress != null) {
			DwellingAddress dwellingAddress = getPerson().dwellingAddress;
			data.dwellingAddress.EGID = dwellingAddress.EGID;
			data.dwellingAddress.EWID = dwellingAddress.EWID;
			data.dwellingAddress.householdID = dwellingAddress.householdID;
			data.dwellingAddress.mailAddress = dwellingAddress.mailAddress;
			data.dwellingAddress.typeOfHousehold = dwellingAddress.typeOfHousehold;
			data.reportingMunicipality = getPerson().residence.reportingMunicipality;
			
			data.dwellingAddress.echSchema = echSchema;
		}
		data.dwellingAddress.movingDate = null;
		return data;
	}

	@Override
	protected List<String> getXml(Person person, MoveEventData med, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.move(person.personIdentification, med.reportingMunicipality, med.dwellingAddress));
	}
	
//	@Override
//	public void validate(List<ValidationMessage> resultList) {
//		super.validate(resultList);
//		getAddress().validate(resultList);
//	}


}
