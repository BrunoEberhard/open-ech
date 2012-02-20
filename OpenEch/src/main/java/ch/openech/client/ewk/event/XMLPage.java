package ch.openech.client.ewk.event;


import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.toolkit.IComponent;

public class XMLPage extends Page {
	public final String xml;
	
	public XMLPage(PageContext context, String xml) {
		super(context);
		this.xml = xml;
	}

	@Override
	public IComponent getPanel() {
		EchFormPanel<XMLPage> form = new EchFormPanel<XMLPage>(XMLPage.class);
		form.area(new XmlTextFormField(xml));
		form.setObject(this);
		return form;
	}
	
}
