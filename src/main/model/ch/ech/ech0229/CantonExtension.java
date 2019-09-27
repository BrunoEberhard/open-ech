package ch.ech.ech0229;

import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

import ch.openech.xml.Any;

// handmade
public class CantonExtension {
	// public static final CantonExtension $ = Keys.of(CantonExtension.class);

	public final Any any = new Any();

	@NotEmpty
	@Size(255) // unknown
	public String canton;
}