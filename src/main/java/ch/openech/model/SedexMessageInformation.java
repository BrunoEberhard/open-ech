package  ch.openech.model;

import  ch.openech.model.person.PersonIdentification;

public class SedexMessageInformation extends Envelope {

	public String type;
	public final PersonIdentification personIdentification = new PersonIdentification();

}
