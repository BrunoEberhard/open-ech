package ch.ech.ech0021;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.882606900")
public class CivilDefenseData {
	public static final CivilDefenseData $ = Keys.of(CivilDefenseData.class);

	public ch.openech.model.YesNo civilDefense;
	public LocalDate civilDefenseValidFrom;
}