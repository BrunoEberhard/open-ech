package ch.ech.ech0211.v1;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class PlanningPermissionApplication {
	public static final PlanningPermissionApplication $ = Keys.of(PlanningPermissionApplication.class);

	public Object id;
	@NotEmpty
	public PlanningPermissionApplicationIdentification planningPermissionApplicationIdentification;
	@NotEmpty
	@Size(1000)
	public String description;
	@Size(100)
	public String applicationType;
	public List<String> remark;
	@Size(100)
	public String proceedingType;
	public Boolean profilingYesNo;
	public LocalDate profilingDate;
	@Size(255)
	public String intendedPurpose;
	public Boolean parkingLotsYesNo;
	public List<NatureRisk> natureRisk;
	public BigDecimal constructionCost;
	public List<Publication> publication;
	public List<ch.ech.ech0129.NamedMetaData> namedMetaData;
	public final ch.ech.ech0010.AddressInformation locationAddress = new ch.ech.ech0010.AddressInformation();
	@NotEmpty
	public List<RealestateInformation> realestateInformation;
	public List<Zone> zone;
	public ConstructionProjectInformation constructionProjectInformation;
	public List<ch.ech.ech0147.t2.Directive> directive;
	public List<DecisionRuling> decisionRuling;
	@NotEmpty
	public List<ch.ech.ech0147.t0.Document> document;
	public List<PlanningPermissionApplicationIdentification> referencedPlanningPermissionApplication;
}