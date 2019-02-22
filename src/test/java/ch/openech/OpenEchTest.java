package ch.openech;

import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.impl.json.JsonFrontend;

public class OpenEchTest {

	static {
		Application.setInstance(new OpenEch());
		Frontend.setInstance(new JsonFrontend());
	}

}
