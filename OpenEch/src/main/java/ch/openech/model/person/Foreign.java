package  ch.openech.model.person;

import org.threeten.bp.LocalDate;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import  ch.openech.model.code.ResidencePermit;

public class Foreign {
	public static final Foreign FOREIGN = Keys.of(Foreign.class);
	
	public ResidencePermit residencePermit;
	public LocalDate residencePermitTill;
	
	@Size(EchFormats.baseName)
	public String nameOnPassport;

	public String toHtml() {
		// Der residencePermitDetailed - Code hat mehr Möglichkeiten als
		// der neuere "normale" Code, daher wird zum auflösen der ältere verwendet
		String codeText = EnumUtils.getText(residencePermit);
		
		String s = "";
		if (codeText != null) {
			s = s + codeText + "<BR>";
		}
		if (residencePermitTill != null) s += "Gültig bis: " + DateUtils.format(residencePermitTill) + "<BR>";
		if (!StringUtils.isBlank(nameOnPassport)) s += "Name in ausl. Pass: " + nameOnPassport;
		return s;
	}
	
	public boolean isEmpty() {
		return residencePermit == null;
	}
}
