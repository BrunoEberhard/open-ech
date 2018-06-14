package ch.ech.ech0213commons.v1;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

//handmade
public class PidsToUPI {
	public static final PidsToUPI $ = Keys.of(PidsToUPI.class);

	@Size(13)
	public Long vn;
	@Size(36)
	public String SPID;
	/*
	@Size(36)
	public String SPID;
	@Size(13)
	public Long vn;
	*/
}