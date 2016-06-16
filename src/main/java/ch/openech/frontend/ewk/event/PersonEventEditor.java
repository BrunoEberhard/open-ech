package ch.openech.frontend.ewk.event;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.util.LoggingRuntimeException;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.ewk.XmlResult;
import ch.openech.frontend.page.PersonPage;
import ch.openech.model.person.Person;
import ch.openech.transaction.PersonTransaction;
import ch.openech.xml.write.WriterEch0020;

public abstract class PersonEventEditor<T> extends XmlEditor<T, Person> implements XmlResult<T> {

	private final PersonPage personPage;
	
	protected PersonEventEditor(PersonPage personPage) {
		super(personPage.getEchSchema());
		this.personPage = personPage;
	}
	
	@Override
	public Form<T> createForm() {
		EchForm<T> formPanel = new EchForm<T>(echSchema, getFormColumns());
		fillForm(formPanel);
		return formPanel;
	}

	public Person getPerson() {
		return personPage.getObject();
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
		personPage.setObject(person);
	}
	
	public static Person send(final List<String> xmls) {
		Person changedPerson = Backend.execute(new PersonTransaction(xmls));
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
