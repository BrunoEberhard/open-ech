package ch.openech.xml.model;

import java.util.Set;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdType.GENERATION_TYPE;
import ch.openech.xml.model.XsdType.XsdTypeComplex;
import ch.openech.xml.model.XsdType.XsdTypeReference;
import ch.openech.xml.model.XsdType.XsdTypeSimple;

public class XsdElement extends XsdNode {

	@Size(255)
	public String name;
	
	public Integer minOccours = 1;
	public Integer maxOccours = 1;

	@NotEmpty
	public XsdType type;

	public XsdType getType() {
		if (type instanceof XsdTypeReference) {
			XsdTypeReference reference = (XsdTypeReference) type;
			return reference.schema.getType(reference.name);
		} else {
			return type;
		}
	}
	
	public void generateJava(StringBuilder s) {
		XsdType type = getType();
		
		boolean generateInnerClass = type.isAnonymous() && type instanceof XsdTypeComplex;
		if (generateInnerClass) {
			XsdTypeComplex typeComplex = (XsdTypeComplex) type;
			typeComplex.name = StringUtils.upperFirstChar(name);
			typeComplex.generateJava(s, GENERATION_TYPE.INNER);
		}
		
		String className = generateInnerClass ? type.className() : type.qualifiedClassName();
		
		if (maxOccours > 1) {
			if (minOccours > 0) {
				s.append("  @NotEmpty\n");
			}
			s.append("  public List<" + className + "> " + name + ";\n");
		} else {
			if (isNotEmpty()) {
				s.append("  @NotEmpty\n");
			}
			Integer size = getSize();
			if (size != null) {
				s.append("  @Size(" + size + ")\n");
			}
			
			s.append("  public " + className + " " + name + ";\n");
		}
		
		if (generateInnerClass) {
			type.name = null;
		}
	} 
	
	public Integer getSize() {
		XsdType type = getType();
		if (type instanceof XsdTypeSimple) {
			XsdTypeSimple typeSimple = (XsdTypeSimple) type;
			if (typeSimple != null) {
				return typeSimple.maxLength;
			}
		}
		return null;
	}
	
	public boolean isNotEmpty() {
		XsdType type = getType();
		if (type instanceof XsdTypeSimple) {
			XsdTypeSimple typeSimple = (XsdTypeSimple) type;
			if (typeSimple != null && typeSimple.minLength != null) {
				return typeSimple.minLength > 0;
			}
		} else {
			return minOccours > 0;
		}
		return false;
	}

	@Override
	public void getUsedTypes(Set<XsdType> usedTypes) {
		XsdType type = getType();

		if (!usedTypes.contains(type)) {
			type.getUsedTypes(usedTypes);
		}
	}
}
