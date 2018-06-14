package ch.ech.ech0072.v1;

import java.time.LocalDate;
import java.util.Locale;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.Codes;
import org.minimalj.util.LocaleContext;
import org.minimalj.util.StringUtils;

//handmade
public class Country implements Code, Rendering {
	public static final Country $ = Keys.of(Country.class);

	@Size(4)
	public Integer id;
	@Size(3)
	public Integer unId;
	@Size(2)
	public String iso2Id;
	@Size(3)
	public String iso3Id;
	@Size(60)
	public String shortNameDe, shortNameFr, shortNameIt, shortNameEn;
	@Size(255)
	public String officialNameDe, officialNameFr, officialNameIt;
	@Size(1)
	public Integer continent;
	@Size(1)
	public Integer region;
	public Boolean state;
	@Size(4)
	public Integer areaState;
	public Boolean unMember;
	public LocalDate unEntryDate;
	public Boolean recognizedCh;
	public LocalDate recognizedDate;
	@Size(255)
	public String remarkDe, remarkFr, remarkIt;
	public Boolean entryValid;
	public LocalDate dateOfChange;

	@Override
	public String render(RenderType renderType) {
		return getShortName();
	}
	
	public String getShortName() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "shortName");
		
		String language = LocaleContext.getCurrent().getLanguage();
		if (!StringUtils.isEmpty(shortNameEn) && language.equals(Locale.ENGLISH.getLanguage())) {
			return shortNameEn;
		} else if (!StringUtils.isEmpty(shortNameIt) && language.equals(Locale.ITALIAN.getLanguage())) {
			return shortNameIt;
		} else if (!StringUtils.isEmpty(shortNameFr) && language.equals(Locale.FRENCH.getLanguage())) {
			return shortNameFr;
		} else {
			return shortNameDe;
		}
	}
	
	public static Country getSwitzerland() {
		return Codes.findCode(Country.class, 8100);
	}
}