package ch.ech.ech0129;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class InsuranceVolume {
	public static final InsuranceVolume $ = Keys.of(InsuranceVolume.class);

	@NotEmpty
	@Size(7)
	public Integer volume;
	@NotEmpty
	public BuildingVolumeNorm norm;
}