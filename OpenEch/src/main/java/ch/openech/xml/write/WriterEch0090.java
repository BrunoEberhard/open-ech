package ch.openech.xml.write;

import java.io.StringWriter;
import java.io.Writer;

import  ch.openech.model.Envelope;
import  ch.openech.model.XmlConstants;

public class WriterEch0090 {

	public static final String URI = "http://www.ech.ch/xmlns/eCH-0090/1";
	public static final String XMLSchema_URI = "http://www.w3.org/2001/XMLSchema-instance";
	private StaxWriterFactory factory;
	private Writer writer;
	
	
	public WriterEch0090() throws Exception {
		this.writer = new StringWriter();
		
		factory = new StaxWriterFactory(writer);
		factory.setPrefix("e90", WriterEch0090.URI);
        factory.setPrefix("xsi", XMLSchema_URI);
    }
	
	public String envelope(Envelope envelope) throws Exception {
        WriterElement root = factory.create(URI, XmlConstants.ENVELOPE);
        root.writeAttribute(XMLSchema_URI, "schemaLocation", "http://www.ech.ch/xmlns/eCH-0108/1 http://www.ech.ch/xmlns/eCH-0090/1/eCH-0090-1-0.xsd");
        
        root.values(envelope);
        return result();
	}
	
	public void flush() throws Exception {
		factory.flush();
		writer.flush();
	}
	
	public String result() throws Exception {
		flush();
		String result = writer.toString();
		writer.close();
		return result;
	}
	
}
