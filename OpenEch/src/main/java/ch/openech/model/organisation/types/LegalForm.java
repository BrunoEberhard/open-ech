package ch.openech.model.organisation.types;

import org.minimalj.model.annotation.Code;
import org.minimalj.model.annotation.Size;

public class LegalForm {
	
	@Code @Size(4)
	public String code;

	@Size(255)
	public String text;
	
	@Size(255)
	public String description;

}
