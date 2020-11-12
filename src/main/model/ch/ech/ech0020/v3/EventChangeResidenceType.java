package ch.ech.ech0020.v3;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class EventChangeResidenceType {
	public static final EventChangeResidenceType $ = Keys.of(EventChangeResidenceType.class);

	@NotEmpty
	public BaseDeliveryPerson changeResidenceTypePerson;
	public final ReportingMunicipality changeResidenceTypeReportingRelationship = new ReportingMunicipality();
	public LocalDate residenceTypeValidFrom;
}