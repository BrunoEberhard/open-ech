package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.NotEmpty;

public class XsdRestriction {

	@NotEmpty
	public String typeName;
	
	@NotEmpty
	public XsdType type;
	
	public final List<String> enumerations = new ArrayList<>();
}
