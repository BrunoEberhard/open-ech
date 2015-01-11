package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import org.minimalj.util.resources.Resources;

import ch.openech.model.Envelope;


public class WriterEch0058 {

	public final String URI;
	
	public WriterEch0058(EchSchema context) {
		URI = context.getNamespaceURI(58);
	}
	
	public void sendingApplication(WriterElement parent) throws Exception {
    	
		WriterElement sendingApplication = parent.create(URI, SENDING_APPLICATION);
    	
		sendingApplication.text(MANUFACTURER, Resources.getString(Resources.APPLICATION_VENDOR));
		sendingApplication.text(PRODUCT, Resources.getString(Resources.APPLICATION_TITLE));
		sendingApplication.text(PRODUCT_VERSION, Resources.getString(Resources.APPLICATION_VERSION));
    }
    
	public void testDeliveryFlag(WriterElement parent) throws Exception {
		parent.text(TEST_DELIVERY_FLAG, "true");
    }
	
    public void extension(WriterElement parent) throws Exception {
    	WriterElement extension = parent.create(URI, EXTENSION);
    	extension.text(DATA_LOCK, "0");
    }
    
	public void header(WriterElement parent, Envelope envelope) throws Exception {
		WriterElement header = parent.create(URI, DELIVERY_HEADER);
    	
    	header.values(envelope, SENDER_ID, RECIPIENT_ID);
    	
    	sendingApplication(header);

    	header.text(EVENT_DATE, envelope.eventDate);
    	header.text(ACTION, "1");
    }
    
}
