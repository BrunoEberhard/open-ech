package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.h2.util.StringUtils;
import org.minimalj.model.annotation.Size;

import ch.openech.xml.model.XsdType.XsdTypeComplex;

public class XsdSchema {

	// eCH-0046 -> XsdSchema
	public final Map<String, XsdSchema> imports = new HashMap<>();
	
	@Size(1024)
	public String namespace, schemaLocation;
	
	public final List<String> annotation = new ArrayList<>();
	
	public final List<XsdType> types = new ArrayList<>();
	
	
	public XsdType getType(String qualifedType) {
		String[] parts = qualifedType.split(":");
		String localName;
		if (parts.length == 2) {
			XsdSchema schema = imports.get(parts[0]);
			localName = parts[1];
			if (schema != null) {
				return schema.getType(localName);
			}
		} else {
			localName = parts[0];
		}
		for (XsdType type : types) {
			if (StringUtils.equals(type.name, localName)) {
				return type;
			}
		}
		throw new IllegalArgumentException(qualifedType);
	}
	
	public void print() {
		System.out.println(namespace);
		for (XsdType type : types) {
			if (type.enumeration.isEmpty()) {
				System.out.println(type.name);
			} else {
				System.out.print(type.name + ": ");
				for (String e: type.enumeration) {
					System.out.print(e + ", ");
				}
				System.out.println();
			}
			if (type instanceof XsdTypeComplex) {
				XsdTypeComplex complextType = (XsdTypeComplex) type;
				for (XsdElement element : complextType.elements) {
					System.out.println("- " + element.name + " / " + element.typeName);
				}
			}
		}
		for (XsdSchema schema : imports.values()) {
			schema.print();
		}
	}
}
