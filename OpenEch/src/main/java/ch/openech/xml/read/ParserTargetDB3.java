package ch.openech.xml.read;

import java.sql.SQLException;

import ch.openech.dm.Event;
import ch.openech.dm.XmlConstants;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.dm.person.Relation;
import ch.openech.mj.util.StringUtils;
import ch.openech.server.EchPersistence;

public class ParserTargetDB3 implements ParserTarget {

	private final EchPersistence persistence;
	private String lastInsertedPersonId = null;
	private Event event;
	
	public ParserTargetDB3(EchPersistence persistence) throws Exception {
		this.persistence = persistence;
	}

	public String getLastInsertedPersonId() {
		return lastInsertedPersonId;
	}
	
	@Override
	public void setEvent(Event event) {
		this.event = event;
	}
	
	@Override
	public void insertPerson(Person person) throws ParserTargetException {
		try {
			person.event = event;
			persistence.person().insert(person);
			lastInsertedPersonId = person.getId();
		} catch (SQLException e) {
			throw new ParserTargetException(e);
		}
	}


	@Override
	public void simplePersonEvent(String type, PersonIdentification personIdentification, Person person) {
		if (StringUtils.equals(type, XmlConstants.DIVORCE, XmlConstants.UNDO_MARRIAGE, XmlConstants.UNDO_PARTNERSHIP)) removePartner(person);

		try {
			person.event = event;
			persistence.person().update(person);
			persistence.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//
	
	private void removePartner(Person changedPerson) {
		for (int i = changedPerson.relation.size()-1; i>= 0; i--) {
			Relation relation = changedPerson.relation.get(i);
			if (relation.isPartnerType()) changedPerson.relation.remove(i);
		}
	}
	
	@Override
	public Person getPerson(PersonIdentification personIdentification) throws ParserTargetException {
		if (personIdentification.getId() != null) {
			return persistence.person().getByLocalPersonId(personIdentification.getId());
		} else if (!StringUtils.isBlank(personIdentification.vn)) {
			return persistence.person().getByVn(personIdentification.vn);
		} else {
			return persistence.person().getByName(personIdentification.officialName, personIdentification.firstName, personIdentification.dateOfBirth);
		}
	}

}
