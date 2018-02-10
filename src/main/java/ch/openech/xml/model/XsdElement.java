package ch.openech.xml.model;

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

	private XsdType type;
	private XsdTypeReference typeReference;

	public void setType(XsdType type) {
		this.type = type;
	}
	
	public void setTypeReference(XsdTypeReference typeReference) {
		this.typeReference = typeReference;
	}
	
	public XsdType getType() {
		if (type == null) {
			type = typeReference.dereference();
		}
		return type;
	}
	
	@Override
	public void generateJava(StringBuilder s, XsdSchema context) {
		XsdType type = getType();
		
		boolean generateInnerClass = type.isAnonymous() && type instanceof XsdTypeComplex;
		if (generateInnerClass) {
			XsdTypeComplex typeComplex = (XsdTypeComplex) type;
			typeComplex.name = StringUtils.upperFirstChar(name);
			typeComplex.generateJava(s, GENERATION_TYPE.INNER, context);
		}
		
		String className = generateInnerClass || type.schema == context ? type.className() : type.qualifiedClassName();
		
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
}
