package ch.openech.model.estate;

import java.util.List;

import ch.openech.model.common.Address;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.organisation.OrganisationIdentification;
import ch.openech.model.person.PersonIdentification;

public class RealestateInformation {

	public Realestate realestate;
	public MunicipalityIdentification municipality;
	public List<BuildingInformation> buildingInformation;
	// public PlaceName placeName;
	public List<Owner> owner;
	
	
	public static class Owner {
		public PersonIdentification personIdentification;
		public OrganisationIdentification organisationIdentification;
		public Address ownerAdress;
	}
	
	// does not exist in 211
	public static class BuildingInformation {
		public Building building;
		public List<Dwelling> dwelling;
	}

}
