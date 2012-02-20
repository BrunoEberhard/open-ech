package ch.openech.client.ewk;

import java.util.Collections;
import java.util.List;

import ch.openech.client.ewk.PersonPanel.PersonPanelType;
import ch.openech.dm.person.Person;
import ch.openech.mj.edit.form.FormVisual;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.util.BusinessRule;
import ch.openech.server.EchServer;
import ch.openech.server.ServerCallResult;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0020;

public class BirthCreator extends XmlEditor<Person> {
	
	public BirthCreator(PageContext context, EchNamespaceContext echNamespaceContext) {
		super(echNamespaceContext);
	}

	@Override
	public FormVisual<Person> createForm() {
		return new PersonPanel(PersonPanelType.BIRTH, getEchNamespaceContext());
	}
	
	@Override
	public Person load() {
		return new Person();
	}

	protected WriterEch0020 getWriterEch0020() {
		return getEchNamespaceContext().getWriterEch0020();
	}
	
	@Override
	public List<String> getXml(Person person) throws Exception {
		return Collections.singletonList(getWriterEch0020().birth(person));
	}

	@Override
	public boolean save(Person person) {
		String xml;
		try {
			xml = getWriterEch0020().birth(person);
			ServerCallResult result = EchServer.getInstance().process(xml);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void validate(Person person, List<ValidationMessage> resultList) {
		person.validate(resultList);
		validatePlaceOfBirth(person, resultList);
	}

	@BusinessRule("Geburtsort muss bei Erfassung einer Geburt angegeben werden")
	public void validatePlaceOfBirth(Person person, List<ValidationMessage> resultList) {
		if (getEchNamespaceContext().birthPlaceMustNotBeUnknown() && (person.placeOfBirth == null || person.placeOfBirth.isUnknown())) {
			resultList.add(new ValidationMessage("placeOfBirth", "Geburtsort fehlt"));
		}
	}

	



}
