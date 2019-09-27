package ch.ech.ech0021;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.297")
public class Occupation {
	public static final Occupation $ = Keys.of(Occupation.class);

	@Size(100)
	public String jobTitle;

	public enum KindOfEmployment { _0, _1, _2, _3, _4;	}
	public KindOfEmployment kindOfEmployment;
	@Size(100)
	public String employer;
	public ch.ech.ech0010.AddressInformation placeOfWork;
	public ch.ech.ech0010.AddressInformation placeOfEmployer;
	public LocalDate occupationValidTill;
}