package ch.openech.client.ewk.event;

import java.util.List;

import ch.openech.client.XmlEditor;
import ch.openech.client.ewk.XmlResult;
import ch.openech.client.page.PersonViewPage;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.Form;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.PageLink;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private final Person person;
	
	protected PersonEventEditor(EchSchema ech, Person person) {
		super(ech);
		this.person = person;
	}
	
	@Override
	public IForm<T> createForm() {
		EchForm<T> formPanel = new EchForm<T>(echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}

	public Person getPerson() {
		return person;
	}
	
	protected abstract void fillForm(Form<T> formPanel);
	
	@Override
	public Object save(T object) throws Exception {
		super.save(object);
		return PageLink.link(PersonViewPage.class, echSchema.getVersion(), person.getId());
	}

	@Override
	public List<String> getXml(T object) throws Exception {
		return getXml(getPerson(), object, echSchema.getWriterEch0020());
	}
	
	protected abstract List<String> getXml(Person person, T object, WriterEch0020 writerEch0020) throws Exception;

	public boolean isEnabled() {
		return person.isAlive();
	}

}
