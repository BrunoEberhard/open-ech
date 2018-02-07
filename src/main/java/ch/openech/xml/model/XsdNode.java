package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.List;

import ch.openech.xml.model.XsdType.GENERATION_TYPE;

public abstract class XsdNode {
	
	public static class XsdCompound extends XsdNode {

		public final List<XsdNode> nodes = new ArrayList<>();

		public void generateJava(StringBuilder s) {
			nodes.forEach(node -> node.generateJava(s));
		}
	}

	public static class XsdSequence extends XsdCompound {
		
	}
	
	public static class XsdChoice extends XsdCompound {
		
	}
	
	public static class XsdModfication extends XsdNode {
		
		public XsdType base;
		public XsdNode node;

		public void generateJava(StringBuilder s) {
			base.generateJava(s, GENERATION_TYPE.FIELDS);
		}
	}

	public static class XsdRestriction extends XsdModfication {
	
	}
	
	public static class XsdExtension extends XsdModfication {
		
		public void generateJava(StringBuilder s) {
			base.generateJava(s, GENERATION_TYPE.FIELDS);
			if (node != null) {
				node.generateJava(s);
			} else {
				// happens for complexType with simpleContent
			}
		}
		
	}

	public abstract void generateJava(StringBuilder s);

}
