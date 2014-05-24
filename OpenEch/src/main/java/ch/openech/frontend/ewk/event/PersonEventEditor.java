package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.edit.form.IForm;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.util.SerializationContainer;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.page.PersonViewPage;
import  ch.openech.model.common.NamedId;
import  ch.openech.model.person.Person;
import ch.openech.transaction.PersonTransaction;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T> implements XmlResult<T> {

	private final Person person;
	
	protected PersonEventEditor(EchSchema ech, Person person) {
		super(ech);
		this.person = person;
		this.person.personIdentification().technicalIds.localId.personIdCategory = NamedId.OPEN_ECH_ID_CATEGORY;
		this.person.personIdentification().technicalIds.localId.personId = String.valueOf(person.id);
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
		List<String> xmls = getXml(object);
		Person person = send(xmls);
		return PageLink.link(PersonViewPage.class, echSchema.getVersion(), person.getId());
	}
	
	public static Person send(final List<String> xmls) {
		Person firstPerson = null;
		for (String xml : xmls) {
			if (firstPerson == null) {
				firstPerson = (Person) SerializationContainer.unwrap(Backend.getInstance().execute(new PersonTransaction(xml)));
			} else {
				Backend.getInstance().execute(new PersonTransaction(xml));
			}
		}
		return firstPerson;
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