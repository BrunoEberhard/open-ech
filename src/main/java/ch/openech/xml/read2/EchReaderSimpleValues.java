package ch.openech.xml.read2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;

import ch.openech.model.types.EchCode;

public class EchReaderSimpleValues extends EchAttributeReader {
	private static Logger logger = Logger.getLogger(EchReaderSimpleValues.class.getName());
	private static final int MAX_DATE_LENGHT = "01-02-1934".length();
	private static final int MAX_DATE_TIME_LENGHT = "01-02-1934 12:12:12".length();

	@Override
	public boolean read(XMLEventReader xml, String attributename, Object object) throws XMLStreamException {
		PropertyInterface property = FlatProperties.getProperty(object.getClass(), attributename);
		if (property == null) {
			return false;
		}

		Object value = null;
		if (property.getClazz() == String.class) {
			value = token(xml);
		} else if (property.getClazz() == Boolean.class) {
			value = bulean(xml);
		} else if (property.getClazz() == Integer.class) {
			value = integer(xml);
		} else if (property.getClazz() == LocalDate.class) {
			value = date(xml);
		} else if (Enum.class.isAssignableFrom(property.getClazz())) {
			enuum(xml, object, property);
			return true;
		} else {
			return false;
		}
		property.setValue(object, value);
		return true;
	}

	public static String token(XMLEventReader xml) throws XMLStreamException {
		String token = null;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				token = event.asCharacters().getData().trim();
			} else if (event.isEndElement()) {
				return token;
			} // else skip
		}
	}

	public static int integer(XMLEventReader xml) throws XMLStreamException {
		int i = 0;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				String token = event.asCharacters().getData().trim();
				i = Integer.parseInt(token);
			} else if (event.isEndElement()) {
				return i;
			} // else skip
		}
	}

	public static int bulean(XMLEventReader xml) throws XMLStreamException {
		int i = -1;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				String token = event.asCharacters().getData().trim();
				i = Boolean.parseBoolean(token) || "1".equals(token) ? 1 : 0;
			} else if (event.isEndElement()) {
				if (i == -1)
					throw new IllegalArgumentException();
				return i;
			} // else skip
		}
	}

	public static void enuum(XMLEventReader xml, Object object, Object key) throws XMLStreamException {
		PropertyInterface property = Keys.getProperty(key);
		enuum(xml, object, property);
	}

	public static void enuum(XMLEventReader xml, Object object, PropertyInterface property) throws XMLStreamException {
		boolean found = false;
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isCharacters()) {
				String token = event.asCharacters().getData().trim();
				enuum(token, object, property);
				found = true;
			} else if (event.isEndElement()) {
				if (!found)
					throw new IllegalArgumentException();
				else
					return;
			} // else skip
		}
	}

	public static <T extends Enum<T>> void enuum(String value, Object object, PropertyInterface property) {
		@SuppressWarnings("unchecked")
		Class<T> enumClass = (Class<T>) property.getClazz();
		List<T> values = EnumUtils.valueList(enumClass);
		for (Object enumValue : values) {
			EchCode echCode = (EchCode) enumValue;
			if (echCode.getValue().equals(value)) {
				property.setValue(object, echCode);
				return;
			}
		}
		Object createdEnum = InvalidValues.createInvalidEnum(enumClass, value);
		property.setValue(object, createdEnum);
	}

	public static <T extends Enum<T>> T enuum(Class<T> enumClass, String value) {
		List<T> values = EnumUtils.valueList(enumClass);
		for (T enumValue : values) {
			EchCode echCode = (EchCode) enumValue;
			if (echCode.getValue().equals(value)) {
				return enumValue;
			}
		}
		return InvalidValues.createInvalidEnum(enumClass, value);
	}

	public static LocalDate date(XMLEventReader xml) throws XMLStreamException {
		String text = token(xml);
		if (text.length() > MAX_DATE_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			text = text.substring(0, MAX_DATE_LENGHT);
		}
		return LocalDate.from(DateTimeFormatter.ISO_DATE.parse(text));
	}
}