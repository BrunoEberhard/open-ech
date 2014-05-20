package ch.openech.xml.write;

import static  ch.openech.model.XmlConstants.ARRIVAL_DATE;
import static  ch.openech.model.XmlConstants.COMES_FROM;
import static  ch.openech.model.XmlConstants.DATE_OF_DEATH;
import static  ch.openech.model.XmlConstants.DEATH;
import static  ch.openech.model.XmlConstants.DEPARTURE_DATE;
import static  ch.openech.model.XmlConstants.DESTINATION_ADDRESS;
import static  ch.openech.model.XmlConstants.DESTINATION_MUNICIPALITY;
import static  ch.openech.model.XmlConstants.FEDERAL_REGISTER;
import static  ch.openech.model.XmlConstants.GOES_TO;
import static  ch.openech.model.XmlConstants.HAS_MAIN_RESIDENCE;
import static  ch.openech.model.XmlConstants.MOVE_IN;
import static  ch.openech.model.XmlConstants.MOVE_OUT;
import static  ch.openech.model.XmlConstants.MOVE_OUT_PERSON;
import static  ch.openech.model.XmlConstants.MOVE_OUT_REPORTING_DESTINATION;
import static  ch.openech.model.XmlConstants.REPORTING_MUNICIPALITY;
import static  ch.openech.model.XmlConstants.SECONDARY_RESIDENCE;

import org.joda.time.LocalDate;

import  ch.openech.model.common.MunicipalityIdentification;
import  ch.openech.model.common.Place;
import  ch.openech.model.person.Occupation;
import  ch.openech.model.person.Person;
import  ch.openech.model.person.PersonIdentification;
import  ch.openech.model.person.Relation;

// Die komplexeren Typen werden meist von 0020 wiederverwendet, vor allem
// moveOutPersonType
public class WriterEch0093 extends DeliveryWriter {

	public final String URI;
	public final WriterEch0007 ech7;
	public final WriterEch0010 ech10;
	public final WriterEch0011 ech11;
	public final WriterEch0020 ech20;
	public final WriterEch0021 ech21;	
	public final WriterEch0044 ech44;
	
	public WriterEch0093(EchSchema context) {
		super(context);

		URI = context.getNamespaceURI(93);
		ech7 = new WriterEch0007(context);
		ech10 = new WriterEch0010(context);
		ech11 = new WriterEch0011(context);
		ech20 = new WriterEch0020(context);
		ech21 = new WriterEch0021(context);
		ech44 = new WriterEch0044(context);
	}

	@Override
	protected int getSchemaNumber() {
		return 93;
	}
	
	@Override
	protected String getNamespaceURI() {
		return URI;
	}

	// Elemente für alle Events

	private WriterElement simplePersonEvent(String eventName, PersonIdentification personIdentification) throws Exception {
		return simplePersonEvent(delivery(), eventName, personIdentification);
	}

	private WriterElement simplePersonEvent(WriterElement delivery, String eventName, PersonIdentification personIdentification) throws Exception {
		WriterElement event = delivery.create(URI, eventName);

		String personName = eventName + "Person";

		WriterElement person = event.create(URI, personName);
		ech44.personIdentification(person, personIdentification);

		return event;
	}

	
	// /////////////////////////////////

	// code 1
	public String moveIn(Person person) throws Exception {
		WriterElement event = simplePersonEvent(MOVE_IN, person.personIdentification());
		reportingMunicipality(event, HAS_MAIN_RESIDENCE, person.residence.reportingMunicipality, person);
		return result();
	}
	
	private void reportingMunicipality(WriterElement parent, String tagName, MunicipalityIdentification reportingMunicipality, Person values) throws Exception {
		WriterElement element = parent.create(URI, tagName);
		if (reportingMunicipality != null) {
			if (reportingMunicipality.isFederalRegister()) {
				element.text(FEDERAL_REGISTER, reportingMunicipality.getFederalRegister());
			} else {
				ech7.municipality(element, REPORTING_MUNICIPALITY, reportingMunicipality);
			}
		}
		element.values(values, ARRIVAL_DATE);
		if (values.comesFrom != null) {
			// Das ist die Abweichung zu der Methode gleichen Namens in e20
			ech7.municipality(element, COMES_FROM, values.comesFrom.municipalityIdentification);
		}
		ech11.dwellingAddress(element, values.dwellingAddress);
	}

	// code 2
	public String moveOut(Person person, MunicipalityIdentification reportingMunicipality, Place goesTo, LocalDate departureDate) throws Exception {
        WriterElement event = delivery().create(URI, MOVE_OUT);
        ech20.moveInPerson(event, URI, MOVE_OUT_PERSON, person);
        ech20.addParentNames(event, person);
        for (Relation relation: person.relation) ech21.relation(event, relation);
        moveOutReportingDestination(event, reportingMunicipality, goesTo, departureDate);
		if (person.residence.secondary != null) {
			for (MunicipalityIdentification residence : person.residence.secondary) {
				ech7.municipality(event, SECONDARY_RESIDENCE, residence);
			}
		}
        for (Occupation occupation : person.occupation) ech21.occupation(event, occupation);
        return result();
	}
	
	private void moveOutReportingDestination(WriterElement parent, MunicipalityIdentification reportingMunicipality, Place goesTo, LocalDate departureDate) throws Exception {
        WriterElement element = parent.create(URI, MOVE_OUT_REPORTING_DESTINATION);
        if (reportingMunicipality.isFederalRegister()) {
        	element.text(FEDERAL_REGISTER, reportingMunicipality.getFederalRegister());
        } else {
            ech7.municipality(element, REPORTING_MUNICIPALITY, reportingMunicipality);
        }
        WriterElement goesToElement = element.create(URI, GOES_TO);
        // Das ist die Abweichung zu der Methode in e20
        ech7.municipality(goesToElement, DESTINATION_MUNICIPALITY, goesTo.municipalityIdentification);
        if (goesTo.mailAddress != null) {
            ech10.swissAddressInformation(goesToElement, DESTINATION_ADDRESS, goesTo.mailAddress);
        }
        //
        element.text(DEPARTURE_DATE, departureDate);
	}

	// code 3
	public String death(PersonIdentification personIdentification, LocalDate date) throws Exception {
		WriterElement event = simplePersonEvent(DEATH, personIdentification);
		event.text(DATE_OF_DEATH, date);
		return result();
	}
	
}
