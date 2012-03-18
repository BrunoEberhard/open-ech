package ch.openech.server;

import static ch.openech.dm.person.Person.PERSON;

import java.sql.SQLException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.SearchableTable;
import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.mj.util.StringUtils;

public class PersonTable extends SearchableTable<Person> {

	private static final String[] INDEX_FIELDS = {
		PERSON.personIdentification.technicalIds.localId.personId, 
		PERSON.personIdentification.officialName, //
		PERSON.personIdentification.firstName, //
		PERSON.personIdentification.dateOfBirth, //
		PERSON.aliasName, //
		PERSON.personIdentification.vn, //
	};
	
	public PersonTable(DbPersistence dbPersistence) throws SQLException {
		super(dbPersistence, Person.class, INDEX_FIELDS);
	}
	
	@Override
	public int insert(Person person) throws SQLException {
		if (person.getId() == null) {
			person.personIdentification.technicalIds.localId.setOpenEch();
		}
		return super.insert(person);
	}

	public Person getByVn(String vn) {
		List<Person> persons = find(vn, new String[]{PERSON.personIdentification.vn});
		if (persons.size() < 1) {
			return null;
			// throw new RuntimeException("Keine Person gefunden für " + vn);
//		} else if (persons.size() > 1) {
//			throw new RuntimeException("Mehr als eine Person gefunden für " + vn);
		} else {
			return persons.get(0);
		}
	}
	
	public Person getByLocalPersonId(String personId) {
		List<Person> persons = find(personId, new String[]{PERSON.personIdentification.technicalIds.localId.personId});
		if (persons.size() != 1) {
			throw new IllegalArgumentException(personId);
		} else {
			return persons.get(0);
		}
	}
	
	public Person getByName(String name, String firstName, String dateOfBirth) {
		List<Person> persons = find(name);
		for (int i = persons.size()-1; i>= 0; i--) {
			Person person = persons.get(i);
			if (!StringUtils.isBlank(firstName) && !StringUtils.equals(firstName, person.personIdentification.firstName)) {
				persons.remove(i);
			} else if (!StringUtils.isBlank(dateOfBirth) && !StringUtils.equals(dateOfBirth, person.personIdentification.dateOfBirth)) {
				persons.remove(i);
			}
		}
		if (persons.size() > 1) {
			throw new IllegalArgumentException("Zuweisung über Namen und Geburtstag nicht eindeutig für: " + name + ", " + firstName + ", " + dateOfBirth);
		}
		if (!persons.isEmpty()) {
			return persons.get(0);
		} else {
			return null;
		}
	}
	
	public Person getByIdentification(PersonIdentification personIdentification) {
		if (personIdentification.technicalIds.localId.personId != null) {
			Person person = getByLocalPersonId(personIdentification.technicalIds.localId.personId);
			if (person != null) return person;
		} 
		if (personIdentification.vn != null) {
			Person person = getByVn(personIdentification.vn);
			if (person != null) return person;
		} 
		// TODO getByIdentification für Sedex verbessern
		List<Person> persons = find(personIdentification.firstName + " " + personIdentification.officialName);
		if (!persons.isEmpty()) {
			return persons.get(0);
		} else {
			return null;
		}
	}

	@Override
	protected Field getField(String fieldName, Person person) {
		String string = (String)PropertyAccessor.get(person, fieldName);
		if (string != null) {
			Field.Index index = Field.Index.ANALYZED;
			if (fieldName.toLowerCase().contains("date") || StringUtils.equals(fieldName, PERSON.personIdentification.technicalIds.localId.personId, PERSON.personIdentification.vn)) {
				index = Field.Index.NOT_ANALYZED;
			}
			return new Field(fieldName, string,	Field.Store.YES, index);
		} else {
			return null;
		}
	}

	@Override
	protected Person createResultObject() {
		return new Person();
	}

	@Override
	protected Person documentToObject(Document document) {
		int id = Integer.parseInt(document.get("id"));
		try {
			return read(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void setField(Person result, String fieldName, String value) {
		ColumnAccess.setValue(result, fieldName, value);
	}

}
