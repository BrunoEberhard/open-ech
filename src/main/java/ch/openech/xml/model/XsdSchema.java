package ch.openech.xml.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

import ch.openech.xml.model.XsdType.XsdTypeJava;
import ch.openech.xml.model.XsdType.XsdTypeReference;
import ch.openech.xml.write.EchNamespaceUtil;

public class XsdSchema implements Comparable<XsdSchema> {
	public static final XsdSchema $ = Keys.of(XsdSchema.class);
	
	private final Map<String, Integer> maxMinorVersions = new HashMap<>();

	// namespace -> XsdSchema
	public final Map<String, XsdSchema> imports = new HashMap<>();

	// prefix -> namespace
	public final Map<String, String> prefixes = new HashMap<>();
	
	@Size(1024)
	public String namespace, schemaLocation;
	
	public final List<String> annotation = new ArrayList<>();
	
	public final List<XsdType> types = new ArrayList<>();
	
	public final List<XsdElement> elements = new ArrayList<>();
	
	public void print() {
		System.out.println("print: " + namespace + " from " + schemaLocation);
		int minorVersion = EchNamespaceUtil.extractSchemaMinorVersion(schemaLocation);
		if (minorVersion >= 0) {
			if (!maxMinorVersions.containsKey(namespace)) {
				maxMinorVersions.put(namespace, minorVersion);
			} else {
				int prevMinorVersion = maxMinorVersions.get(namespace);
				if (prevMinorVersion >= minorVersion) {
					return;
				}
				maxMinorVersions.put(namespace, minorVersion);
			}
		}
		
		File dir = new File("C:\\Users\\eberhard\\git\\open-ech\\src\\main\\generated");
		dir.mkdirs();
		for (XsdType type : types) {
			String java = type.generateJava();
			if (java == null) {
				continue;
			}
			File packageDir = new File(dir, packageName(namespace).replace('.', File.separatorChar));
			packageDir.mkdirs();
			File javaFile = new File(packageDir, type.className() + ".java");
			try (FileWriter filerWriter = new FileWriter(javaFile); BufferedWriter writer = new BufferedWriter(filerWriter)) {
				writer.write("package " + packageName(namespace) + ";\n\n");
				if (java.contains("@NotEmpty")) writer.write("import org.minimalj.model.annotation.NotEmpty;\n");
				if (java.contains("@Size")) writer.write("import org.minimalj.model.annotation.Size;\n");
				if (java.contains("BigDecimal")) writer.write("import java.math.BigDecimal;\n");
				if (java.contains("LocalDate")) writer.write("import java.time.LocalDate;\n");
				if (java.contains("LocalTime")) writer.write("import java.time.LocalTime;\n");
				if (java.contains("LocalDateTime")) writer.write("import java.time.LocalDateTime;\n\n");
				if (java.contains("List<")) writer.write("import java.util.List;\n\n");
				
//				Set<String> importLines = new TreeSet<>();
//				Set<XsdType> usedTypes = new TreeSet<>();
//				type.getUsedTypes(usedTypes);
//				for (XsdType usedType : usedTypes) {
//					if (usedType.schema != this) {
//						XsdSchema schema = usedType.schema;
//						if (!usedType.isJavaType() && !usedType.isAnonymous())
//						importLines.add(schema.getPackage() + "." + usedType.className());
//					}
//				}
//				for (String i : importLines) {
//					writer.write("import " + i + ";\n");
//				}
				writer.write("\n");
				
				writer.write(java);
			} catch (IOException x) {
				throw new RuntimeException(x);
			}
		}
		for (XsdSchema schema : imports.values()) {
			schema.print();
		}
	}
	
	// http://www.ech.ch/xmlns/eCH-0135/1 -> ch.ech.ech0135.v1
	public static String packageName(String namespace) {
		StringBuilder s = new StringBuilder();
		URI uri = URI.create(namespace);
		
		String host[] = uri.getHost().split("\\.");
		for (String hostElement : host) {
			if (StringUtils.equals(hostElement, "www")) continue;
			if (s.length() != 0) s.insert(0, '.');
			s.insert(0, hostElement);
		}
		
		String path[] = uri.getPath().split("/");
		for (String pathElement : path) {
			if (StringUtils.equals(pathElement, "xmlns", "")) continue;
			s.append(".");
			pathElement = pathElement.toLowerCase().replace("-", "");
			if (Character.isDigit(pathElement.charAt(0))) s.append("v");
			s.append(pathElement);
		}
		
		return s.toString();
	}
	
	public String getPackage() {
		return packageName(namespace);
	}

	public XsdType getType(String qualifiedName) {
		System.out.println("Looking for " + qualifiedName);
		String parts[] = qualifiedName.split(":");
		String namespace = this.namespace;
		String name = qualifiedName;
		if (parts.length == 2) {
			namespace = prefixes.get(parts[0]);
			name = parts[1];
		}
		if (StringUtils.equals(this.namespace, namespace)) {
			XsdTypeReference reference = null;
			for (XsdType type : types) {
				if (type instanceof XsdTypeReference) {
					reference = (XsdTypeReference) type;
					continue;
				}
				if (StringUtils.equals(type.name, name)) {
					return type;
				}
			}
			if (reference != null) {
				return reference;
			}
			return new XsdTypeReference(this, name);
		}
		if ("http://www.w3.org/2001/XMLSchema".equals(namespace)) {
			return XsdTypeJava.get(name);
		} else {
			XsdSchema schema = imports.get(namespace);
			if (schema == null) {
				throw new IllegalStateException("Not imported: " + namespace + " for " + name + " in " + this.namespace);
			}
			return schema.getType(name);
		}
	}

	@Override
	public int compareTo(XsdSchema o) {
		return schemaLocation.compareTo(o.schemaLocation);
	}
}
