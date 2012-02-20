package ch.openech.xml.read;

import ch.openech.dm.Event;
import ch.openech.dm.person.Person;
import ch.openech.dm.person.PersonIdentification;

public interface ParserTarget {

	Person getPerson(PersonIdentification personIdentification) throws ParserTargetException;
	
	void setEvent(Event event);
	
	void insertPerson(Person values) throws ParserTargetException;

	// events

	void simplePersonEvent(String type, PersonIdentification personIdentification, Person person);
	
}
