package ch.openech.xml.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

public class XsdType implements Comparable<XsdType> {
	public final XsdSchema schema;
	
	public XsdType(XsdSchema schema) {
		this.schema = schema;
	}
	
	private XsdType base;
	private XsdTypeReference baseReference;
	
	public void setBase(XsdType base) {
		this.base = base;
	}
	
	public void setBaseReference(XsdTypeReference baseReference) {
		this.baseReference = baseReference;
	}
	
	private void getBase() {
		// TODO Auto-generated method stub

	}
	
	@Size(255)
	public String name;

	public String qualifiedClassName() {
		if (schema != null && !isJavaType()) {
			return XsdSchema.packageName(schema.namespace) + "." + className();
		} else {
			return className();
		}
	}
	
	public String className() {
		if (isAnonymous()) {
			return "??";
		}
		
		if (name.endsWith("Type")) {
			return StringUtils.upperFirstChar(name).substring(0, name.length() - 4);
		} else {
			return StringUtils.upperFirstChar(name);
		}
	}
	
	public boolean isAnonymous() {
		return name == null;
	}
	
	public boolean isJavaType() {
		return false;
	}
	
	@Override
	public int compareTo(XsdType o) {
		String schemaName = schema != null ? schema.namespace : "";
		String oSchemaName = o.schema != null ? o.schema.namespace : "";
		int result = schemaName.compareTo(oSchemaName);
		if (result != 0) return result;
		return className().compareTo(o.className());
	}

	public static class XsdTypeJava extends XsdType {
		
		private static Map<String, XsdTypeJava> types = new HashMap<>();
		
		static {
			XsdTypeJava INT = new XsdTypeJava(Integer.class);
			types.put("gYear", INT);
			types.put("unsignedInt", INT);
			types.put("int", INT);
			types.put("integer", INT);
		
			XsdTypeJava LONG = new XsdTypeJava(Long.class);
			types.put("unsignedLong", LONG);
			types.put("nonNegativeInteger", LONG);
			
			XsdTypeJava STRING = new XsdTypeJava(String.class);
			types.put("string", STRING);
			types.put("gYearMonth", STRING);
			types.put("normalizedString", STRING);
			types.put("token", STRING);
			types.put("anyURI", STRING);
			
			types.put("boolean", new XsdTypeJava(Boolean.class));
			types.put("decimal", new XsdTypeJava(BigDecimal.class));
			types.put("date", new XsdTypeJava(LocalDate.class));
			types.put("dateTime", new XsdTypeJava(LocalDateTime.class));
			types.put("time", new XsdTypeJava(LocalTime.class));

			types.put("anyType", new XsdTypeJava(Object.class));
		}
		
		public static XsdTypeJava get(String name) {
			return types.get(name);
		}
		
		private final Class<?> clazz;
		
		private XsdTypeJava(Class<?> clazz) {
			super(null);
			this.name = clazz.getSimpleName();
			this.clazz = clazz;
		}
		
		@Override
		public String className() {
			return clazz.getSimpleName();
		}
		
		public boolean isJavaType() {
			return true;
		}
		
