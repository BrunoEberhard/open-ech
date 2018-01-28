package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.Size;

public class XsdType {

	@Size(255)
	public String name;
	
	public XsdRestriction restriction;
	
	public Long minInclusive, maxInclusive;
	public Integer minLength, maxLength;
	public final List<String> enumeration = new ArrayList<>();
	
//	public static class XsdTypeStringEnum extends XsdType {
//		
//		public final List<String> values = new ArrayList<>();
//		
//	}
//
//	public static class XsdTypeIntegerEnum extends XsdType {
//		
//		public final List<Integer> values = new ArrayList<>();
//		
//	}
//	
//	public static class XsdTypeString extends XsdType {
//		
//		public Integer minLength, maxLength;
//
//	}
//	
//	public static class XsdTypeInteger extends XsdType {
//		
//		public Integer minInclusive, maxInclusive;
//
//	}

	public static class XsdTypeComplex extends XsdType {

		public final List<XsdElement> elements = new ArrayList<>();

	}

}
