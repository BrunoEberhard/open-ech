package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.model.common.NamedId;
import ch.openech.model.person.Person;
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
	public Form<T> createForm() {
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
		Object changedPersonId = send(xmls);
		return changedPersonId;
	}
	
	public static Object send(final List<String> xmls) {
		Object changedPersonId = Backend.getInstance().execute(new PersonTransaction(xmls));
		return changedPersonId;
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
