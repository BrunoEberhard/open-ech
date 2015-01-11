package  ch.openech.model.person;

import java.time.LocalDate;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Canton;
import ch.openech.model.person.types.ReasonOfAcquisition;

public class PlaceOfOrigin {

	public static final PlaceOfOrigin $ = Keys.of(PlaceOfOrigin.class);
	
	// 11: placeOfOriginType
	@Required @Size(EchFormats.municipalityName)
	public String originName;
	public Canton canton;
	
	// 21:placeOfOriginAddonType
	public ReasonOfAcquisition reasonOfAcquisition;
	public LocalDate naturalizationDate, expatriationDate;
	
	@Override
	public String toString() {
		String s = originName;
		if (!originEndsWithCanton()) {
			s = s + " " + canton.id;
		}
		return s;
	}
	
	public boolean originAndCantonNotBlank() {
		return !StringUtils.isBlank(originName) && canton != null;
	}
	
	public boolean originEndsWithCanton() {
		if (originAndCantonNotBlank()) {
			return originName.endsWith(canton.id) || originName.endsWith(canton.id + ")");
		}
		return false;
	}
	
	public String display() {
		StringBuilder s = new StringBuilder();
		return display(s);
	}
	
	public String display(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (canton != null && !StringUtils.isBlank(canton.id) && !s.toString().endsWith(canton.id)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton.id);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append("\n"); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append("\n"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.format(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append("\n"); s.append("Entlassungsdatum: ");
			s.append(DateUtils.format(expatriationDate));
		}
		
		return s.toString();
	}
	
	public String displayHtml() {
		StringBuilder s = new StringBuilder();
		return displayHtml(s);
	}
	
	public String displayHtml(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (canton != null && !StringUtils.isBlank(canton.id) && !s.toString().endsWith(canton.id)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton.id);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append("<BR>"); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append("<BR>"); s.append("Erwerbsdatum: ");
			s.append(DateUtils.format(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append("<BR>"); s.append("Entlassungsdatum: ");
			s.append(DateUtils.format(expatriationDate));
		}
		
		return s.toString();
	}
	
	public void displayText(StringBuilder s) {
		if (!StringUtils.isBlank(originName)) s.append(originName);
		if (canton != null && !StringUtils.isBlank(canton.id) && !s.toString().endsWith(canton.id)) {
			if (s.length() > 0) s.append(' ');
			s.append(canton.id);
		}

		boolean reasonOfAcquisitionAvailable = reasonOfAcquisition != null;
		if (reasonOfAcquisitionAvailable) {
			s.append(", "); s.append("Erwerbsgrund: ");
			s.append(EnumUtils.getText(reasonOfAcquisition));
		}

		boolean naturalizationDateAvailable = naturalizationDate != null;
		if (naturalizationDateAvailable) {
			s.append(", "); s.append("Erwerbsdatum: ");
			s.append(DateUtils.format(naturalizationDate));
		}

		boolean expatriationDateAvailable = expatriationDate != null;
		if (expatriationDateAvailable) {
			s.append(", "); s.append("Entlassungsdatum: ");
			s.append(DateUtils.format(expatriationDate));
		}
	}
	
}
