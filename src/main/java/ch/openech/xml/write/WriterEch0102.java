package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;
import ch.openech.model.Envelope;


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