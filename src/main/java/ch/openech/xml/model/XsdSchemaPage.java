package ch.openech.xml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.minimalj.frontend.page.TableDetailPage;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.repository.list.SortableList;

public class XsdSchemaPage extends TableDetailPage<XsdSchema> {

	private final XsdReader reader;

	public XsdSchemaPage() {
		super(new Object[] {XsdSchema.$.namespace, XsdSchema.$.schemaLocation});
		reader = new XsdReader();
	}
	
	@Override
	protected TablePage<?> getDetailPage(XsdSchema schema) {
		return new XsdTypeTablePage(schema);
	}

	@Override
	protected List<XsdSchema> load() {
		XsdSchema schema;
		try {
			schema = reader.read("http://www.ech.ch/xmlns/eCH-0211/1/eCH-0211-1-0.xsd");
			return new SortableList<>(new ArrayList<>(collectSchemas(schema)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Collection<XsdSchema> collectSchemas(XsdSchema schema) {
		Set<XsdSchema> schemas = new TreeSet<>();
		schemas.add(schema);
		schema.imports.values().forEach(s -> schemas.addAll(collectSchemas(s)));
		return schemas;
	}

}
