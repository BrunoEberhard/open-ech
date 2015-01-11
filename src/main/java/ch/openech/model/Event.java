package  ch.openech.model;

import java.time.LocalDateTime;

import org.minimalj.model.annotation.Size;

public class Event {

	public LocalDateTime time;
	@Size(255)
	public String type;
	// @Blob
	@Size(255)
	public String message;
}
