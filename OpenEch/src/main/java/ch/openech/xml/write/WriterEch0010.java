package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;
import ch.openech.model.common.Address;

public class WriterEch0010 {

	public final String URI;
	
	public WriterEch0010(EchSchema context) {
		URI = context.getNamespaceURI(10);
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
	
	private static void addressInformation(WriterElement parent, Address address) throws Exception {
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
		parent.values(address, COUNTRY);
	}
	
	public void swissAddressInformation(WriterElement parent, String tagName, Address address) throws Exception {
		WriterElement addressInformation = parent.create(URI, tagName);
		swissAddressInformation(addressInformation, address);
	}
	
	private static void swissAddressInformation(WriterElement parent, Address address) throws Exception {
		parent.values(address, ADDRESS_LINE1, ADDRESS_LINE2);
		parent.values(address, STREET, HOUSE_NUMBER, DWELLING_NUMBER);
		parent.values(address, LOCALITY, TOWN);
		parent.textIfSet(SWISS_ZIP_CODE, address.getSwissZipCode());
		parent.textIfSet(SWISS_ZIP_CODE_ADD_ON, address.getSwissZipCodeAddOn());
		parent.textIfSet(SWISS_ZIP_CODE_ID, address.getSwissZipCodeId());
		parent.text(COUNTRY, "CH");
	}

}
