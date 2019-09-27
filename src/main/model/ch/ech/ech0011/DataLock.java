package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

// handmade. existiert in eCH nur als Teil von DataLock
public class DataLock {
	public static final DataLock $ = Keys.of(DataLock.class);

	@NotEmpty
	public ch.ech.ech0021.DataLock dataLock = ch.ech.ech0021.DataLock._0;
	public LocalDate dataLockValidFrom;
	public LocalDate dataLockValidTill;
}
