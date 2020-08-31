package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.463260")
public class NatureRisk {
	public static final NatureRisk $ = Keys.of(NatureRisk.class);

	@NotEmpty
	@Size(255)
	public String riskDesignation;
	@NotEmpty
	public Boolean riskExists;
}