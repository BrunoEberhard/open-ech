package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdType.XsdTypeComplex;

public class XsdSchema {

	// eCH-0046 -> XsdSchema
	public final Map<String, XsdSchema> imports = new HashMap<>();
	
	@Size(1024)
	public String namespace, schemaLocation;
	
	public final List<String> annotation = new ArrayList<>();
	
	public final List<XsdType> types = new ArrayList<>();
	
	
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
					System.out.println("- " + element.name + " / " + (element.type != null ? element.type.getJavaType() : element.typeName));
				}
			}
		}
		for (XsdSchema schema : imports.values()) {
			schema.print();
		}
	}

	public void setTypes(Map<String, XsdSchema> schemas) {
		for (XsdType type : types) {
			if (type instanceof XsdTypeComplex) {
				XsdTypeComplex typeComplex = (XsdTypeComplex) type;
				for (XsdElement element : typeComplex.elements) {
					System.out.println(element.typeNamespace + " - " + element.typeName);
					if (element.typeNamespace == null) {
						element.type = getType(element.typeName);
					} else {
						XsdSchema schema = schemas.get(element.typeNamespace);
						if (schema != null) {
							element.type = schema.getType(element.typeName);
						} else {
							System.out.println("Not found: " + element.typeName);
						}
					}
				}
			}
		}
	}
	
	private XsdType getType(String name) {
		for (XsdType type : types) {
			if (StringUtils.equals(type.name, name)) {
				return type;
			}
		}
		return null;
	}
}
