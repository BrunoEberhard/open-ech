package ch.openech.xml.write;

import static  ch.openech.model.XmlConstants.ADDRESS_INFO;
import static  ch.openech.model.XmlConstants.ADMINISTRATIVE_DWELLING_ID;
import static  ch.openech.model.XmlConstants.BEGIN_OF_CONTRACT;
import static  ch.openech.model.XmlConstants.BUILDING;
import static  ch.openech.model.XmlConstants.CALL_NAME;
import static  ch.openech.model.XmlConstants.COMES_FROM;
import static  ch.openech.model.XmlConstants.CONTRACT;
import static  ch.openech.model.XmlConstants.CONTRACTOR;
import static  ch.openech.model.XmlConstants.DATE_OF_BIRTH;
import static  ch.openech.model.XmlConstants.DWELLING;
import static  ch.openech.model.XmlConstants.END_OF_CONTRACT;
import static  ch.openech.model.XmlConstants.FLOOR;
import static  ch.openech.model.XmlConstants.GOES_TO;
import static  ch.openech.model.XmlConstants.MOVE_IN;
import static  ch.openech.model.XmlConstants.MOVE_OUT;
import static  ch.openech.model.XmlConstants.NATIONALITY;
import static  ch.openech.model.XmlConstants.OFFICIAL_NAME;
import static  ch.openech.model.XmlConstants.PERSON;
import static  ch.openech.model.XmlConstants.POSITION_ON_FLOOR;
import static  ch.openech.model.XmlConstants.ROOM_COUNT;
import static  ch.openech.model.XmlConstants.SEX;
import static  ch.openech.model.XmlConstants._E_G_I_D;
import static  ch.openech.model.XmlConstants._E_W_I_D;
import  ch.openech.model.tpn.ThirdPartyMove;

public class WriterEch0112 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0007 ech7;
	public final WriterEch0008 ech8;
	public final WriterEch0010 ech10;
	public final WriterEch0011 ech11;
	public final WriterEch0021 ech21;
	public final WriterEch0044 ech44;
	public final WriterEch0046 ech46;
	
	public WriterEch0112(EchSchema context) {
		super(context);
		
		URI = context.getNamespaceURI(112);
		ech7 = new WriterEch0007(context);
		ech8 = new WriterEch0008(context);
		ech10 = new WriterEch0010(context);
		ech11 = new WriterEch0011(context);
		ech21 = new WriterEch0021(context);
		ech44 = new WriterEch0044(context);
		ech46 = new WriterEch0046(context);
	}
	
	@Override
	protected int getSchemaNumber() {
		return 112;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}

	// Elemente für alle Events

	private void contractor(WriterElement parent, ThirdPartyMove contract) throws Exception {
		WriterElement element = parent.create(URI, CONTRACTOR);
    	ech46.contact(element, contract.contractor);
	}
	
	private void person(WriterElement parent, ThirdPartyMove contract) throws Exception {
		WriterElement element = parent.create(URI, PERSON);
		element.values(contract, OFFICIAL_NAME, CALL_NAME);
		ech44.datePartiallyKnownType(element,  DATE_OF_BIRTH, contract);
		element.values(contract, SEX);
		ech8.country(element, NATIONALITY, contract.nationality);
	}

	private void contract(WriterElement parent, ThirdPartyMove contract) throws Exception {
		WriterElement element = parent.create(URI, CONTRACT);
		element.values(contract, BEGIN_OF_CONTRACT, END_OF_CONTRACT);
	}

	private void building(WriterElement parent, ThirdPartyMove contract) throws Exception {
		WriterElement element = parent.create(URI, BUILDING);
		element.values(contract, _E_G_I_D);
		ech10.swissAddressInformation(element, ADDRESS_INFO, contract.address);
	}
	
	private void dwelling(WriterElement parent, ThirdPartyMove contract) throws Exception {
		WriterElement element = parent.create(URI, DWELLING);
		element.values(contract, _E_W_I_D);
		element.values(contract, ADMINISTRATIVE_DWELLING_ID, FLOOR, POSITION_ON_FLOOR, ROOM_COUNT);
	}
	
	// /////////////////////////////////

	// code 1
	public String moveIn(ThirdPartyMove contract) throws Exception {
		WriterElement event = delivery().create(URI, MOVE_IN);
		move(event, contract);
		return result();
	}
	

	// code 2
	public String moveOut(ThirdPartyMove contract) throws Exception {
		WriterElement event = delivery().create(URI, MOVE_OUT);
		move(event, contract);
		return result();
	}
	
	private void move(WriterElement parent, ThirdPartyMove contract) throws Exception {
		contractor(parent, contract);
		person(parent, contract);
		contract(parent, contract);
		building(parent, contract);
		dwelling(parent, contract);
		if (contract.comesFrom != null && !contract.comesFrom.isEmpty()) {
			ech10.addressInformation(parent, COMES_FROM, contract.comesFrom);
		}
		if (contract.goesTo != null && !contract.goesTo.isEmpty()) {
			ech10.addressInformation(parent, GOES_TO, contract.goesTo);
		}
	}
	
}
