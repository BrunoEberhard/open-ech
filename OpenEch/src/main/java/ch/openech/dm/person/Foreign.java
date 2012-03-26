package ch.openech.dm.person;

import ch.openech.dm.EchFormats;
import ch.openech.dm.code.EchCodes;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.db.model.annotation.Date;
import ch.openech.mj.db.model.annotation.FormatName;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

public class Foreign {
	public static final Foreign FOREIGN = Constants.of(Foreign.class);
	
	public String residencePermit;
	
	@Date 
	public String residencePermitTill;
	
	@FormatName(EchFormats.baseName) 
	public String nameOnPassport;

	public String toHtml() {
		// Der residencePermitDetailed - Code hat mehr Möglichkeiten als
		// der neuere "normale" Code, daher wird zum auflösen der ältere verwendet
		String codeText = EchCodes.residencePermitDetailed.getText(residencePermit);
		
		String s = "<HTML>";
		if (codeText != null) {
			s = s + "Ausländerkategorie<BR>" + codeText + "<BR>&nbsp;<BR>";
		}
		if (!StringUtils.isBlank(residencePermitTill)) s += "Ausländerkategorie gültig bis<BR>" + DateUtils.formatCH(residencePermitTill) + "<BR>&nbsp;<BR>";
		if (!StringUtils.isBlank(nameOnPassport)) s += "Name in ausländischem Pass<BR>" + nameOnPassport;
		s = s + "</HTML>";
		return s;
	}
	
//	@Override
//	public Foreign clone() {
//		Foreign clone = new Foreign();
//		clone.residencePermit = this.residencePermit;
//		clone.residencePermitTill = this.residencePermitTill;
//		clone.nameOnPassport = this.nameOnPassport;
//		return clone;
//	}
}
