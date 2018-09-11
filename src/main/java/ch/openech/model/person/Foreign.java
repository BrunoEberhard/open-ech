package  ch.openech.model.person;

import java.time.LocalDate;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.code.ResidencePermit;

public class Foreign implements Rendering {
	public static final Foreign $ = Keys.of(Foreign.class);
	
	public ResidencePermit residencePermit;
	public LocalDate residencePermitTill;
	
	@Size(EchFormats.baseName)
	public String nameOnPassport;
	
	@Override
	public CharSequence render() {
		// Der residencePermitDetailed - Code hat mehr Möglichkeiten als
		// der neuere "normale" Code, daher wird zum auflösen der ältere verwendet
		StringBuilder s = new StringBuilder();
		StringUtils.appendLine(s, EnumUtils.getText(residencePermit));
		if (residencePermitTill != null) {
			StringUtils.appendLine(s, "Gültig bis: " + DateUtils.format(residencePermitTill));
		}
		if (!StringUtils.isBlank(nameOnPassport)) {
			StringUtils.appendLine(s, "Name in ausl. Pass: " + nameOnPassport);
		}
		return s;
	}
	
	public boolean isEmpty() {
		return residencePermit == null;
	}
}
