package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.ADDRESS_INFORMATION;
import static ch.openech.dm.XmlConstants.ADDRESS_LINE1;
import static ch.openech.dm.XmlConstants.ADDRESS_LINE2;
import static ch.openech.dm.XmlConstants.COUNTRY;
import static ch.openech.dm.XmlConstants.DWELLING_NUMBER;
import static ch.openech.dm.XmlConstants.FIRST_NAME;
import static ch.openech.dm.XmlConstants.FOREIGN_ZIP_CODE;
import static ch.openech.dm.XmlConstants.HOUSE_NUMBER;
import static ch.openech.dm.XmlConstants.LAST_NAME;
import static ch.openech.dm.XmlConstants.LOCALITY;
import static ch.openech.dm.XmlConstants.MR_MRS;
import static ch.openech.dm.XmlConstants.ORGANISATION;
import static ch.openech.dm.XmlConstants.ORGANISATION_NAME;
import static ch.openech.dm.XmlConstants.ORGANISATION_NAME_ADD_ON1;
import static ch.openech.dm.XmlConstants.ORGANISATION_NAME_ADD_ON2;
import static ch.openech.dm.XmlConstants.PERSON;
import static ch.openech.dm.XmlConstants.POST_OFFICE_BOX_NUMBER;
import static ch.openech.dm.XmlConstants.POST_OFFICE_BOX_TEXT;
import static ch.openech.dm.XmlConstants.STREET;
import static ch.openech.dm.XmlConstants.SWISS_ZIP_CODE;
import static ch.openech.dm.XmlConstants.SWISS_ZIP_CODE_ADD_ON;
import static ch.openech.dm.XmlConstants.SWISS_ZIP_CODE_ID;
import static ch.openech.dm.XmlConstants.TITLE;
import static ch.openech.dm.XmlConstants.TOWN;
import ch.openech.dm.common.Address;

public class WriterEch0010 {

	public final String URI;
	
	public WriterEch0010(EchNamespaceContext context) {
		URI = context.getNamespaceURI(10);
	}
	
	public void address(WriterElement parent, String tagName, Address address) throws Exception {
		if (address == null || address.isEmpty()) return;
		WriterElement writer = parent.create(URI, tagName);
		
		if (address.isOrganisation()) {
			WriterElement organisation = writer.create(URI, ORGANISATION);
			organisation.values(address, ORGANISATION_NAME, ORGANISATION_NAME_ADD_ON1, ORGANISATION_NAME_ADD_ON2, TITLE, FIRST_NAME, LAST_NAME);
		} else if (address.isPerson()) {
			WriterElement person = writer.create(URI, PERSON);
			person.values(address, MR_MRS, TITLE, FIRST_NAME, LAST_NAME);
		}
		
		addressInformation(writer, ADDRESS_INFORMATION, address);
	}

	public void addressInformation(WriterElement parent, String tagName, Address address) throws Exception {
		WriterElement addressInformation = parent.create(URI, tagName);
		addressInformation(addressInformation, address);
	}
	
	private static void addressInformation(WriterElement parent, Address address) throws Exception {
		parent.values(address, ADDRESS_LINE1, ADDRESS_LINE2);
		parent.values(address, STREET, HOUSE_NUMBER, DWELLING_NUMBER);
		parent.values(address, POST_OFFICE_BOX_NUMBER, POST_OFFICE_BOX_TEXT);
		parent.values(address, LOCALITY, TOWN);
		parent.values(address, SWISS_ZIP_CODE, SWISS_ZIP_CODE_ADD_ON, SWISS_ZIP_CODE_ID);
		parent.values(address, FOREIGN_ZIP_CODE);
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
		parent.values(address, SWISS_ZIP_CODE, SWISS_ZIP_CODE_ADD_ON, SWISS_ZIP_CODE_ID);
		parent.text(COUNTRY, "CH");
	}

}
