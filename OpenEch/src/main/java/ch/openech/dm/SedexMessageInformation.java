package ch.openech.dm;

import ch.openech.dm.person.PersonIdentification;

public class SedexMessageInformation extends Envelope {

	public String type;
	public final PersonIdentification personIdentification = new PersonIdentification();

}
