package  ch.openech.model.common;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.StringUtils;

import  ch.openech.model.EchFormats;
import ch.openech.xml.read.StaxEch0071;


public class CantonAbbreviation implements Validatable {
	
	public static final CantonAbbreviation CANTON_ABBREVIATION = Keys.of(CantonAbbreviation.class);
	
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
