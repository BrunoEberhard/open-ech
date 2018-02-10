package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.List;

import ch.openech.xml.model.XsdType.GENERATION_TYPE;
import ch.openech.xml.model.XsdType.XsdTypeReference;

public abstract class XsdNode {
	
	public static class XsdCompound extends XsdNode {

		public final List<XsdNode> nodes = new ArrayList<>();

		@Override
		public void generateJava(StringBuilder s, XsdSchema context) {
			nodes.forEach(node -> node.generateJava(s, context));
		}
	}

	public static class XsdSequence extends XsdCompound {
		
	}
	
	public static class XsdChoice extends XsdCompound {
		
	}
	
	public static class XsdModfication extends XsdNode {

		private XsdType base;
		private XsdTypeReference baseReference;
		
		public void setBase(XsdType base) {
			this.base = base;
		}
		
		public void setBaseReference(XsdTypeReference baseReference) {
			this.baseReference = baseReference;
		}
		
		public XsdType getBase() {
			if (base == null) {
				base = baseReference.dereference();
			}
			return base;
		}
		
		public XsdNode node;

		@Override
		public void generateJava(StringBuilder s, XsdSchema context) {
			getBase().generateJava(s, GENERATION_TYPE.FIELDS, context);
		}
	}

	public static class XsdRestriction extends XsdModfication {
	
	}
	
	public static class XsdExtension extends XsdModfication {
		
		public void generateJava(StringBuilder s, XsdSchema context) {
			getBase().generateJava(s, GENERATION_TYPE.FIELDS, context);
			if (node != null) {
				node.generateJava(s, context);
			} else {
				// happens for complexType with simpleContent
			}
		}
		
	}

	public abstract void generateJava(StringBuilder s, XsdSchema context);

}
