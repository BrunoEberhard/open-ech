package ch.openech.xml.model;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class XsdElement {

	@Size(255)
	public String name;
	
	public Integer minOccours = 1;
	public Integer maxOccours = 1;

	@NotEmpty
	public String typeName;
	
	@NotEmpty
	public XsdType type;
}
