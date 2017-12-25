package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.model.common.Address;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.organisation.OrganisationIdentification;

public class PlanningPermissionApplication {

	public static final PlanningPermissionApplication $ = Keys.of(PlanningPermissionApplication.class);
	
	public Object id;
	
	// identification
	public List<NamedId> localID;
	public List<NamedId> otherID;
	//
	
	@NotEmpty @Size(255)
	public String description;
	
	@Size(100)
	public String applicationType;
	
	public final List<Remark> remark = new ArrayList<>();

	@Size(100)
	public String proceedingType;

	// < planningPermissionApplicationOnly
	
	public List<Publication> publication = new ArrayList<>();
	public List<NamedMetaData> namedMetaData = new ArrayList<>();
	public MunicipalityIdentification municipality;
	public Address locationAddress;
	@NotEmpty
	public List<RealestateInformation> realestateInformation = new ArrayList<>();
	public List<Zone> zone = new ArrayList<>();
	// public ConstructionProject constructionProject;
	public List<Directive> directive;
	public List<DecisionRuling> decisionRuling = new ArrayList<>();
	// public List<Document> document = new ArrayList<>();
	
	public static class Publication {
		@NotEmpty @Size(255)
		public String officialGazette;
		@Size(255)
		public String publicationText;
		@NotEmpty
		public LocalDate publicationDate;
		public LocalDate publicationTill;
	}
	
	public static class Zone {
		@Size(25)
		public String abbreviatedDesignation;
		@NotEmpty @Size(255)
		public String zoneDesignation;
		@Size(255)
		public String zoneType;
	}
	
	public static class DecisionRuling {
		@NotEmpty @Size(255)
		public String ruling;
		@NotEmpty
		public LocalDate date;
		@NotEmpty
		public OrganisationIdentification rulingAuthority;
	}
	
	public static class Remark {
		@NotEmpty @Size(255)
		public String token;
		
	}
}
