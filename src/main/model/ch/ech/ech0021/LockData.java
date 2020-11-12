package ch.ech.ech0021;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator")
public class LockData {
	public static final LockData $ = Keys.of(LockData.class);

	@NotEmpty
	public DataLock dataLock;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
	@NotEmpty
	public ch.openech.model.YesNo paperLock;
	public LocalDate paperLockValidFrom;
	public LocalDate paperLockValidTill;
}