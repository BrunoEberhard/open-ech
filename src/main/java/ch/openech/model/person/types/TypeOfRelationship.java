package  ch.openech.model.person.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import  ch.openech.model.types.EchCode;

public enum TypeOfRelationship implements EchCode {
	Ehepartner, 
	Partner, 
	Mutter, 
	Vater, 
	Pflegevater, 
	Pflegemutter, 
	Beistand, 
	Beirat, 
	Vormund,
	Vorsorgebeauftragter;
	
	public static final List<TypeOfRelationship> PARTNER = Collections.unmodifiableList(Arrays.asList(Ehepartner, Partner));
	public static final List<TypeOfRelationship> PARENT = Collections.unmodifiableList(Arrays.asList(Mutter, Vater, Pflegevater, Pflegemutter));
	public static final List<TypeOfRelationship> CARE = Collections.unmodifiableList(Arrays.asList(Beistand, Beirat, Vormund));
	public static final List<TypeOfRelationship> CARE_2_3 = Collections.unmodifiableList(Arrays.asList(Beistand, Beirat, Vormund, Vorsorgebeauftragter));
	
	public boolean isPartner() {
		return PARTNER.contains(this);
	}
	
	public boolean isParent() {
		return PARENT.contains(this);
	}

	public boolean isCare() {
		return CARE.contains(this);
	}

	@Override
	public String getValue() {
		return String.valueOf(ordinal() + 1);
	}
}