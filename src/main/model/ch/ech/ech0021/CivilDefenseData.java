package ch.ech.ech0021;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.485")
public class CivilDefenseData {
	public static final CivilDefenseData $ = Keys.of(CivilDefenseData.class);

	public ch.openech.xml.YesNo civilDefense;
	public LocalDate civilDefenseValidFrom;
}