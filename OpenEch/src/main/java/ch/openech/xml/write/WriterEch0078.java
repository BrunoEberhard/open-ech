package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;
import ch.openech.dm.Envelope;


public class WriterEch0078 {

	public final String URI;
	public final WriterEch0058 ech58;
	
	public WriterEch0078(EchNamespaceContext context) {
		URI = context.getNamespaceURI(78);
		ech58 = new WriterEch0058(context);
	}
	
	public void header(WriterElement parent, Envelope envelope) throws Exception {
		WriterElement header = parent.create(URI, DELIVERY_HEADER);
    	
		header.values(envelope, SENDER_ID, RECIPIENT_ID, MESSAGE_ID, MESSAGE_TYPE);
    	
		ech58.sendingApplication(header);

		header.values(envelope, MESSAGE_DATE, EVENT_DATE);
    	header.text(ACTION, "1");
    	
    	ech58.testDeliveryFlag(header);
    	extension(header);
    }

    public void extension(WriterElement parent) throws Exception {
    	WriterElement extension = parent.create(URI, EXTENSION);
    	extension.text(DATA_LOCK, "0");
    }
	
}
