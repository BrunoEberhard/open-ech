package ch.ech.ech0020.v3;

import java.time.LocalDate;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:30:59.530")
public class NameInfo {
	public static final NameInfo $ = Keys.of(NameInfo.class);

	public final ch.ech.ech0011.NameData nameData = new ch.ech.ech0011.NameData();
	public LocalDate nameValidFrom;
}