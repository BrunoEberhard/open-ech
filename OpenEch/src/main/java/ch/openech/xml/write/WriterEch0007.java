package ch.openech.xml.write;

import static ch.openech.dm.XmlConstants.*;

import ch.openech.dm.common.MunicipalityIdentification;

public class WriterEch0007 {

	public final String URI;
	public final boolean HAS_TYPO;
	
	public WriterEch0007(EchNamespaceContext context) {
		URI = context.getNamespaceURI(7);
		HAS_TYPO = EchNamespaceUtil.extractSchemaMajorVersion(URI) < 4;
	}
	
	public void municipality(WriterElement parent, String tagName, MunicipalityIdentification municipalityIdentification) throws Exception {
		if (municipalityIdentification == null || municipalityIdentification.isEmpty()) return;

		WriterElement writer = parent.create(URI, tagName);
		
		if (HAS_TYPO) {
			// im XML Schmema eCH-0007-3-0 heisst ein Attribut
			// historyMunicipialityId statt
			// historyMunicipalityId
	    	writer.text(MUNICIPALITY_ID, municipalityIdentification.municipalityId);
			writer.text(MUNICIPALITY_NAME, municipalityIdentification.municipalityName);
			writer.text(CANTON_ABBREVIATION, municipalityIdentification.cantonAbbreviation);
			writer.text(HISTORY_MUNICIPIALITY_ID, municipalityIdentification.historyMunicipalityId); // tagName!!!!!
		} else {
			// in Version 4.0 ist das dann korrigiert ;)
	    	writer.values(municipalityIdentification);
		}
	}
	
}