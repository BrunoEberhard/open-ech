package ch.openech.client.ewk.event;

import static ch.openech.dm.person.Person.PERSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.event.RenewPermitEvent.RenewPermitActionData;
import ch.openech.dm.person.Foreign;
import ch.openech.dm.person.Occupation;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class RenewPermitEvent extends PersonEventEditor<RenewPermitActionData> {
	
	public RenewPermitEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	public static class RenewPermitActionData extends Foreign {
		public final List<Occupation> occupation = new ArrayList<Occupation>();
	}

	@Override
	protected void fillForm(AbstractFormVisual<RenewPermitActionData> formPanel) {
		formPanel.line(PERSON.foreign.residencePermit);
		if (getEchNamespaceContext().renewPermitHasTill()) {
			// bei Change ist diese Angabe obligatorisch, bei Correct und Renew nicht
			formPanel.line(PERSON.foreign.residencePermitTill);
		}
		formPanel.area(PERSON.occupation);
	}

	@Override
	public RenewPermitActionData load() {
		return new RenewPermitActionData();
	}

	@Override
	protected List<String> getXml(Person person, RenewPermitActionData data, WriterEch0020 writerEch0020) throws Exception {
        return Collections.singletonList(writerEch0020.renewPermit(person.personIdentification, data, data.occupation));
	}
	
	@Override
	public void validate(RenewPermitActionData data, List<ValidationMessage> resultList) {
		if (data.occupation.isEmpty()) {
			resultList.add(new ValidationMessage("occupation", "Beruf erforderlich"));
		}
	}

}
