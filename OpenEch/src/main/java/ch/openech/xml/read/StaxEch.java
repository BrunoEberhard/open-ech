package ch.openech.xml.read;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.InvalidValues;
import org.minimalj.model.Keys;
import org.minimalj.model.PropertyInterface;
import org.minimalj.model.properties.FlatProperties;
import org.minimalj.util.DateUtils;

import ch.openech.model.types.EchCode;

public class StaxEch {
	private static Logger logger = Logger.getLogger(StaxEch.class.getName());
	private static final int MAX_DATE_LENGHT = "01-02-1934".length();
	private static final int MAX_DATE_TIME_LENGHT = "01-02-1934 12:12:12".length();
	
	public static LocalDate date(XMLEventReader xml) throws XMLStreamException {
		String text = token(xml);
		if (text.length() > MAX_DATE_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			text = text.substring(0, MAX_DATE_LENGHT);
		}
		return ISODateTimeFormat.date().parseLocalDate(text);
	}

	public static LocalDateTime dateTime(XMLEventReader xml) throws XMLStreamException {
		String text = token(xml);
		if (text.length() > MAX_DATE_TIME_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			text = text.substring(0, MAX_DATE_TIME_LENGHT);
		}
		return ISODateTimeFormat.dateHourMinuteSecond().parseLocalDateTime(text);
	}

	public static ReadablePartial partial(XMLEventReader xml) throws XMLStreamException {
		String text = token(xml);
		// handle dates like 1950-08-30+01:00
		if (text != null && text.length() > 10) text = text.substring(0, 10);
		return DateUtils.parsePartial(text);
	}

	public static void simpleValue(XMLEventReader xml, Object object, Object key) throws XMLStreamException {
		PropertyInterface property = FlatProperties.getProperties(object.getClass()).get(key);
		if (property == null) {
			throw new IllegalArgumentException("Unknown field: " + key);
		}
		Object value = null;
		if (property.getFieldClazz() == String.class) {
			value = token(xml);
		} else if (property.getFieldClazz() == Boolean.class) {
			value = bulean(xml);
		} else if (property.getFieldClazz() == Integer.class) {
			value = integer(xml);
		} else if (property.getFieldClazz() == LocalDate.class) {
			value = date(xml);
		} else if (Enum.class.isAssignableFrom(property.getFieldClazz())) {
			enuum(xml, object, property);
			return;
		} else {
			throw new IllegalArgumentException("Unknown field type: " + property.getFieldClazz().getName());
		}
		property.setValue(object, value);
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
				if (i == -1) throw new IllegalArgumentException();
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
				if (!found) throw new IllegalArgumentException(); else return;
			} // else skip
		}
	}
	
	public static <T extends Enum<T>> void enuum(String value, Object object, PropertyInterface property) {
		@SuppressWarnings("unchecked")
		Class<T> enumClass = (Class<T>) property.getFieldClazz();
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

	public static void skip(XMLEventReader xml) throws XMLStreamException {
		while (true) {
			XMLEvent event = xml.nextEvent();
			if (event.isStartElement()) {
				logger.fine("Skipping XML Element: " + event.asStartElement().getName().getLocalPart());
				skip(xml);
			} else if (event.isEndElement()) break;
			// else ignore
		}
	}
	
}
