package ch.ech.ech0011;

import java.time.LocalDate;

import org.minimalj.model.EnumUtils;
import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;

import ch.openech.frontend.ech0011.RangeUtil;

// rendering
public class SeparationData implements Rendering {
	public static final SeparationData $ = Keys.of(SeparationData.class);

	public Separation separation;
	public LocalDate separationValidFrom;
	public LocalDate separationValidTill;

	@Override
	public CharSequence render() {
		if (separation != null) {
			StringBuilder s = new StringBuilder();
			s.append(EnumUtils.getText(separation));
			RangeUtil.appendRange(s, separationValidFrom, separationValidTill);
			return s.toString();
		} else {
			return null;
		}
	}
}