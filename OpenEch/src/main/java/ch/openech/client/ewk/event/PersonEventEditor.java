package ch.openech.client.ewk.event;

import java.util.List;

import ch.openech.client.ewk.PersonViewPage;
import ch.openech.client.ewk.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.AbstractFormVisual;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.page.Page;
import ch.openech.mj.util.GenericUtils;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private Person person;
	
	protected PersonEventEditor(EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
	}
	
	@Override
	public FormVisual<T> createForm() {
		@SuppressWarnings("unchecked")
		Class<T> genericClass = (Class<T>) GenericUtils.getGenericClass(getClass());

		EchFormPanel<T> formPanel = new EchFormPanel<T>(genericClass, getEchNamespaceContext(), getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}
	
	protected abstract void fillForm(AbstractFormVisual<T> formPanel);

	public void setPerson(Person person) {
		this.person = person;
		setFollowLink(Page.link(PersonViewPage.class, getEchNamespaceContext().getVersion(), person.getId()));
	}
	
	public Person getPerson() {
		return person;
	}
	
	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getPerson(), object, getEchNamespaceContext().getWriterEch0020());
	}
	
	protected abstract List<String> getXml(Person person, T object, WriterEch0020 writerEch0020) throws Exception;

}
