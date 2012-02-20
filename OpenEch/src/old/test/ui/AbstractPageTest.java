package ch.openech.test.ui;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;

import ch.openech.client.ewk.OpenEchApplication;
import ch.openech.dm.person.Person;
import ch.openech.mj.application.ObjectViewPage;
import ch.openech.mj.page.Page;
import ch.openech.mj.swing.FrameManager;
import ch.openech.mj.swing.SwingApplication;
import ch.openech.mj.swing.SwingFrame;

public class AbstractPageTest {

	private FrameFixture frameFixture;

	@Before
	public void setUp() {
		SwingApplication.launch(new OpenEchApplication());
		frameFixture = new FrameFixture(getView());
	}
	
	protected SwingFrame getView() {
		return FrameManager.getInstance().getNavigationFrames().get(0);
	}
	
//	protected void save() {
//		NavigationFrame view = getView();
//		int answer = 0;
//		((EditorPage) view.getVisiblePage()).checkedClose().save(answer);
//	}

	protected void menu(String text) {
		frameFixture.menuItem(text).click();
	}
	
	protected void gotoSearchPage() {
		menu("Neuer Tab");
	}

	protected void setText(String fieldName, String value) {
		frameFixture.textBox(fieldName).setText(value);
	}
	
	protected void doClickButton(String string) {
		frameFixture.button(string).click();
	}
	
	protected void executeSearch() {
		doClickButton("Person anzeigen");
	}
	
	protected String getVisiblePersonId() {
		Page visiblePage = getView().getVisiblePage();
		if (visiblePage instanceof ObjectViewPage<?>) {
			ObjectViewPage<?> personPage = (ObjectViewPage<?>) visiblePage;
			Person person = (Person) personPage.getObject();
			return person.getId();
		}
		return null;
	}

	protected void closeWindow() {
		frameFixture.menuItem("Fenster schliessen").click();
	}
	
}
