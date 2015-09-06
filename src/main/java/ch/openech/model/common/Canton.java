package  ch.openech.model.common;

import java.util.Locale;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.model.EchFormats;

public class Canton implements Code, Rendering, Comparable<Canton> {

	public static final Canton $ = Keys.of(Canton.class);

	public Canton() {
	}
	
	public Canton(String id) {
		this.id = id;
	}
	
	@Size(EchFormats.cantonAbbreviation)
    public String id;
	
	@Size(60)
	public String cantonLongName;
	
	@Override
	public int compareTo(Canton c) {
		// Used in ComboBox
		return StringUtils.compare(id, c.id);
	}

	@Override
	public String render(RenderType renderType) {
		return id;
	}

	// hash / equals. This is important

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Canton))
			return false;
		
		Canton other = (Canton) obj;
		
		// if id is the same other values don't matter
		if (id != null && other.id != null) {
			return id.equals(other.id);
		}
		
		return false;
	}

//	@Override
//	public String validate() {
//		if (canton == null || canton.length() < 2) {
//			return "Es sind mindestens 2 Buchstaben erforderlich";
//		}
//		List<Canton> cantons = StaxEch0071.getInstance().getCantons();
//		for (Canton canton : cantons) {
//			if (StringUtils.equals(canton.canton.cantonAbbreviation, this.canton)) return null;
//		}
//		return "Kein gültiger Kanton";
//	}

}
