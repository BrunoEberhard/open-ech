package ch.openech.xml.write;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.joda.time.LocalDateTime;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import ch.openech.dm.Envelope;
import ch.openech.dm.XmlConstants;
import ch.openech.dm.common.MunicipalityIdentification;

public abstract class DeliveryWriter {
	private static final Logger logger = Logger.getLogger(DeliveryWriter.class.getName());
	
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

	private Writer writer;
	//private XMLStreamWriter xmlStreamWriter;
	XmlSerializer xmlStreamWriter;
	private WriterElement rootElement;
	protected final EchSchema context;
	private String recipientId = "1-23-4";
	private String senderId = "1-23-4";
	
	protected DeliveryWriter(EchSchema context)  {
		this.context = context;
	}

	protected abstract int getSchemaNumber();
	
	protected abstract String getNamespaceURI();
	//
	
	private Envelope envelope;

	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}

	public Envelope getEnvelope() {
		if (envelope == null) {
			envelope = new Envelope();

			envelope.messageId = UUID.randomUUID().toString();
			envelope.messageType = Integer.toString(getSchemaNumber());
			envelope.messageClass = "0";
			// envelope.senderId = AbstractClient.preferences().get("sedexAddress", null);
			envelope.senderId = senderId;
			envelope.recipientId = recipientId;
			envelope.eventDate = new LocalDateTime();
			envelope.messageDate = envelope.eventDate;
		}
		return envelope;
	}
	
	public void setRecepientMunicipality(MunicipalityIdentification municipalityIdentification) {
		getEnvelope().recipientId = "1-" + municipalityIdentification.municipalityId +"-1";
	}

	public WriterElement delivery() throws Exception {
		return delivery(new StringWriter());
	}

	public WriterElement delivery(Writer writer) throws Exception {
		if (this.writer != null) {
			logger.info("Method result() not called before delivery()");
			writer.close();
			writer = null;
		}

		this.writer = writer;
		//XMLOutputFactory factory = XMLOutputFactory.newInstance();
		//xmlStreamWriter = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(writer));

		//xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		xmlStreamWriter = factory.newSerializer();
		xmlStreamWriter.setOutput(writer);
		xmlStreamWriter.startDocument("UTF-8", null);
		
		startDocument();
		
		// TODO Diese ifs sind etwas gewagt. Es wird davon ausgegangen, dass wenn das Schema
		// 58 oder 78 bekannt ist auch ein Header geschrieben werden soll.
		if (context.getNamespaceURI(102) != null) {
			WriterEch0102 ech102 = new WriterEch0102(context);
			ech102.header(rootElement, getEnvelope());
		} else if (context.getNamespaceURI(78) != null) {
			WriterEch0078 ech78 = new WriterEch0078(context);
			ech78.header(rootElement, getEnvelope());
		} else if (context.getNamespaceURI(58) != null) {
			WriterEch0058 ech58 = new WriterEch0058(context);
			ech58.header(rootElement, getEnvelope());
		}
		return rootElement;
	}
	
	public String result() throws Exception {
		endDocument();
		String result = writer.toString();
		writer.close();
		writer = null;
		return result;
	}
	
	//
	
	private WriterElement startDocument() throws Exception {
		// EchNamespaceContext namespaceContext = getNamespaceContext(getSchemaNumber());

		List<Integer> namespaceNumbers = new ArrayList<Integer>(context.getNamespaceNumbers());
		Collections.sort(namespaceNumbers);
		
		setPrefixs(context, namespaceNumbers);
		xmlStreamWriter.startTag(getNamespaceURI(), getRootType());
		writeXmlSchemaLocation();
		writeNamespaces(context, namespaceNumbers);
		
		//rootElement = new WriterElement(xmlStreamWriter, getNamespaceURI());
		return rootElement;
	}
	
	protected String getRootType() {
		return XmlConstants.DELIVERY;
	}
	
	private void writeXmlSchemaLocation() throws IOException  {
		xmlStreamWriter.attribute(WriterEch0020.XMLSchema_URI, "schemaLocation", context.getNamespaceURI(getSchemaNumber()) + " " + context.getNamespaceLocation(getSchemaNumber()));
	}

	private void setPrefixs(EchSchema namespaceContext, List<Integer> namespaceNumbers) {
//		xmlStreamWriter.setPrefix("xsi", XMLSchema_URI);
//		for (int number : namespaceNumbers) {
//			xmlStreamWriter.setPrefix("e" + number, namespaceContext.getNamespaceURI(number));
//		}
	}
	
	private void writeNamespaces(EchSchema namespaceContext, List<Integer> namespaceNumbers) {
//		xmlStreamWriter.writeNamespace("xsi", XMLSchema_URI);
//		for (int number : namespaceNumbers) {
//			xmlStreamWriter.writeNamespace("e" + number, namespaceContext.getNamespaceURI(number));
//		}
//		if (namespaceContext.getOpenEchNamespaceLocation() != null) {
//			xmlStreamWriter.writeNamespace("openEch", namespaceContext.getOpenEchNamespaceLocation());
//		}
	}

	public void endDocument() throws Exception {
		rootElement.flush();
		xmlStreamWriter.endDocument();
		writer.flush();
	}
	
}
