package ch.openech.ech0020.event;

import org.minimalj.frontend.form.Form;

import ch.ech.ech0011.Person;
import ch.ech.ech0020.v3.Delivery;

public interface PersonEventImplementation<T> {

	public T createEvent(Person person);
	
	public Form<T> createForm();

	public Delivery createDelivery(T event);

	public Person apply(T event);
}