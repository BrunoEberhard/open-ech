package ch.ech.ech0021;

import java.time.LocalDate;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:00.469")
public class LockData {
	public static final LockData $ = Keys.of(LockData.class);

	@NotEmpty
	public DataLock dataLock;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
	@NotEmpty
	public ch.openech.xml.YesNo paperLock;
	public LocalDate paperLockValidFrom;
	public LocalDate paperLockValidTill;
}