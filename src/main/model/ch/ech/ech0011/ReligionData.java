package ch.ech.ech0011;

import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.755287200")
public class ReligionData {
	public static final ReligionData $ = Keys.of(ReligionData.class);

	@NotEmpty
	@Size(6)
	public String religion;
	public LocalDate religionValidFrom;
}