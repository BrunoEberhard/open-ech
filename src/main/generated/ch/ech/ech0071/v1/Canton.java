package ch.ech.ech0071.v1;

import java.time.LocalDate;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

//handmade
public class Canton implements Code {
	public static final Canton $ = Keys.of(Canton.class);

	@Size(2)
	public String id;
	
	@Size(2)
	public Integer cantonId;
	@Size(60)
	public String cantonLongName;
	public LocalDate cantonDateOfChange;
	
	public CantonAbbreviation getCantonAbbreviation() {
		return CantonAbbreviation.valueOf(id);
	}

	public void setCantonAbbreviation(CantonAbbreviation cantonAbbreviation) {
		this.id = cantonAbbreviation.name();
	}
	
}