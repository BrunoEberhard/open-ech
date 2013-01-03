package ch.openech.client.ewk.event;

import java.util.List;


import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.page.PersonViewPage;
import ch.openech.client.preferences.OpenEchPreferences;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.util.GenericUtils;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private Person person;
	
	protected PersonEventEditor(EchSchema echSchema, OpenEchPreferences preferences) {
		super(echSchema, preferences);
	}
	
	@Override
	public IForm<T> createForm() {
		@SuppressWarnings("unchecked")
		Class<T> genericClass = (Class<T>) GenericUtils.getGenericClass(getClass());

		EchForm<T> formPanel = new EchForm<T>(genericClass, echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}
	
	protected abstract void fillForm(Form<T> formPanel);

	public void setPerson(Person person) {
		this.person = person;
		setFollowLink(Page.link(PersonViewPage.class, echSchema.getVersion(), person.getId()));
	}
	
	public Person getPerson() {
		return person;
	}
	
	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getPerson(), object, echSchema.getWriterEch0020());
	}
	
	protected abstract List<String> getXml(Person person, T object, WriterEch0020 writerEch0020) throws Exception;

}
