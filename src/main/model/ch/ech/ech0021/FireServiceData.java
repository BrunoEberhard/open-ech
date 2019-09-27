package ch.ech.ech0021;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.469")
public class FireServiceData {
	public static final FireServiceData $ = Keys.of(FireServiceData.class);

	public ch.openech.model.YesNo fireService;
	public ch.openech.model.YesNo fireServiceLiability;
	public LocalDate fireServiceValidFrom;
}