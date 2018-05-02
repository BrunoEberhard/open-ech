package ch.openech.xml.frontend;

import org.minimalj.application.Application;
import org.minimalj.frontend.impl.swing.Swing;
import org.minimalj.frontend.page.Page;

public class EchXsdInspector extends Application {

	@Override
	public Page createDefaultPage() {
		return new XsdModelTablePage();
	}
	
	public static void main(String[] args) {
		Swing.start(new EchXsdInspector());
	}
}