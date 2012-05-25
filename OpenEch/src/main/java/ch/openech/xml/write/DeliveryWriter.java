package ch.openech.xml.write;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ch.openech.dm.Envelope;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.util.DateUtils;

public abstract class DeliveryWriter {
	private static final Logger logger = Logger.getLogger(DeliveryWriter.class.getName());
	
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";

	private Writer writer;
	private XMLStreamWriter xmlStreamWriter;
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
			Date date = new Date();
			envelope.eventDate = DateUtils.formatXsd(date);
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
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		xmlStreamWriter = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(writer));

		xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
		
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
	
	private WriterElement startDocument() throws XMLStreamException {
		// EchNamespaceContext namespaceContext = getNamespaceContext(getSchemaNumber());

		List<Integer> namespaceNumbers = new ArrayList<Integer>(context.getNamespaceNumbers());
		Collections.sort(namespaceNumbers);
		
		setPrefixs(context, namespaceNumbers);
		xmlStreamWriter.writeStartElement(getNamespaceURI(), getRootType());
		writeXmlSchemaLocation();
		writeNamespaces(context, namespaceNumbers);
		
		rootElement = new WriterElement(xmlStreamWriter, getNamespaceURI());
		return rootElement;
	}
	
	protected String getRootType() {
		return "delivery";
	}
	
	private void writeXmlSchemaLocation() throws XMLStreamException {
		xmlStreamWriter.writeAttribute(WriterEch0020.XMLSchema_URI, "schemaLocation",
				context.getNamespaceURI(getSchemaNumber()) + " " + 
				context.getNamespaceLocation(getSchemaNumber()));
	}

	private void setPrefixs(EchSchema namespaceContext, List<Integer> namespaceNumbers) throws XMLStreamException {
		xmlStreamWriter.setPrefix("xsi", XMLSchema_URI);
		for (int number : namespaceNumbers) {
			xmlStreamWriter.setPrefix("e" + number, namespaceContext.getNamespaceURI(number));
		}
	}
	
	private void writeNamespaces(EchSchema namespaceContext, List<Integer> namespaceNumbers) throws XMLStreamException {
		xmlStreamWriter.writeNamespace("xsi", XMLSchema_URI);
		for (int number : namespaceNumbers) {
			xmlStreamWriter.writeNamespace("e" + number, namespaceContext.getNamespaceURI(number));
		}
		if (namespaceContext.getOpenEchNamespaceLocation() != null) {
			xmlStreamWriter.writeNamespace("openEch", namespaceContext.getOpenEchNamespaceLocation());
		}
	}

	public void endDocument() throws Exception {
		rootElement.flush();
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeEndDocument();

		xmlStreamWriter.flush();
		writer.flush();
	}
	
}
