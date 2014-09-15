package ch.openech.xml.write;

import static ch.openech.model.XmlConstants.*;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.person.SecondaryResidence;

public class WriterEch0007 {

	public final String URI;
	public final boolean HAS_TYPO;
	
	public WriterEch0007(EchSchema context) {
		URI = context.getNamespaceURI(7);
		HAS_TYPO = EchNamespaceUtil.extractSchemaMajorVersion(URI) < 4;
	}
	
	public void municipality(WriterElement parent, String tagName, MunicipalityIdentification municipalityIdentification) throws Exception {
		if (municipalityIdentification == null || municipalityIdentification.isEmpty()) return;

		WriterElement writer = parent.create(URI, tagName);
		
		writer.text(MUNICIPALITY_ID, municipalityIdentification.id);
		writer.text(MUNICIPALITY_NAME, municipalityIdentification.municipalityName);
		writer.text(CANTON_ABBREVIATION, municipalityIdentification.canton.id);
		if (HAS_TYPO) {
			// im XML Schmema eCH-0007-3-0 heisst ein Attribut
			// historyMunicipialityId statt
			// historyMunicipalityId
			writer.text(HISTORY_MUNICIPIALITY_ID, municipalityIdentification.historyMunicipalityId); // tagName!!!!!
		} else {
			// in Version 4.0 ist das dann korrigiert ;)
			writer.text(HISTORY_MUNICIPALITY_ID, municipalityIdentification.historyMunicipalityId); // tagName!!!!!
		}
	}

	public void municipality(WriterElement parent, String tagName, SecondaryResidence secondaryResidence) throws Exception {
		if (secondaryResidence == null || secondaryResidence.municipalityIdentification == null) return;
		municipality(parent, tagName, secondaryResidence.municipalityIdentification);
	}
	
}
