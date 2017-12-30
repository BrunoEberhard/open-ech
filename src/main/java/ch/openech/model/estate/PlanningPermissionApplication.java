package ch.openech.model.estate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;
import org.minimalj.util.DateUtils;

import ch.openech.model.EchFormats;
import ch.openech.model.common.Address;
import ch.openech.model.common.MunicipalityIdentification;
import ch.openech.model.common.NamedId;
import ch.openech.model.common.NamedMetaData;
import ch.openech.model.organisation.OrganisationIdentification;

@Sizes(EchFormats.class)
public class PlanningPermissionApplication {

	public static final PlanningPermissionApplication $ = Keys.of(PlanningPermissionApplication.class);
	
	public Object id;
	
	// identification
	public List<NamedId> localID = new ArrayList<>();
	public List<NamedId> otherID = new ArrayList<>();
	//
	
	@NotEmpty @Size(255)
	public String description;
	

	public String applicationType;
	
	public final List<Remark> remark = new ArrayList<>();

	@Size(100)
	public String proceedingType;

	// 
	
	public final List<Publication> publication = new ArrayList<>();
	public List<NamedMetaData> namedMetaData = new ArrayList<>();
	@NotEmpty
	public MunicipalityIdentification municipality;
	public final Address locationAddress = new Address();
	@NotEmpty
	public List<RealestateInformation> realestateInformation = new ArrayList<>();
	public List<Zone> zone = new ArrayList<>();
	public ConstructionProject constructionProject;
	// TODO ech147
	// public List<Directive> directive;
	// public List<DecisionRuling> decisionRuling = new ArrayList<>();
	// public List<Document> document = new ArrayList<>();
	
	public static class Publication implements Rendering {
		public static final Publication $ = Keys.of(Publication.class);

		@NotEmpty @Size(255)
		public String officialGazette;
		@NotEmpty @Size(1023) // in eCH nicht definiert
		public String publicationText;
		@NotEmpty
		public LocalDate publicationDate;
		public LocalDate publicationTill;
		
		@Override
		public String render(RenderType renderType) {
			return DateUtils.format(publicationDate) + " " + officialGazette;
 		}
	}
	
	public static class Zone {
		public static final Zone $ = Keys.of(Zone.class);

		public Object id;
		
		@Size(25)
		public String abbreviatedDesignation;
		@NotEmpty @Size(255)
		public String zoneDesignation;
		@Size(255)
		public String zoneType;
	}
	
	public static class DecisionRuling {
		public static final DecisionRuling $ = Keys.of(DecisionRuling.class);

		@NotEmpty @Size(255)
		public String ruling;
		@NotEmpty
		public LocalDate date;
		@NotEmpty
		public OrganisationIdentification rulingAuthority;
	}
	
	public static class Remark implements Rendering {
		public static final Remark $ = Keys.of(Remark.class);
		
		@NotEmpty @Size(255)
		public String token;

		@Override
		public String render(RenderType renderType) {
			return token;
		}
	}
	
	public static class PlanningPermissionApplicationOnly implements View<PlanningPermissionApplication> {
		
		public Object id;
		
		public List<NamedId> localID;
		public List<NamedId> otherID;
		//
		
		public String description;
		
		public String applicationType;
		
		public final List<Remark> remark = new ArrayList<>();

		public String proceedingType;
	}
}

