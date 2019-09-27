package ch.ech.ech0011;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Selection;

import ch.ech.ech0006.ResidencePermit;
import ch.ech.ech0006.ResidencePermitBorder;
import ch.ech.ech0006.ResidencePermitCategory;
import ch.ech.ech0006.ResidencePermitRuling;
import ch.ech.ech0006.ResidencePermitShort;
import ch.ech.ech0006.ResidencePermitToBeRegistered;

// handmade: für das UI braucht es die einzelnen felder
public class ResidencePermitData {
	public static final ResidencePermitData $ = Keys.of(ResidencePermitData.class);

	public ResidencePermitCategory residencePermitCategory;
	public ResidencePermitRuling residencePermitRuling;

	public ResidencePermitBorder residencePermitBorder;
	public ResidencePermitShort residencePermitShort;
	public ResidencePermitToBeRegistered residencePermitToBeRegistered;

	public LocalDate residencePermitValidFrom;
	public LocalDate residencePermitValidTill;
	public LocalDate entryDate;

	public ResidencePermit getResidencePermit() {
		if (Keys.isKeyObject(this)) {
			return Keys.methodOf(this, "residencePermit");
		}
		if (residencePermitCategory == null) {
			return null;
		}
		String code = residencePermitCategory.name().substring(1);
		if (residencePermitRuling != null) {
			code += residencePermitRuling.name().substring(1);
		}

		if (residencePermitBorder != null) {
			code += residencePermitBorder.name().substring(1);
		} else if (residencePermitShort != null) {
			code += residencePermitShort.name().substring(1);
		} else if (residencePermitToBeRegistered != null) {
			code += residencePermitToBeRegistered.name().substring(1);
		}
		return getValue(ResidencePermit.class, code);
	}

	public void setResidencePermit(ResidencePermit r) {
		residencePermitCategory = null;
		residencePermitRuling = null;
		residencePermitBorder = null;
		residencePermitShort = null;
		residencePermitToBeRegistered = null;

		if (r == null) {
			return;
		} else {
			String code = r.name();
			residencePermitCategory = getValue(ResidencePermitCategory.class, code.substring(1, 3));

			if (code.length() >= 5) {
				residencePermitRuling = getValue(ResidencePermitRuling.class, code.substring(3, 5));
				if (code.length() >= 7) {
					if (residencePermitCategory == ResidencePermitCategory._06) {
						residencePermitBorder = getValue(ResidencePermitBorder.class, code.substring(5, 7));
					} else if (residencePermitCategory == ResidencePermitCategory._07) {
						residencePermitShort = getValue(ResidencePermitShort.class, code.substring(5, 7));
					} else if (residencePermitCategory == ResidencePermitCategory._10) {
						residencePermitToBeRegistered = getValue(ResidencePermitToBeRegistered.class,
								code.substring(5, 7));
					}
				}
			}
		}
	}

	public Selection<ResidencePermitRuling> getResidencePermitRulingSelection() {
		if (Keys.isKeyObject(this)) {
			return Keys.methodOf(this, "residencePermitRulingSelection", $.residencePermitCategory);
		}
		return new Selection<ResidencePermitRuling>(residencePermitRuling, getRulingValues());
	}

	public void setResidencePermitRulingSelection(Selection<ResidencePermitRuling> r) {
		residencePermitRuling = r.selectedValue;
	}

	//

	public Selection<?> getResidencePermitSubCategorySelection() {
		if (Keys.isKeyObject(this)) {
			return Keys.methodOf(this, "residencePermitSubCategorySelection", $.residencePermitCategory,
					$.getResidencePermitRulingSelection());
		}
		if (residencePermitCategory == ResidencePermitCategory._06) {
			return new Selection<ResidencePermitBorder>(residencePermitBorder, getBorderValues());
		} else if (residencePermitCategory == ResidencePermitCategory._07) {
			return new Selection<ResidencePermitShort>(residencePermitShort, getResidencePermitShortValues());
		} else if (residencePermitCategory == ResidencePermitCategory._10) {
			return new Selection<ResidencePermitToBeRegistered>(residencePermitToBeRegistered,
					getResidencePermitToBeRegisteredValues());
		} else {
			return null;
		}
	}

	public void setResidencePermitSubCategorySelection(Selection<Object> r) {
		residencePermitBorder = null;
		residencePermitShort = null;
		residencePermitToBeRegistered = null;
		if (r != null) {
			if (r.selectedValue instanceof ResidencePermitBorder) {
				residencePermitBorder = (ResidencePermitBorder) r.selectedValue;
			} else if (r.selectedValue instanceof ResidencePermitShort) {
				residencePermitShort = (ResidencePermitShort) r.selectedValue;
			} else if (r.selectedValue instanceof ResidencePermitToBeRegistered) {
				residencePermitToBeRegistered = (ResidencePermitToBeRegistered) r.selectedValue;
			}
		}
	}

	//

	private List<ResidencePermitRuling> getRulingValues() {
		if (residencePermitCategory == null) {
			return Collections.emptyList();
		}
		switch (residencePermitCategory) {
		case _01:
			return Arrays.asList(ResidencePermitRuling._02);
		case _02:
		case _03:
		case _04:
		case _06:
			return Arrays.asList(ResidencePermitRuling._01, ResidencePermitRuling._02);
		case _05:
			return Arrays.asList(ResidencePermitRuling._03);
		case _07:
			return Arrays.asList(ResidencePermitRuling._01, ResidencePermitRuling._02, ResidencePermitRuling._09);
		case _08:
			return Arrays.asList(ResidencePermitRuling._04);
		case _09:
			return Arrays.asList(ResidencePermitRuling._05);
		case _10:
			return Arrays.asList(ResidencePermitRuling._06);
		case _11:
			return Arrays.asList(ResidencePermitRuling._07);
		case _12:
			return Arrays.asList(ResidencePermitRuling._08);
		default:
			return Collections.emptyList();
		}
	}

	private List<ResidencePermitBorder> getBorderValues() {
		if (residencePermitCategory == ResidencePermitCategory._06) {
			return Arrays.asList(ResidencePermitBorder.values());
		} else {
			return Collections.emptyList();
		}
	}

	private List<ResidencePermitShort> getResidencePermitShortValues() {
		if (residencePermitCategory == ResidencePermitCategory._07) {
			if (residencePermitRuling == ResidencePermitRuling._01) {
				return Arrays.asList(ResidencePermitShort._01, ResidencePermitShort._02, ResidencePermitShort._03,
						ResidencePermitShort._04, ResidencePermitShort._05);
			} else if (residencePermitRuling == ResidencePermitRuling._02) {
				return Arrays.asList(ResidencePermitShort._01, ResidencePermitShort._02, ResidencePermitShort._04,
						ResidencePermitShort._05, ResidencePermitShort._06);
			} else if (residencePermitRuling == ResidencePermitRuling._09) {
				return Arrays.asList(ResidencePermitShort._07);
			}
		}
		return Collections.emptyList();
	}

	private List<ResidencePermitToBeRegistered> getResidencePermitToBeRegisteredValues() {
		if (residencePermitCategory == ResidencePermitCategory._10) {
			return Arrays.asList(ResidencePermitToBeRegistered.values());
		} else {
			return Collections.emptyList();
		}
	}

	private <T extends Enum<T>> T getValue(Class<T> enumClass, String code) {
		if (code != null) {
			for (T e : EnumUtils.valueList(enumClass)) {
				if (e.name().substring(1).equals(code)) {
					return e;
				}
			}
		}
		return null;
	}

}