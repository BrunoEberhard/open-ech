package org.minimalj.swing;

import org.junit.Assert;

import org.fest.swing.fixture.FrameFixture;
import org.junit.BeforeClass;
import org.junit.Test;

import org.minimalj.swing.FrameManager;
import org.minimalj.swing.SwingFrame;
import org.minimalj.swing.SwingStandaloneClient;
import org.minimalj.swing.SwingFrame.CloseTabAction;
import org.minimalj.swing.SwingFrame.NewTabAction;

public class NavigationFrameTest {

	private static FrameFixture window;

	@BeforeClass
	public static void setUpOnce() {
		SwingStandaloneClient.launch(new SwingStandaloneClient());
		window = new FrameFixture(FrameManager.getInstance().getNavigationFrames().get(0));
	}

//	@AfterClass
//	public static void tearDown() {
//		window.menuItem(new ActionMatcher(ExitAction.class)).click();
//
//		JOptionPaneFixture optionPane = window.optionPane();
//		optionPane.yesButton().click();
//
//		window.cleanUp();
//	}

	@Test
	public void testNewTabAction() {
		
		window.menuItem(new ActionMatcher(NewTabAction.class)).click();
		SwingFrame frame = (SwingFrame) window.component();
		Assert.assertEquals(2, frame.getPages().size());
	}
	
	@Test
	public void testCloseTabAction() {
		SwingFrame frame = (SwingFrame) window.component();
		Assert.assertEquals(2, frame.getPages().size());
		window.menuItem(new ActionMatcher(CloseTabAction.class)).click();
		Assert.assertEquals(1, frame.getPages().size());
	}

}
