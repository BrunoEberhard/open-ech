package ch.openech.xml.model;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.minimalj.frontend.page.TableDetailPage;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.model.Keys;
import org.minimalj.repository.list.SortableList;
import org.minimalj.util.resources.Resources;

import ch.openech.xml.model.XsdNode.XsdCompound;
import ch.openech.xml.model.XsdType.XsdTypeComplex;
import ch.openech.xml.model.XsdType.XsdTypeSimple;
import ch.openech.xml.model.XsdTypeTablePage.XsdTypeRow;

public class XsdTypeTablePage extends TableDetailPage<XsdTypeRow> {

	private final XsdSchema schema;

	public XsdTypeTablePage(XsdSchema schema) {
		super(new Object[] { XsdTypeRow.$.name, XsdTypeRow.$.clazz, XsdTypeRow.$.description, XsdTypeRow.$.isJava, XsdTypeRow.$.className});
		this.schema = schema;
	}

	@Override
	public String getTitle() {
		return MessageFormat.format(Resources.getString(XsdTypeTablePage.class), schema.namespace);
	}

	@Override
	protected List<XsdTypeRow> load() {
		return new SortableList<>(schema.types.stream().map(XsdTypeRow::new).collect(Collectors.toList()));
	}

	@Override
	protected TablePage<?> getDetailPage(XsdTypeRow row) {
		if (row.type instanceof XsdTypeComplex) {
			XsdTypeComplex typeComplex = (XsdTypeComplex) row.type;
			XsdNode node = typeComplex.node;
			if (node instanceof XsdCompound) {
				XsdCompound compoundNode = (XsdCompound) node;
				return new XsdCompoundNodePage(compoundNode);
			}
		}

		// if (property.propertyType.isPrimitive()) {
		// return null;
		// }
		// if (property.propertyType == MjPropertyType.ENUM) {
		// return new EnumTablePage(property.type.getClazz());
		// } else {
		// MjEntity entity = property.getModel().getEntity(property.type.getClazz());
		// return new XsdTypeTablePage(entity);
		// }
		return null;
	}

	public static class XsdTypeRow {
		public static final XsdTypeRow $ = Keys.of(XsdTypeRow.class);

		private XsdType type;
		public String clazz;
		public String name;
		public String description;
		public String isJava;
		public String className;

		public XsdTypeRow() {

		}

		public XsdTypeRow(XsdType type) {
			this.type = type;
			this.name = type.name;
			this.clazz = type.getClass().getSimpleName();
			if (type instanceof XsdTypeSimple) {
				this.description = ((XsdTypeSimple) type).getDescription();
			} else {
				this.description = type.getBase() != null ? type.getBase().name : "";
			}
			if (type.isJavaType()) {
				this.isJava = "Java";
				if (type.getBase() != null) {
					this.isJava = this.isJava + " " + type.getBase().name;
				}
			}
			this.className = type.className();
		}

	}

}
