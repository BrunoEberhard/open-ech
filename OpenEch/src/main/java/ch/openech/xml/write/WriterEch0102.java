package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.ACTION;
import static ch.openech.dm.XmlConstants.EVENT_DATE;
import static ch.openech.dm.XmlConstants.EVENT_DATE_FROM;
import static ch.openech.dm.XmlConstants.EVENT_DATE_TO;
import static ch.openech.dm.XmlConstants.EXTENSION;
import static ch.openech.dm.XmlConstants.HEADER;
import static ch.openech.dm.XmlConstants.MESSAGE_DATE;
import static ch.openech.dm.XmlConstants.MESSAGE_ID;
import static ch.openech.dm.XmlConstants.MESSAGE_TYPE;
import static ch.openech.dm.XmlConstants.RECIPIENT_ID;
import static ch.openech.dm.XmlConstants.SENDER_ID;
import ch.openech.dm.Envelope;


public class WriterEch0102 {

	public final String URI;
	public final WriterEch0058 ech58;
	
	public WriterEch0102(EchSchema context) {
		URI = context.getNamespaceURI(102);
		ech58 = new WriterEch0058(context);
	}
	
	public void header(WriterElement parent, Envelope envelope) throws Exception {
		WriterElement header = parent.create(URI, HEADER);
    	
		header.values(envelope, SENDER_ID, RECIPIENT_ID, MESSAGE_ID, MESSAGE_TYPE);
    	
		ech58.sendingApplication(header);

		header.values(envelope, MESSAGE_DATE, EVENT_DATE);
    	header.text(ACTION, "1");
    	
    	ech58.testDeliveryFlag(header);
    	eventInformation(header);
    }

    public void eventInformation(WriterElement parent) throws Exception {
    	WriterElement extension = parent.create(URI, EXTENSION);
    	eventDate(extension);
    	// TODO das ist noch nicht komplett
    }
	
    public void eventDate(WriterElement parent) throws Exception {
    	WriterElement extension = parent.create(URI, EVENT_DATE);
    	extension.text(EVENT_DATE_FROM, "2011-01-01");
    	extension.text(EVENT_DATE_TO, "2011-02-02");
    }
    
}
