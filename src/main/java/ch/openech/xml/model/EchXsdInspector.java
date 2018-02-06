package ch.openech.xml.model;

import org.minimalj.application.Application;
import org.minimalj.frontend.impl.swing.Swing;
import org.minimalj.frontend.page.Page;

public class EchXsdInspector extends Application {

	@Override
	public Page createDefaultPage() {
		return new XsdSchemaPage();
	}
	
	public static void main(String[] args) {
		Swing.start(new EchXsdInspector());
	}
}