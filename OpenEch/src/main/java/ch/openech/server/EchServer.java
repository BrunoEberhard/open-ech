package ch.openech.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.openech.mj.db.DbPersistence;
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
			persistence = new EchPersistence(DbPersistence.mariaDbDataSource("OpenEch", "APP", "APP"));
//			persistence = new EchPersistence(DbPersistence.embeddedDataSource());
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
	
	private static Validator validator;
	
	static Validator getValidator() throws SAXException {
		if (validator == null) {
		    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		    
		    LSResourceResolver resourceResolver = new LSResourceResolver() {
				@Override
				public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
					if (systemId == null) return null;
					
					int pos = systemId.lastIndexOf("/");
					String fileName = "/ch/ech/xmlns" + systemId.substring(pos);
					InputStream stream = this.getClass().getResourceAsStream(fileName);
					
					return new LSInputImpl(publicId, systemId, baseURI, stream, null);
				}
			};
			
		    schemaFactory.setResourceResolver(resourceResolver);
		    Schema schema = schemaFactory.newSchema();
		    validator = schema.newValidator();
		    validator.setResourceResolver(resourceResolver);
		}
		return validator;
	}
	
	public static String validate(String string)  {
		String message = OK;
		try {
		    getValidator().validate(new StreamSource(new StringReader(string)));
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
	
	public ServerCallResult process(String... xmlStrings) {
		return process(ech0020, xmlStrings);
	}

	public ServerCallResult processOrg(String... xmlStrings) {
		return process(ech0148, xmlStrings);
	}
	
	public ServerCallResult process(final StaxEchParser parser, final String... xmlStrings) {
		if (xmlStrings.length > 1) {
			logger.fine("process " + xmlStrings.length + " xmls");
		}

		final ServerCallResult result = new ServerCallResult();
		try {
			persistence.transaction(new Runnable() {
				public void run() {
					for (String xmlString : xmlStrings) {
						logger.fine(xmlString);
						try {
							parser.process(xmlString);
						} catch (XMLStreamException e) {
							// TODO process should not throw that (but runtime)
							e.printStackTrace();
						}
					}
					String lastInsertedId = parser.getLastInsertedId();
					if (lastInsertedId != null) {
						result.createdPersonId = lastInsertedId;
					}
				}
			});
		} catch (Exception x) {
			if (result.exception == null) {
				result.errorMessage = x.getLocalizedMessage();
				result.exception = x;
			}
		}
		
		return result;
	}

	public ServerCallResult process_(final StaxEchParser parser, final String... xmlStrings) {
		if (xmlStrings.length > 1) {
			logger.fine("process " + xmlStrings.length + " xmls");
		}

		final ServerCallResult result = new ServerCallResult();
		try {
			for (String xmlString : xmlStrings) {
				logger.fine(xmlString);
				try {
					parser.process(xmlString);
				} catch (XMLStreamException e) {
					// TODO process should not throw that (but runtime)
					e.printStackTrace();
				}
			}
			String lastInsertedId = parser.getLastInsertedId();
			if (lastInsertedId != null) {
				result.createdPersonId = lastInsertedId;
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
