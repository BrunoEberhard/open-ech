package ch.openech.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
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

public class EchSchemaValidation {
	public static final Logger logger = Logger.getLogger(EchSchemaValidation.class.getName());
	public static final String OK = "ok";
	private static Map<String, String> fileByNamespace = new HashMap<>();

	//

	static {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(EchSchemaValidation.class.getClassLoader().getResourceAsStream("catalog.txt")))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("=");
				fileByNamespace.put(parts[0].trim(), parts[1].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Validator validator;

	static Validator getValidator() throws SAXException {
		if (validator == null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			LSResourceResolver resourceResolver = new LSResourceResolver() {
				@Override
				public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
					String fileName = fileByNamespace.get(namespaceURI);
					InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);

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

	public static String validate(String string) {
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
		} catch (Exception e) {
			message = e.getLocalizedMessage();
			e.printStackTrace();
		}
		return message;
	}

}
