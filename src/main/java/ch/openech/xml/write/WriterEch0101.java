package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import org.minimalj.util.StringUtils;

import ch.openech.model.person.PersonExtendedInformation;
import ch.openech.model.types.YesNo;

public class WriterEch0101 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0010 ech10;
	public final WriterEch0044 ech44;

	public WriterEch0101(EchSchema context) {
		super(context);

		URI = context.getNamespaceURI(getSchemaNumber());
		ech10 = new WriterEch0010(context);
		ech44 = new WriterEch0044(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 101;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}

	@Override
	protected String getRootType() {
		return PERSON_EXTENDED_INFORMATION_ROOT;
	}
	
	public String informationRoot(PersonExtendedInformation information) throws Exception {
		WriterElement informationRoot = delivery();
		information(informationRoot, information);
		return result();
	}
	
	public void information(WriterElement parent, PersonExtendedInformation information) throws Exception {
		personExtendedInformation(parent, PERSON_ADDON, information);
	}
	
	public void personExtendedInformation(WriterElement parent, String tagName, PersonExtendedInformation information) throws Exception {
		if (information == null) return;
		WriterElement writer = parent.create(URI, tagName);
		writer.writeAttribute(null, "schemaVersion", "1.0"); // whats that for??
		
		// WTF: Schreibfehler in Spez: personidentification statt personIdentification
		ech44.personIdentification(writer, PERSONIDENTIFICATION, information.personIdentification);
		armedForces(writer, information);
		writer.values(information, CIVIL_DEFENSE);
		fireService(writer, information);
		healthInsurance(writer, information);
		writer.values(information, MATRIMONIAL_INHERITANCE_ARRANGEMENT);
	}

	private void armedForces(WriterElement parent, PersonExtendedInformation information) throws Exception {
		WriterElement writer = parent.create(URI, ARMED_FORCES);
		writer.values(information, ARMED_FORCES_SERVICE, ARMED_FORCES_LIABILITY);
	}
	
	private void fireService(WriterElement parent, PersonExtendedInformation information) throws Exception {
		WriterElement writer = parent.create(URI, FIRE_SERVICE);
		writer.values(information, FIRE_SERVICE, FIRE_SERVICE_LIABILITY);
	}
	
	private void healthInsurance(WriterElement parent, PersonExtendedInformation information) throws Exception {
		WriterElement writer = parent.create(URI, HEALTH_INSURANCE);
		if (information.healthInsured != null) {
			writer.values(information, HEALTH_INSURED);
		} else {
			writer.text(HEALTH_INSURED, YesNo.Yes); // really yes as default?
		}
		insurance(writer, information);
	}

	private void insurance(WriterElement parent, PersonExtendedInformation information) throws Exception {
		boolean hasInsuranceName = !StringUtils.isBlank(information.insuranceName);
		boolean hasInsuranceAddress = information.insuranceAddress != null && !information.insuranceAddress.isEmpty();
		if (!hasInsuranceName && !hasInsuranceAddress) return;
		
		WriterElement writer = parent.create(URI, INSURANCE);
		if (hasInsuranceAddress) {
			if (StringUtils.isBlank(information.insuranceAddress.organisationName)) {
				information.insuranceAddress.organisationName = information.insuranceName;
			}
			ech10.address(writer, INSURANCE_ADDRESS, information.insuranceAddress);
		} else  if (hasInsuranceName) {
			writer.values(information, INSURANCE_NAME);
		}
	}
}
