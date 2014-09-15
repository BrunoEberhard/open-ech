package ch.openech.model.organisation.types;

import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Size;

public class LegalForm implements Code {
	
	@Size(4)
	public String id;

	@Size(255)
	public String text;
	
	@Size(255)
	public String description;

	@Override
//	public String getText(Locale local) {
	public String display() {
		return text;
	}
	
	// hash / equals. This is important

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else if (text != null) {
			return text.hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LegalForm))
			return false;
		
		LegalForm other = (LegalForm) obj;
		
		// if id is the same other values don't matter
		if (id != null && other.id != null) {
			return id.equals(other.id);
		}
		
		if (text != null && other.text != null) {
			return text.equals(other.text);
		}
		
		return false;
	}

}
