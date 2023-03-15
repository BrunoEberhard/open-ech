package ch.openech.ech0020.event;

import java.util.Optional;

import org.minimalj.backend.Backend;
import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.Property;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.GenericUtils;

import ch.ech.ech0011.Person;
import ch.ech.ech0020.v3.Delivery;
import ch.ech.ech0044.PersonIdentification;

@SuppressWarnings("unchecked")
public abstract class AbstractPersonEventImplementation<EVENT, CHANGE> implements PersonEventImplementation<EVENT> {

	protected final Class<EVENT> eventClass = (Class<EVENT>) GenericUtils.getGenericClass(this.getClass(), 0);
	protected final Class<CHANGE> changeClass = (Class<CHANGE>) GenericUtils.getGenericClass(this.getClass(), 1);

	@Override
	public EVENT createEvent(Person person) {
		EVENT event = CloneHelper.newInstance(eventClass);

		Optional<Property> identificationProperty = findProperty(eventClass, PersonIdentification.class);
		identificationProperty.orElseThrow(IllegalArgumentException::new).setValue(event, person.personIdentification);

		Optional<Property> changeProperty = findProperty(eventClass, changeClass);
		changeProperty.orElseThrow(IllegalArgumentException::new).setValue(event, findOldValue(person));

		return event;
	}

	protected Optional<Property> findProperty(Class<?> clazz, Class<?> propertyClass) {
		return Properties.getProperties(clazz).values().stream().filter(p -> p.getClazz() == propertyClass).findFirst();
	}

	private CHANGE findOldValue(Object oldObject) {
		Optional<Property> changeProperty = findProperty(oldObject.getClass(), changeClass);
		return (CHANGE) changeProperty.orElseThrow(IllegalArgumentException::new).getValue(oldObject);
	}

	@Override
	public Delivery createDelivery(EVENT event) {
		Delivery delivery = new Delivery();
		setValue(delivery, event);
		return delivery;
	}

	protected void setValue(Object delivery, EVENT event) {
		Optional<Property> eventProperty = findProperty(delivery.getClass(), eventClass);
		eventProperty.orElseThrow(IllegalArgumentException::new).setValue(delivery, event);
	}

	@Override
	public Person apply(EVENT event) {
		Person person = readPerson(event);
		apply(event, person);
		return Backend.save(person);
	}

	protected void apply(EVENT event, Person person) {
		Optional<Property> eventChangeProperty = findProperty(eventClass, changeClass);
		CHANGE change = (CHANGE) eventChangeProperty.orElseThrow(IllegalArgumentException::new).getValue(event);

		Optional<Property> changeProperty = findProperty(Person.class, changeClass);
		changeProperty.orElseThrow(IllegalArgumentException::new).setValue(person, change);
	}

	private Person readPerson(EVENT event) {
		Optional<Property> identificationProperty = findProperty(eventClass, PersonIdentification.class);
		PersonIdentification personIdentification = (PersonIdentification) identificationProperty.orElseThrow(IllegalArgumentException::new).getValue(event);

		return Backend.read(Person.class, personIdentification.localPersonId.namedId);
	}

}
