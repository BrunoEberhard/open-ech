package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

//handmade. existiert in eCH nur als Teil von DataLock
public class PaperLock {
	public static final PaperLock $ = Keys.of(PaperLock.class);

	@NotEmpty
	public Boolean paperLock = false;
	public LocalDate paperLockValidFrom;
	public LocalDate paperLockValidTill;

}