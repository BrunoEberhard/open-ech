package ch.openech.xml.frontend;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.page.TableDetailPage;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.metamodel.page.EntityTablePage;
import org.minimalj.repository.list.SortableList;

import ch.openech.xml.EchSchemas;
import ch.openech.xml.XsdModel;

public class XsdModelTablePage extends TableDetailPage<XsdModel> {

	public XsdModelTablePage() {
		super(new Object[]{XsdModel.$.getNamespace()});
	}
	
	@Override
	protected TablePage<?> getDetailPage(XsdModel model) {
		return new EntityTablePage(new ArrayList<>(model.getEntities()));
	}

	@Override
	protected List<XsdModel> load() {
		return new SortableList<XsdModel>(EchSchemas.getXsdModels());
	}

}
