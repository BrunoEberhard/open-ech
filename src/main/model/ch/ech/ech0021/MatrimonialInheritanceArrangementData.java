package ch.ech.ech0021;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.883607900")
public class MatrimonialInheritanceArrangementData {
	public static final MatrimonialInheritanceArrangementData $ = Keys.of(MatrimonialInheritanceArrangementData.class);

	@NotEmpty
	public ch.openech.model.YesNo matrimonialInheritanceArrangement;
	public LocalDate matrimonialInheritanceArrangementValidFrom;
}