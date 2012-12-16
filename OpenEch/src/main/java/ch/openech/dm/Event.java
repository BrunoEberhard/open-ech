package ch.openech.dm;

import org.joda.time.LocalDateTime;

import ch.openech.mj.model.annotation.Size;

public class Event {

	public LocalDateTime time;
	@Size(255)
	public String type;
	// @Blob
	@Size(255)
	public String message;
}
