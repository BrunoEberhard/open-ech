package ch.openech.dm.person;

import org.joda.time.LocalDate;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.ResidencePermit;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.Keys;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

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
		if (residencePermitTill != null) s += "Gültig bis: " + DateUtils.formatCH(residencePermitTill) + "<BR>";
		if (!StringUtils.isBlank(nameOnPassport)) s += "Name in ausl. Pass: " + nameOnPassport;
		return s;
	}
	
	public boolean isEmpty() {
		return residencePermit == null;
	}
}
