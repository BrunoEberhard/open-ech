package ch.openech.dm.organisation;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import ch.openech.dm.EchFormats;
import ch.openech.dm.common.TechnicalIds;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.annotation.Code;
import ch.openech.mj.model.annotation.Required;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.model.properties.FlatProperties;
import ch.openech.xml.read.StaxEch;

public class OrganisationIdentification {

public final UidStructure uid = new UidStructure(); 
	
	public final TechnicalIds technicalIds = new TechnicalIds();
	
	@Required @Size(EchFormats.organisationName)
	public String organisationName; 
	@Size(EchFormats.organisationName)
	public String organisationLegalName, organisationAdditionalName;
	@Code
	public String legalForm;
	
	
	public String getId() {
		if (technicalIds.localId.openEch()) {
			return technicalIds.localId.personId;
		} else {
			return null;
		}
	}
	
	public void set(String propertyName, Object value) {
		PropertyInterface property = FlatProperties.getProperties(this.getClass()).get(propertyName);
		if (property.getFieldClazz() == LocalDate.class && value instanceof String) {
			value = ISODateTimeFormat.date().parseLocalDate((String) value);
		} else if (Enum.class.isAssignableFrom(property.getFieldClazz()) && value instanceof String) {
			StaxEch.enuum((String) value, this, property);
			return;
		} 
		FlatProperties.set(this, propertyName, value);
	}
}
