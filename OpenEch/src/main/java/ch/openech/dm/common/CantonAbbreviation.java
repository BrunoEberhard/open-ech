package ch.openech.dm.common;

import java.util.List;

import ch.openech.dm.EchFormats;
import ch.openech.mj.db.model.Constants;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.model.annotation.Size;
import ch.openech.mj.util.StringUtils;
import ch.openech.xml.read.StaxEch0071;


public class CantonAbbreviation implements Validatable {
	
	public static final CantonAbbreviation CANTON_ABBREVIATION = Constants.of(CantonAbbreviation.class);
	
	@Size(EchFormats.cantonAbbreviation)
    public String canton;
	
	@Override
	public String validate() {
		if (canton == null || canton.length() < 2) {
			return "Es sind mindestens 2 Buchstaben erforderlich";
		}
		List<Canton> cantons = StaxEch0071.getInstance().getCantons();
		for (Canton canton : cantons) {
			if (StringUtils.equals(canton.cantonAbbreviation.canton, this.canton)) return null;
		}
		return "Kein gültiger Kanton";
	}

}
