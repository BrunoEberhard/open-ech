package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;

import ch.openech.client.e07.MunicipalityField;
import ch.openech.client.ewk.event.MoveOutEvent.MoveOutData;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Required;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0093;

public class MoveOutEvent extends PersonEventEditor<MoveOutData> {

	public MoveOutEvent(EchSchema ech, Person person) {
		super(ech, person);
	}

	public static class MoveOutData {
		@Required
		public LocalDate departureDate;
		public MunicipalityIdentification reportingMunicipality;
		public Place goesTo;
	}

	private static final MoveOutData MOD = Keys.of(MoveOutData.class);
	
	@Override
	protected List<String> getXml(Person person, MoveOutData data, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.moveOut(person.personIdentification(), data.reportingMunicipality, data.goesTo, data.departureDate));
	}

	@Override
	public MoveOutData load() {
		return new MoveOutData();
	}

	@Override
	protected void fillForm(Form<MoveOutData> formPanel) {
		formPanel.line(MOD.departureDate);
		formPanel.line(new MunicipalityField(MOD.reportingMunicipality, true));
		formPanel.line(MOD.goesTo);
	}
	
	@Override
	public void generateSedexOutput(MoveOutData data) throws Exception {
		WriterEch0093 sedexWriter = new WriterEch0093(echSchema);
		sedexWriter.setRecepientMunicipality(data.reportingMunicipality);
		String sedexOutput = sedexWriter.moveOut(getPerson(), data.reportingMunicipality, data.goesTo, data.departureDate);
		SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
	}

}
