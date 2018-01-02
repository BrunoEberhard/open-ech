package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.ADDRESS_INFORMATION;
import static ch.openech.model.XmlConstants.ADDRESS_LINE1;
import static ch.openech.model.XmlConstants.ADDRESS_LINE2;
import static ch.openech.model.XmlConstants.COUNTRY;
import static ch.openech.model.XmlConstants.DWELLING_NUMBER;
import static ch.openech.model.XmlConstants.FIRST_NAME;
import static ch.openech.model.XmlConstants.FOREIGN_ZIP_CODE;
import static ch.openech.model.XmlConstants.HOUSE_NUMBER;
import static ch.openech.model.XmlConstants.LAST_NAME;
import static ch.openech.model.XmlConstants.LOCALITY;
import static ch.openech.model.XmlConstants.MR_MRS;
import static ch.openech.model.XmlConstants.ORGANISATION;
import static ch.openech.model.XmlConstants.ORGANISATION_NAME;
import static ch.openech.model.XmlConstants.ORGANISATION_NAME_ADD_ON1;
import static ch.openech.model.XmlConstants.ORGANISATION_NAME_ADD_ON2;
import static ch.openech.model.XmlConstants.PERSON;
import static ch.openech.model.XmlConstants.POST_OFFICE_BOX_NUMBER;
import static ch.openech.model.XmlConstants.POST_OFFICE_BOX_TEXT;
import static ch.openech.model.XmlConstants.STREET;
import static ch.openech.model.XmlConstants.SWISS_ZIP_CODE;
import static ch.openech.model.XmlConstants.SWISS_ZIP_CODE_ADD_ON;
import static ch.openech.model.XmlConstants.SWISS_ZIP_CODE_ID;
import static ch.openech.model.XmlConstants.TITLE;
import static ch.openech.model.XmlConstants.TOWN;

import ch.openech.model.common.Address;

public class WriterEch0010 {

	public final String URI;
	public final WriterEch0008 ech8;
	private final boolean completeCountry;
	
	public WriterEch0010(EchSchema context) {
		URI = context.getNamespaceURI(10);
		ech8 = new WriterEch0008(context);
		completeCountry = context.completeCountryInAddress();
	}
	
	public void address(WriterElement parent, String tagName, Address address) throws Exception {
		address(parent, URI, tagName, address);
	}
	
	public void address(WriterElement parent, String uri, String tagName, Address address) throws Exception {
		if (address == null || address.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
		
		if (address.isOrganisation()) {
			WriterElement organisation = writer.create(URI, ORGANISATION);
			organisation.values(address, ORGANISATION_NAME, ORGANISATION_NAME_ADD_ON1, ORGANISATION_NAME_ADD_ON2, TITLE, FIRST_NAME, LAST_NAME);
		} else if (address.isPerson()) {
			WriterElement person = writer.create(URI, PERSON);
			person.values(address, MR_MRS, TITLE, FIRST_NAME, LAST_NAME);
		}
		
		addressInformation(writer, uri, ADDRESS_INFORMATION, address);
	}

	public void addressInformation(WriterElement parent, String tagName, Address address) throws Exception {
		addressInformation(parent, URI, tagName, address);
	}

	public void addressInformation(WriterElement parent, String uri, String tagName, Address address) throws Exception {
		WriterElement addressInformation = parent.create(URI, tagName);
		addressInformation(addressInformation, address);
	}
	
	private void addressInformation(WriterElement parent, Address address) throws Exception {
		parent.values(address, ADDRESS_LINE1, ADDRESS_LINE2);
		parent.values(address, STREET, HOUSE_NUMBER, DWELLING_NUMBER);
		parent.values(address, POST_OFFICE_BOX_NUMBER, POST_OFFICE_BOX_TEXT);
		parent.values(address, LOCALITY, TOWN);
		if (address.isSwiss()) {
			parent.textIfSet(SWISS_ZIP_CODE, address.getSwissZipCode());
			parent.textIfSet(SWISS_ZIP_CODE_ADD_ON, address.getSwissZipCodeAddOn());
			parent.textIfSet(SWISS_ZIP_CODE_ID, address.getSwissZipCodeId());
		} else {
			parent.textIfSet(FOREIGN_ZIP_CODE, address.zip);
		}
		// TODO if (completeCountry) 
		parent.values(address, COUNTRY);
	}
	
	public void swissAddressInformation(WriterElement parent, String tagName, Address address) throws Exception {
		WriterElement addressInformation = parent.create(URI, tagName);
		swissAddressInformation(addressInformation, address);
	}
	
	private void swissAddressInformation(WriterElement parent, Address address) throws Exception {
		parent.values(address, ADDRESS_LINE1, ADDRESS_LINE2);
		parent.values(address, STREET, HOUSE_NUMBER, DWELLING_NUMBER);
		parent.values(address, LOCALITY, TOWN);
		parent.textIfSet(SWISS_ZIP_CODE, address.getSwissZipCode());
		parent.textIfSet(SWISS_ZIP_CODE_ADD_ON, address.getSwissZipCodeAddOn());
		parent.textIfSet(SWISS_ZIP_CODE_ID, address.getSwissZipCodeId());
		// TODO if (completeCountry) 
		parent.text(COUNTRY, "CH");
	}

}
