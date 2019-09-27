package ch.ech.ech0229;

import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:04.526")
public class DepreciationOnAssetsAppreciatedInPreviousYears {
	public static final DepreciationOnAssetsAppreciatedInPreviousYears $ = Keys.of(DepreciationOnAssetsAppreciatedInPreviousYears.class);

	@NotEmpty
	@Size(4)
	public Integer yearOfAppreciation;
	@NotEmpty
	@Size(400)
	public String nameOfAsset;
	public Long appreciationAmount;
	public Long depreciationAmount;
	public CantonExtension cantonExtension;
}