		@Override
		public void generateJava(StringBuilder s, GENERATION_TYPE type) {
			// not implemented
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public String generateJava() {
		StringBuilder s = new StringBuilder();
		generateJava(s, GENERATION_TYPE.MAIN);
		return s.toString();
	}
	
	public enum GENERATION_TYPE {
		MAIN, INNER, FIELDS;
	}
	
	public void generateJava(StringBuilder s, GENERATION_TYPE type) {}
	
	public static class XsdTypeSimple extends XsdType {

		private String minInclusive, maxInclusive;
		public Integer minLength, maxLength;
		public final Set<String> enumeration = new TreeSet<>();
		
		public void setMinInclusive(String minInclusive) {
			this.minInclusive = minInclusive;
		}
		
		public void setMaxInclusive(String maxInclusive) {
			this.maxInclusive = maxInclusive;
		}
				
		public XsdTypeSimple(XsdSchema schema) {
			super(schema);
		}

		@Override
		public String className() {
			String className = _className();
			if (className.endsWith("Type")) className = className.substring(0, className.length() - 4);
			return className;
		}
		
		private String _className() {
			if (base != null) {
				return base.dereference().className();
			} else if (enumeration.size() > 1 && name != null) {
				return StringUtils.upperFirstChar(name);
			} else {
				return "!!!";
			}
		}
		
		public boolean isEnumeration() {
			if (enumeration.size() > 1) {
				return true;
			}
			if (base.dereference() instanceof XsdTypeSimple) {
				return ((XsdTypeSimple) base.dereference()).isEnumeration();
			}
			return false;
		}
		
		@Override
		public boolean isJavaType() {
			if (base != null) {
				return base.dereference().isJavaType();
			} else {
				return !isEnumeration();
			}
		}
		
		public String generateJava() {
			if (isEnumeration()) {
				StringBuilder s = new StringBuilder();
				s.append("public enum " + className() + " {\n  ");
				
				boolean startsWithDigit = false;
				
				for (String element : enumeration) {
					startsWithDigit |= Character.isDigit(element.charAt(0));
				}

				boolean first = true;
				for (String element : enumeration) {
					if (!first) s.append(", ");
					first = false;
					if (startsWithDigit) s.append("_");
					element = element.replaceAll("\\-", "\\_");
					element = StringUtils.toSnakeCase(element);
					if (element.equals("public")) element = "_public";
					s.append(element);
				}
				
				s.append(";\n}");
				return s.toString();
			} else {
				return null;
			}
		}
		
		@Override
		public void generateJava(StringBuilder s, GENERATION_TYPE type) {
			// not implemented
		}
		
		public String getDescription() {
			StringBuilder s = new StringBuilder();
			if (base != null)
				s.append(base.name).append(", ");
			if (minLength != null) {
				s.append("minLength = ").append(minLength).append(", ");
			}
			if (maxLength != null) {
				s.append("maxLength = ").append(maxLength).append(", ");
			}
			if (minInclusive != null) {
				s.append("minInclusive = ").append(minInclusive).append(", ");
			}
			if (maxInclusive != null) {
				s.append("maxInclusive = ").append(maxInclusive).append(", ");
			}
			if (!enumeration.isEmpty()) {
				s.append("enum = {");
				enumeration.forEach(e -> s.append(e).append(", "));
				s.delete(s.length() - 2, s.length());
				s.append("}, ");
			}
			return s.substring(0, s.length() - 2);
		}
		
		@Override
		public String toString() {
			return name + ": " + getDescription();
		}
	}
	
	public static class XsdTypeComplex extends XsdType {

		public XsdTypeComplex(XsdSchema schema) {
			super(schema);
		}

		public XsdNode node;
			
		public void generateJava(StringBuilder s, GENERATION_TYPE type) {
			System.out.println(className());
			switch (type) {
			case MAIN:
				s.append("public class " + className() + " {\n\n");
				break;
			case INNER:
				s.append("\npublic static class " + className() + " {\n");
				break;
			default:
				break;
			}
			node.generateJava(s);
			switch (type) {
			case MAIN:
				s.append("}");
				break;
			case INNER:
				s.append("}\n");
				break;
			default:
				break;
			}
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	public static class XsdTypeReference {
		private final XsdSchema schema;
		private final String name;
		
		public XsdTypeReference(XsdSchema schema, String name) {
			this.schema = schema;
			this.name = name;
		}
		
		public XsdType dereference() {
			return schema.getType(name);
		}

//		@Override
//		public void generateJava(StringBuilder s, GENERATION_TYPE type) {
//			// not implemented
//		}
//		
//		@Override
//		public String className() {
//			XsdType realType = schema.getType(name);
//			if (realType == this) {
//				System.out.println("Not resolvable: " + name);
//				return "Shit";
//			}
//			return schema.getType(name).className();
//		}
	}
}
