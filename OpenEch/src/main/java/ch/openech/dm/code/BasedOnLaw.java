package ch.openech.dm.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.openech.dm.types.EchCode;

// Gesetzesgrundlage
public enum BasedOnLaw implements EchCode {
	
	_308, _309, _310, _311, _312,
	_368, _369, _370, _371, _372,
	_392, _393, _394, _395_1, _395_2,
	_397_a,
	_385, _386;

	public static final List<BasedOnLaw> VERSION_3 = Collections.unmodifiableList(Arrays.asList(_368, _369, _370, _371, _372));
	
	@Override
	public String getValue() {
		return name().substring(1);
	}
	
}
