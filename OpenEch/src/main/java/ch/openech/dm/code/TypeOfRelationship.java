package ch.openech.dm.code;

import java.util.Arrays;
import java.util.List;

import ch.openech.mj.db.model.CodeValue;
import ch.openech.mj.util.StringUtils;

public enum TypeOfRelationship implements CodeValue {

	Ehepartner(1), Partner(2), Mutter(3), Vater(4), Pflegevater(5), Pflegemutter(6), Beistand(7), Beirat(8), Vormund(9);
	
	private static final List<TypeOfRelationship> PARTNER = Arrays.asList(Ehepartner, Partner);
	private static final List<TypeOfRelationship> PARENT = Arrays.asList(Mutter, Vater, Pflegevater, Pflegemutter);
	private static final List<TypeOfRelationship> CARE = Arrays.asList(Beistand, Beirat, Vormund);
	
	private final String value;
	
	private TypeOfRelationship(int value) {
		this.value = Integer.toString(value);
	}
	
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
	public String getKey() {
		return value;
	}
	
	public static TypeOfRelationship lookup(String key) {
		for (TypeOfRelationship t : values()) {
			if (StringUtils.equals(t.getKey(), key)) {
				return t;
			}
		}
		return null;
	}
	
}
