package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.common.NamedId;
import ch.openech.model.person.Person;
import ch.openech.transaction.PersonTransaction;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T, Person> implements XmlResult<T> {

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
	public Person save(T object) {
		List<String> xmls = getXml(object);
		Person changedPerson = send(xmls);
		return changedPerson;
	}
	
	@Override
	protected void finished(Person person) {
		ClientToolkit.getToolkit().show(new PersonPage(echSchema, person));
	}
	
	public static Person send(final List<String> xmls) {
		Person changedPerson = Backend.getInstance().execute(new PersonTransaction(xmls));
		return changedPerson;
	}
	
	@Override
	public List<String> getXml(T object) {
		try {
			return getXml(getPerson(), object, echSchema.getWriterEch0020());
		} catch (Exception x) {
			throw new LoggingRuntimeException(x, logger, "XML generation failed");
		}
	}
	
	protected abstract List<String> getXml(Person person, T object, WriterEch0020 writerEch0020) throws Exception;

}
