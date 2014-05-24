package  ch.openech.model.person;

import org.joda.time.LocalDate;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import  ch.openech.model.common.CantonAbbreviation;
import  ch.openech.model.person.types.ReasonOfAcquisition;

public class PlaceOfOrigin {

	public static final PlaceOfOrigin PLACE_OF_ORIGIN = Keys.of(PlaceOfOrigin.class);
	
	// 11: placeOfOriginType
	@Required @Size(EchFormats.municipalityName)
	public String originName;
	public final CantonAbbreviation cantonAbbreviation = new CantonAbbreviation();
	
	// 21:placeOfOriginAddonType
	public ReasonOfAcquisition reasonOfAcquisition;
	public LocalDate naturalizationDate, expatriationDate;
	
	@Override
	public String toString() {
		String s = originName;
		if (!originEndsWithCanton()) {
			s = s + " " + cantonAbbreviation.canton;
		}
		return s;
	}
	
	public boolean originAndCantonNotBlank() {
		return !StringUtils.isBlank(originName) && !StringUtils.isBlank(cantonAbbreviation.canton);
	}
	
	public boolean originEndsWithCanton() {
		if (originAndCantonNotBlank()) {
			return originName.endsWith(cantonAbbreviation.canton) || originName.endsWith(cantonAbbreviation.canton + ")");
		}
		return false;
	}
	
	public String display() {
		StringBuilder s = new StringBuilder();
		return display(s);
	}
	
	public String display(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(cantonAbbreviation.canton) && !s.toString().endsWith(cantonAbbreviation.canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(cantonAbbreviation.canton);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append("\n"); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append("\n"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append("\n"); s.append("Entlassungsdatum: ");
			s.append(DateUtils.formatCH(expatriationDate));
		}
		
		return s.toString();
	}
	
	public String displayHtml() {
		StringBuilder s = new StringBuilder();
		return displayHtml(s);
	}
	
	public String displayHtml(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(cantonAbbreviation.canton) && !s.toString().endsWith(cantonAbbreviation.canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(cantonAbbreviation.canton);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append("<BR>"); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append("<BR>"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append("<BR>"); s.append("Entlassungsdatum: ");
			s.append(DateUtils.formatCH(expatriationDate));
		}
		
		return s.toString();
	}
	
	public void displayText(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(cantonAbbreviation.canton) && !s.toString().endsWith(cantonAbbreviation.canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(cantonAbbreviation.canton);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append(", "); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append(", "); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append(", "); s.append("Entlassungsdatum: ");
			s.append(DateUtils.formatCH(expatriationDate));
		}
	}
	
}