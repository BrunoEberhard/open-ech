package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;

import java.util.List;

import ch.openech.dm.XmlConstants;
import ch.openech.dm.common.NamedId;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.mj.util.StringUtils;

public class WriterEch0044 {

	public final String URI;
	
	public WriterEch0044(EchSchema context) {
		URI = context.getNamespaceURI(44);
	}

	public void personIdentification(WriterElement parent, PersonIdentification values) throws Exception {
		personIdentification(parent, XmlConstants.PERSON_IDENTIFICATION, values);
	}
	
	public void personIdentification(WriterElement parent, String tagName, PersonIdentification values) throws Exception {
		WriterElement personIdentification = parent.create(URI, tagName);

		personIdentification.text(XmlConstants.VN, values.vn);
		namedId(personIdentification, values.technicalIds.localId, XmlConstants.LOCAL_PERSON_ID);
    	NamedId(personIdentification, values.technicalIds.otherId, _OTHER_PERSON_ID); // VERSION
    	NamedId(personIdentification, values.technicalIds.euId, "EuPersonId"); // VERSION
		personIdentification.values(values, OFFICIAL_NAME, FIRST_NAME);
		personIdentification.text(XmlConstants.SEX, values.sex);
    	datePartiallyKnownType(personIdentification, DATE_OF_BIRTH, values);
    }

	public void datePartiallyKnownType(WriterElement parent, String tagName, Object object) throws Exception {
		datePartiallyKnownType(parent, URI, tagName, object);
	}

	// Diese Methode wird auch von e98 benutzt, der ComplexType ist dort
	// dupliziert worden
	public static void datePartiallyKnownType(WriterElement parent, String uri, String tagName, Object object) throws Exception {
		Object value = FlatProperties.getValue(object, tagName);
		if (value == null) return;
		String text = value.toString();
		if (StringUtils.isBlank(text)) return;

		WriterElement date = parent.create(uri, tagName);

    	String typeName = YEAR_MONTH_DAY;
    	if (text.length() < 10) {
    		typeName = YEAR_MONTH;
    	}
    	if (text.length() < 7) {
    		typeName = YEAR;
    	}

    	date.text(typeName, text);
	}
	
	private void NamedId(WriterElement parent, List<NamedId> namedIds, String name) throws Exception {
		for (NamedId namedPersonId : namedIds) {
			namedId(parent, namedPersonId, name);
		}
	}
	
	public void namedId(WriterElement parent, NamedId namedId, String name) throws Exception {
		WriterElement personIdentification = parent.create(URI, name);
		
		personIdentification.text(PERSON_ID_CATEGORY, namedId.personIdCategory);
		personIdentification.text(PERSON_ID, namedId.personId);
	}

}
