package ch.openech.transaction;

import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.Property;
import org.minimalj.transaction.Transaction;

import ch.ech.ech0011.Person;
import ch.ech.ech0020.v3.Delivery;
import ch.openech.ech0020.event.PersonEventImplementation;
import ch.openech.xml.EchReader;

public class EchTransaction implements Transaction<Person> {
	private static final long serialVersionUID = 1L;

	public final String input;

	public EchTransaction(String input) {
		this.input = input;
	}

	public Person execute() {
		Object object = EchReader.deserialize(input);
		if (object instanceof Delivery) {
			Person person = execute((Delivery) object);
			return person;
		} else {
			throw new IllegalArgumentException();
		}
	}

	private Person execute(Delivery delivery) {
		for (Property property : Properties.getProperties(Delivery.class).values()) {
			Object event = property.getValue(delivery);
			if (event != null) {
				return executeEvent(event);
			}
		}
		throw new IllegalArgumentException("No event in delivery");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Person executeEvent(Object event) {
		Class clazz = event.getClass();
		if (clazz.getName().contains("ech0020")) {
			try {
				String clazzName = PersonEventImplementation.class.getPackage().getName() + "." + clazz.getSimpleName() + "Implementation";
				Class<PersonEventImplementation> implementationClass = (Class<PersonEventImplementation>) Class.forName(clazzName);
				PersonEventImplementation impl = implementationClass.newInstance();
				return impl.apply(event);
			} catch (Exception x) {
				throw new RuntimeException(x);
			}
		} else {
			throw new IllegalArgumentException(clazz.getName());
		}
	}
		
}