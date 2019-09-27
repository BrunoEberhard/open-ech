package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.901")
public class NatureRisk {
	public static final NatureRisk $ = Keys.of(NatureRisk.class);

	@NotEmpty
	@Size(255)
	public String riskDesignation;
	@NotEmpty
	public Boolean riskExists;
}