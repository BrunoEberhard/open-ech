package ch.openech.client.ewk.event;

import java.util.Collections;
import java.util.List;

import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.client.ewk.event.MoveOutEvent.MoveOutData;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.dm.common.Place;
import ch.openech.dm.person.Person;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.fields.DateField;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0093;

public class MoveOutEvent extends PersonEventEditor<MoveOutData> {

	public MoveOutEvent(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
	}

	public static class MoveOutData {
		public String departureDate;
		public MunicipalityIdentification reportingMunicipality;
		public Place goesTo;
	}

	private static final MoveOutData MOD = Constants.of(MoveOutData.class);
	
	@Override
	protected List<String> getXml(Person person, MoveOutData data, WriterEch0020 writerEch0020) throws Exception {
		return Collections.singletonList(writerEch0020.moveOut(person.personIdentification, data.reportingMunicipality, data.goesTo, data.departureDate));
	}

	@Override
	public MoveOutData load() {
		return new MoveOutData();
	}

	@Override
	protected void fillForm(AbstractFormVisual<MoveOutData> formPanel) {
		formPanel.line(new DateField(MOD.departureDate, DateField.REQUIRED));
		formPanel.line(new SwissMunicipalityField(MOD.reportingMunicipality, true));
		formPanel.line(MOD.goesTo);
	}
	
	@Override
	public void generateSedexOutput(MoveOutData data) throws Exception {
		WriterEch0093 sedexWriter = new WriterEch0093(getEchNamespaceContext());
		sedexWriter.setRecepientMunicipality(data.reportingMunicipality);
		String sedexOutput = sedexWriter.moveOut(getPerson(), data.reportingMunicipality, data.goesTo, data.departureDate);
		SedexOutputGenerator.generateSedex(sedexOutput, sedexWriter.getEnvelope());
	}

}