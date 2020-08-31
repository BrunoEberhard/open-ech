package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.828333100")
public class EventChangeResidenceType {
	public static final EventChangeResidenceType $ = Keys.of(EventChangeResidenceType.class);

	@NotEmpty
	public BaseDeliveryPerson changeResidenceTypePerson;
	public final ReportingMunicipality changeResidenceTypeReportingRelationship = new ReportingMunicipality();
	public LocalDate residenceTypeValidFrom;
}