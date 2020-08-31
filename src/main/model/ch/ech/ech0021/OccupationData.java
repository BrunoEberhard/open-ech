package ch.ech.ech0021;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.863098500")
public class OccupationData {
	public static final OccupationData $ = Keys.of(OccupationData.class);

	public ch.openech.model.UidStructure UID;
	@Size(100)
	public String employer;
	public ch.ech.ech0010.AddressInformation placeOfWork;
	public ch.ech.ech0010.AddressInformation placeOfEmployer;
	public LocalDate occupationValidFrom;
	public LocalDate occupationValidTill;
}