package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.ACTION;
import static ch.openech.dm.XmlConstants.DATA_LOCK;
import static ch.openech.dm.XmlConstants.DELIVERY_HEADER;
import static ch.openech.dm.XmlConstants.EVENT_DATE;
import static ch.openech.dm.XmlConstants.EXTENSION;
import static ch.openech.dm.XmlConstants.MANUFACTURER;
import static ch.openech.dm.XmlConstants.PRODUCT;
import static ch.openech.dm.XmlConstants.PRODUCT_VERSION;
import static ch.openech.dm.XmlConstants.RECIPIENT_ID;
import static ch.openech.dm.XmlConstants.SENDER_ID;
import static ch.openech.dm.XmlConstants.SENDING_APPLICATION;
import static ch.openech.dm.XmlConstants.TEST_DELIVERY_FLAG;
import ch.openech.dm.Envelope;
import ch.openech.mj.resources.ResourceHelper;


public class WriterEch0058 {

	public final String URI;
	
	public WriterEch0058(EchSchema context) {
		URI = context.getNamespaceURI(58);
	}
	
	public void sendingApplication(WriterElement parent) throws Exception {
    	
		WriterElement sendingApplication = parent.create(URI, SENDING_APPLICATION);
    	
		sendingApplication.text(MANUFACTURER, ResourceHelper.getApplicationVendor());
		sendingApplication.text(PRODUCT, ResourceHelper.getApplicationTitle());
		sendingApplication.text(PRODUCT_VERSION, ResourceHelper.getApplicationVersion());
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
