package ch.openech.xml.model;

import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.frontend.page.TableDetailPage;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.model.Keys;
import org.minimalj.repository.list.SortableList;

import ch.openech.xml.model.XsdCompoundNodePage.XsdNodeRow;
import ch.openech.xml.model.XsdNode.XsdCompound;
import ch.openech.xml.model.XsdType.XsdTypeComplex;

public class XsdCompoundNodePage extends TableDetailPage<XsdNodeRow> {

	private final XsdCompound compound;
	
	public XsdCompoundNodePage(XsdCompound compound) {
		super(new Object[]{XsdNodeRow.$.clazz, XsdNodeRow.$.name, XsdNodeRow.$.type});
		this.compound = compound;
	}
	
//	@Override
//	public String getTitle() {
//		return MessageFormat.format(Resources.getString(XsdCompoundNodePage.class), schema.namespace);
//	}
	
	@Override
	protected List<XsdNodeRow> load() {
		return new SortableList<>(compound.nodes.stream().map(XsdNodeRow::new).collect(Collectors.toList()));
	}

	@Override
	protected TablePage<?> getDetailPage(XsdNodeRow row) {
		if (row.node instanceof XsdElement) {
			XsdElement element = (XsdElement) row.node;
			if (element.getType() instanceof XsdTypeComplex) {
				XsdTypeComplex typeComplex = (XsdTypeComplex) element.getType();
				XsdNode node = typeComplex.node;
				if (node instanceof XsdCompound) {
					XsdCompound compoundNode = (XsdCompound) node;
					return new XsdCompoundNodePage(compoundNode);
				}
			}
		}
		
//		if (property.propertyType.isPrimitive()) {
//			return null;
//		}
//		if (property.propertyType == MjPropertyType.ENUM) {
//			return new EnumTablePage(property.type.getClazz());
//		} else {
//			MjEntity entity = property.getModel().getEntity(property.type.getClazz());
//			return new XsdTypeTablePage(entity);
//		}
		return null;
	}

	public static class XsdNodeRow {
		public static final XsdNodeRow $ = Keys.of(XsdNodeRow.class);

		private XsdNode node;
		public String clazz;
		public String name;
		public String type;
		
		public XsdNodeRow() {
			
		}
		
		public XsdNodeRow(XsdNode node) {
			this.node = node;
			if (node instanceof XsdElement) {
				this.name = ((XsdElement) node).name;
				this.type = ((XsdElement) node).getType().toString();
			}
			this.clazz = node.getClass().getSimpleName();
		}
		
	}
	
}
