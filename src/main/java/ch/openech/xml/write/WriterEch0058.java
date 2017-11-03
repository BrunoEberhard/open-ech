package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.ACTION;
import static ch.openech.model.XmlConstants.DATA_LOCK;
import static ch.openech.model.XmlConstants.DELIVERY_HEADER;
import static ch.openech.model.XmlConstants.EVENT_DATE;
import static ch.openech.model.XmlConstants.EXTENSION;
import static ch.openech.model.XmlConstants.MANUFACTURER;
import static ch.openech.model.XmlConstants.PRODUCT;
import static ch.openech.model.XmlConstants.PRODUCT_VERSION;
import static ch.openech.model.XmlConstants.RECIPIENT_ID;
import static ch.openech.model.XmlConstants.SENDER_ID;
import static ch.openech.model.XmlConstants.SENDING_APPLICATION;
import static ch.openech.model.XmlConstants.TEST_DELIVERY_FLAG;

import org.minimalj.application.Application;
import org.minimalj.util.StringUtils;

import ch.openech.model.Envelope;
import ch.openech.model.SendingApplication;


public class WriterEch0058 {

	public final String URI;
	
	public WriterEch0058(EchSchema context) {
		URI = context.getNamespaceURI(58);
	}
	
	public void sendingApplication(WriterElement parent) throws Exception {
		WriterElement sendingApplication = parent.create(URI, SENDING_APPLICATION);
    	
		sendingApplication.text(MANUFACTURER, "Open-eCH");
		sendingApplication.text(PRODUCT, "Open-eCH");
		sendingApplication.text(PRODUCT_VERSION, getVersion());
    }

	public void sendingApplication(WriterElement parent, SendingApplication sendingApplication) throws Exception {
		WriterElement element = parent.create(URI, SENDING_APPLICATION);
    	
		element.values(sendingApplication, SendingApplication.$.manufacturer, SendingApplication.$.product, SendingApplication.$.productVersion);
	}
	
	private static String getVersion() {
		String version = Application.getInstance().getClass().getPackage().getImplementationVersion();
		if (StringUtils.isEmpty(version)) {
			version = "DEV";
		}
		return version;
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
