package ch.openech.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.openech.xml.read.LSInputImpl;
import ch.openech.xml.read.StaxEch0020;
import ch.openech.xml.read.StaxEch0148;
import ch.openech.xml.read.StaxEchParser;

public class EchServer {
	public static final Logger logger = Logger.getLogger(EchServer.class.getName());
	public static final String OK = "ok";
	private static EchServer instance;
	private EchPersistence persistence;
	private StaxEch0020 ech0020;
	private StaxEch0148 ech0148;
	
	private EchServer() {
		try {
			persistence = new EchPersistence();
			ech0020 = new StaxEch0020(persistence);
			ech0148 = new StaxEch0148(persistence);
			instance = this;
		} catch (Exception x) {
			x.printStackTrace();
			logger.log(Level.SEVERE, "Couldnt initialize EchServer", x);
		}
	}

	public static synchronized EchServer getInstance() {
		if (instance == null) {
			instance = new EchServer();
		}
		return instance;
	}
	
	public EchPersistence getPersistence() {
		return persistence;
	}
	
	//
	
	public static String validate(String string)  {
		String message = OK;
		try {
		    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		    
		    LSResourceResolver resourceResolver = new LSResourceResolver() {
				@Override
				public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
					if (systemId == null) return null;
					
					int pos = systemId.lastIndexOf("/");
					String fileName = "ch/ech/xmlns" + systemId.substring(pos);
					InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
					
					return new LSInputImpl(publicId, systemId, baseURI, stream, null);
				}
			};
			
		    schemaFactory.setResourceResolver(resourceResolver);
		    Schema schema = schemaFactory.newSchema();
		    Validator validator = schema.newValidator();
		    validator.setResourceResolver(resourceResolver);
		    validator.validate(new StreamSource(new StringReader(string)));
	    } catch (SAXParseException parseException) {
	    	message = "XML invalid:\n";
	    	message += parseException.getLocalizedMessage() + "\n";
	    	message += "Line: " + parseException.getLineNumber();
	    	message += " - Column: " + parseException.getColumnNumber();
	    } catch (SAXException e) {
			message = e.getLocalizedMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message = e.getLocalizedMessage();
			e.printStackTrace();
		}
	    return message;
	}
	
	public synchronized ServerCallResult process(String... xmlStrings) {
		return process(ech0020, xmlStrings);
	}

	public synchronized ServerCallResult processOrg(String... xmlStrings) {
		return process(ech0148, xmlStrings);
	}
	
	public synchronized ServerCallResult process(StaxEchParser parser, String... xmlStrings) {
		if (xmlStrings.length > 1) {
			logger.fine("process " + xmlStrings.length + " xmls");
		}

		ServerCallResult result = new ServerCallResult();
		try {
			try {
				for (String xmlString : xmlStrings) {
					logger.fine(xmlString);
					parser.process(xmlString);
				}
				persistence.commit();
				String lastInsertedId = parser.getLastInsertedId();
				if (lastInsertedId != null) {
					result.createdPersonId = lastInsertedId;
				}
			} catch (Exception x) {
				x.printStackTrace();
				result.errorMessage = x.getLocalizedMessage();
				result.exception = x;
				persistence.rollback();
			} 
		} catch (Exception x) {
			if (result.exception == null) {
				result.errorMessage = x.getLocalizedMessage();
				result.exception = x;
			}
		}
		
		return result;
	}

}
