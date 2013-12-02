package ch.openech.xml.write;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;


public class StaxWriterFactory {

	private XmlSerializer writer;
	private WriterElement rootElement;
	private List<String> prefixList = new ArrayList<String>();
	private List<String> uriList = new ArrayList<String>();
	
	public StaxWriterFactory(Writer outputWriter) throws Exception {
		//XMLOutputFactory factory = XMLOutputFactory.newInstance();
	    //writer = new IndentingXMLStreamWriter(factory.createXMLStreamWriter(outputWriter));

	    //writer.writeStartDocument("UTF-8", "1.0");
	}
	
	public WriterElement create(String childNameSpaceURI, String localName) throws Exception {
		//writer.writeStartElement(childNameSpaceURI, localName); // !!
		writeNamespaces();
		
		rootElement = new WriterElement(writer, childNameSpaceURI);
		return rootElement;
	}
	
	private void writeNamespaces() throws Exception {
		for (int i = 0; i<prefixList.size(); i++) {
			//writer.writeNamespace(prefixList.get(i), uriList.get(i));
		}
	}

	public void setPrefix(String prefix, String uri) throws Exception {
		prefixList.add(prefix);
		uriList.add(uri);
		//writer.setPrefix(prefix, uri);
	}
	
	public void flush() throws Exception {
		rootElement.flush();
		
		//writer.writeEndElement();
	    //writer.writeEndDocument();

	    //writer.flush();
	    //writer.close();
	}
	
}
