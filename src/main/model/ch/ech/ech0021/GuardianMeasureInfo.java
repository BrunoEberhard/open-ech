package ch.ech.ech0021;

import java.util.TreeSet;
import java.util.Set;
import java.time.LocalDate;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.856334400")
public class GuardianMeasureInfo {
	public static final GuardianMeasureInfo $ = Keys.of(GuardianMeasureInfo.class);

	public final Set<BasedOnLaw> basedOnLaw = new TreeSet<>();
	@Size(100)
	public String basedOnLawAddOn;
	@NotEmpty
	public LocalDate guardianMeasureValidFrom;
}