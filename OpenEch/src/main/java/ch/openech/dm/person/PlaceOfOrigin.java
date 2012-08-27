package ch.openech.dm.person;

import static ch.openech.mj.db.model.annotation.PredefinedFormat.Date;
import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Is;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class PlaceOfOrigin {

	public static final PlaceOfOrigin PLACE_OF_ORIGIN = Constants.of(PlaceOfOrigin.class);
	
	// 11: placeOfOriginType
	public String originName;
	@Is(EchFormats.cantonAbbreviation)
	public String canton;
	
	// 21:placeOfOriginAddonType
	public String reasonOfAcquisition;
	@Is(Date)
	public String naturalizationDate, expatriationDate;
	
	@Override
	public String toString() {
		String s = originName;
		if (!originEndsWithCanton()) {
			s = s + " " + canton;
		}
		return s;
	}
	
	public boolean originAndCantonNotBlank() {
		return !StringUtils.isBlank(originName) && !StringUtils.isBlank(canton);
	}
	
	public boolean originEndsWithCanton() {
		if (originAndCantonNotBlank()) {
			return originName.endsWith(canton) || originName.endsWith(canton + ")");
		}
		return false;
	}
	
	public String display() {
		StringBuilder s = new StringBuilder();
		return display(s);
	}
	
	public String display(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(canton) && !s.toString().endsWith(canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton);
		}

		boolean reasonOfAcquisitionAvailable = !StringUtils.isBlank(reasonOfAcquisition);
		if (reasonOfAcquisitionAvailable) {
			s.append("\n"); s.append("Erwerbsgrund: ");
			s.append(EchCodes.reasonOfAcquisition.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = !StringUtils.isBlank(naturalizationDate);
		if (naturalizationDateAvailable) {
			s.append("\n"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = !StringUtils.isBlank(expatriationDate);
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
		s.append("<HTML><BODY>");
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(canton) && !s.toString().endsWith(canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton);
		}

		boolean reasonOfAcquisitionAvailable = !StringUtils.isBlank(reasonOfAcquisition);
		if (reasonOfAcquisitionAvailable) {
			s.append("<BR>"); s.append("Erwerbsgrund: ");
			s.append(EchCodes.reasonOfAcquisition.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = !StringUtils.isBlank(naturalizationDate);
		if (naturalizationDateAvailable) {
			s.append("<BR>"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = !StringUtils.isBlank(expatriationDate);
		if (expatriationDateAvailable) {
			s.append("<BR>"); s.append("Entlassungsdatum: ");
			s.append(DateUtils.formatCH(expatriationDate));
		}
		
		s.append("</BODY></HTML>");
		return s.toString();
	}
	
	public void displayText(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (!StringUtils.isBlank(canton) && !s.toString().endsWith(canton)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton);
		}

		boolean reasonOfAcquisitionAvailable = !StringUtils.isBlank(reasonOfAcquisition);
		if (reasonOfAcquisitionAvailable) {
			s.append(", "); s.append("Erwerbsgrund: ");
			s.append(EchCodes.reasonOfAcquisition.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = !StringUtils.isBlank(naturalizationDate);
		if (naturalizationDateAvailable) {
			s.append(", "); s.append("Erwerbsdatum: ");
			s.append(DateUtils.formatCH(naturalizationDate));
		}

		boolean expatriationDateAvailable = !StringUtils.isBlank(expatriationDate);
		if (expatriationDateAvailable) {
			s.append(", "); s.append("Entlassungsdatum: ");
			s.append(DateUtils.formatCH(expatriationDate));
		}
	}
}
