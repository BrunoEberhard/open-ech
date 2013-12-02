package ch.openech.xml.read;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ch.openech.dm.types.EchCode;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.InvalidValues;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.DateUtils;

public class StaxEch {
	private static Logger logger = Logger.getLogger(StaxEch.class.getName());
	private static final int MAX_DATE_LENGHT = "01-02-1934".length();
	private static final int MAX_DATE_TIME_LENGHT = "01-02-1934 12:12:12".length();
	
	public static LocalDate date(XmlPullParser xml) throws  XmlPullParserException, IOException {
		String text = token(xml);
		if (text.length() > MAX_DATE_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			text = text.substring(0, MAX_DATE_LENGHT);
		}
		return ISODateTimeFormat.date().parseLocalDate(text);
	}

	public static LocalDateTime dateTime(XmlPullParser xml) throws XmlPullParserException, IOException {
		String text = token(xml);
		if (text.length() > MAX_DATE_TIME_LENGHT) {
			// Einige Schlaumeier hängen ein "+01:00" oder eine Zeitzone an
			text = text.substring(0, MAX_DATE_TIME_LENGHT);
		}
		return ISODateTimeFormat.dateHourMinuteSecond().parseLocalDateTime(text);
	}

	public static ReadablePartial partial(XmlPullParser xml) throws  XmlPullParserException, IOException {
		String text = token(xml);
		// handle dates like 1950-08-30+01:00
		if (text != null && text.length() > 10) text = text.substring(0, 10);
		return DateUtils.parsePartial(text);
	}

	public static void simpleValue(XmlPullParser xml, Object object, Object key) throws XmlPullParserException, IOException {
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
	
	public static String token(XmlPullParser xmlParser) throws  XmlPullParserException, IOException {
		String token = null;
		while (true) {
			int event = xmlParser.next();
			if (isText(event)) {
				token = xmlParser.getText();
			} else if (isEndTag(event)) {
				return token;
			} // else skip
		}
	}
	
	public static int integer(XmlPullParser xml) throws XmlPullParserException, IOException {
		int i = 0;
		while (true) {
			int event = xml.next();
			if (isText(event)) {
				String token = xml.getText();
				i = Integer.parseInt(token);
			} else if (isEndTag(event)) {
				return i;
			} // else skip
		}
	}
	
	public static int bulean(XmlPullParser xml) throws  XmlPullParserException, IOException {
		int i = -1;
		while (true) {
			int event = xml.next();
			if (isText(event)) {
				String token = xml.getText();
				i = Boolean.parseBoolean(token) || "1".equals(token) ? 1 : 0;
			} else if (isEndTag(event)) {
				if (i == -1) throw new IllegalArgumentException();
				return i;
			} // else skip
		}
	}

	public static void enuum(XmlPullParser xml, Object object, Object key) throws  XmlPullParserException, IOException {
		PropertyInterface property = Keys.getProperty(key);
		enuum(xml, object, property);
	}
	
	public static void enuum(XmlPullParser xml, Object object, PropertyInterface property) throws  XmlPullParserException, IOException {
		boolean found = false;
		while (true) {
			int event = xml.next();
			if (isText(event)) {
				String token = xml.getText();
				enuum(token, object, property);
				found = true;
			} else if (isEndTag(event)) {
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

	public static <T extends Enum<T>> Enum<T> enuum(Class<T> enumClass, String value) {
		List<T> values = EnumUtils.valueList(enumClass);
		for (T enumValue : values) {
			EchCode echCode = (EchCode) enumValue;
			if (echCode.getValue().equals(value)) {
				return enumValue;
			}
		}
		return InvalidValues.createInvalidEnum(enumClass, value);
	}

	public static void skip(XmlPullParser xmlParser) throws XmlPullParserException, IOException  {
		while (true) {
			int event = xmlParser.next();
			if (isStartTag(event)) {
				logger.fine("Skipping XML Element: " + xmlParser.getName());
				skip(xmlParser);
			} else if (isEndTag(event)) break;
			// else ignore
		}
	}
	
	public static boolean isText(int event) {
		return event == XmlPullParser.TEXT;
	}
	
	public static boolean isStartTag(int event) {
		return event == XmlPullParser.START_TAG;
	}
	
	public static boolean isEndTag(int event) {
		return event == XmlPullParser.END_TAG;
	}
	
	public static boolean isEndDocument(int event) {
		return event == XmlPullParser.END_DOCUMENT;
	}
	
	
	
}
