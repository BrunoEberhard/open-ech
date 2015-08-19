package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;

import java.util.List;

import org.minimalj.util.IdUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.XmlConstants;
import ch.openech.model.common.DatePartiallyKnown;
import ch.openech.model.common.NamedId;
import ch.openech.model.person.PersonIdentification;

public class WriterEch0044 {

	public final String URI;
	private final int lengthOfPersonId;
	
	public WriterEch0044(EchSchema schema) {
		URI = schema.getNamespaceURI(44);
		
		lengthOfPersonId = schema.lengthOfPersonId();
	}

	public void personIdentification(WriterElement parent, PersonIdentification values) throws Exception {
		personIdentification(parent, XmlConstants.PERSON_IDENTIFICATION, values);
	}
	
	public void personIdentification(WriterElement parent, String tagName, PersonIdentification values) throws Exception {
		WriterElement personIdentification = parent.create(URI, tagName);

		personIdentification.textIfSet(XmlConstants.VN, values.vn.value);
		// namedId(personIdentification, values.technicalIds.localId, XmlConstants.LOCAL_PERSON_ID);
		// hier wird die interne id verwendet
		localID(personIdentification, IdUtils.getCompactIdString(values));
    	NamedId(personIdentification, values.technicalIds.otherId, _OTHER_PERSON_ID); // VERSION
    	NamedId(personIdentification, values.technicalIds.euId, "EuPersonId"); // VERSION
		personIdentification.values(values, OFFICIAL_NAME, FIRST_NAME);
		personIdentification.text(XmlConstants.SEX, values.sex);
    	datePartiallyKnownType(personIdentification, DATE_OF_BIRTH, values.dateOfBirth);
    }

	public void datePartiallyKnownType(WriterElement parent, String tagName, DatePartiallyKnown object) throws Exception {
		datePartiallyKnownType(parent, URI, tagName, object);
	}

	// Diese Methode wird auch von e98 benutzt, der ComplexType ist dort
	// dupliziert worden
	public static void datePartiallyKnownType(WriterElement parent, String uri, String tagName, DatePartiallyKnown object) throws Exception {
		Object value = object.value;
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
	
	public void localID(WriterElement parent, String id) throws Exception {
		if (id == null) return;
		if (id.length() > lengthOfPersonId) {
			id = id.substring(0, lengthOfPersonId);
		}
		
		WriterElement personIdentification = parent.create(URI, LOCAL_PERSON_ID);
		
		personIdentification.text(PERSON_ID_CATEGORY, NamedId.OPEN_ECH_ID_CATEGORY);
		personIdentification.text(PERSON_ID, id);
	}
	
	public void namedId(WriterElement parent, NamedId namedId, String name) throws Exception {
		WriterElement personIdentification = parent.create(URI, name);
		
		personIdentification.text(PERSON_ID_CATEGORY, namedId.personIdCategory);
		personIdentification.text(PERSON_ID, namedId.personId);
	}

}
