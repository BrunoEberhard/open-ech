package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.e10.AddressField;
import ch.openech.dm.common.DwellingAddress;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.person.Person;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class MoveEvent extends PersonEventEditor<MoveEvent.MoveEventData> {
	
	public MoveEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	public static class MoveEventData extends DwellingAddress {
		private MunicipalityIdentification reportingMunicipality;
	}

	private static final MoveEventData MED = Constants.of(MoveEventData.class);
	
	@Override
	protected void fillForm(AbstractFormVisual<MoveEventData> formPanel) {
		formPanel.line(MED.EGID);
		formPanel.line(MED.EWID);
		formPanel.line(MED.householdID);
		formPanel.area(new AddressField(MED.mailAddress, true, false, false));
		formPanel.line(MED.typeOfHousehold);
		formPanel.area(new SwissMunicipalityField(MED.reportingMunicipality, true));
	}

	@Override
	public MoveEventData load() {
		MoveEventData data = new MoveEventData();
		if (getPerson().dwellingAddress != null) {
			DwellingAddress dwellingAddress = getPerson().dwellingAddress;
			data.EGID = dwellingAddress.EGID;
			data.EWID = dwellingAddress.EWID;
			data.householdID = dwellingAddress.householdID;
			data.mailAddress = dwellingAddress.mailAddress;
			data.typeOfHousehold = dwellingAddress.typeOfHousehold;
		}
		data.movingDate = null;
		return data;
	}

	@Override
	public void validate(MoveEventData data, List<ValidationMessage> resultList) {
	}

	@Override
	protected List<String> getXml(Person person, MoveEventData address, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.move(person.personIdentification, address.reportingMunicipality, address));
	}
	
//	@Override
//	public void validate(List<ValidationMessage> resultList) {
//		super.validate(resultList);
//		getAddress().validate(resultList);
//	}


}