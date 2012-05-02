package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.edit.value.Required;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class Foreign {
	public static final Foreign FOREIGN = Constants.of(Foreign.class);
	
	@Required
	public String residencePermit;
	
	@Date 
	public String residencePermitTill;
	
	@FormatName(EchFormats.baseName) 
	public String nameOnPassport;

	public String toHtml() {
		// Der residencePermitDetailed - Code hat mehr Möglichkeiten als
		// der neuere "normale" Code, daher wird zum auflösen der ältere verwendet
		String codeText = EchCodes.residencePermit.getText(residencePermit);
		
		String s = "<HTML>";
		if (codeText != null) {
			s = s + codeText + "<BR>";
		}
		if (!StringUtils.isBlank(residencePermitTill)) s += "Gültig bis: " + DateUtils.formatCH(residencePermitTill) + "<BR>";
		if (!StringUtils.isBlank(nameOnPassport)) s += "Name in ausl. Pass: " + nameOnPassport;
		s = s + "</HTML>";
		return s;
	}
	
	public boolean isEmpty() {
		return StringUtils.isEmpty(residencePermit);
	}
}
