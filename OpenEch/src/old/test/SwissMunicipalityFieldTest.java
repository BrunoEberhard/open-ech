package ch.openech.swing.field.e07;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPopupMenuFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.openech.client.e07.SwissMunicipalityField;
import ch.openech.dm.common.MunicipalityIdentification;
import ch.openech.mj.swing.ActionMatcher;
import ch.openech.mj.swing.toolkit.SwingClientToolkit;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.SwitchLayout;

// Sollte unabhängig von Toolkit getestet werden
@Deprecated
public class SwissMunicipalityFieldTest implements ChangeListener {

	private FrameFixture window;
	private SwissMunicipalityField field;
	private int changeCount = 0;

	@Before
	public void onSetUp() {
		ClientToolkit.setToolkit(new SwingClientToolkit());
		
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		field = new SwissMunicipalityField(true);
		field.setChangeListener(this);
		frame.add((Component) field.getComponent(), BorderLayout.CENTER);
		
		window = new FrameFixture(frame);
		window.show(); // shows the frame to test
	}

	private Component getVisibleComponent() {
		Container c = (Container) field.getComponent();
		for (Component component: c.getComponents()) {
			if (component.isVisible()) {
				if (component instanceof SwitchLayout) {
					component = (Component) ((SwitchLayout) component).getShownComponent();
				}
				return component;
			}
		}
		return null;
	}
	
	@Test
	public void testSetFreeMunicipality() {
		MunicipalityIdentification municipality = new MunicipalityIdentification();
		municipality.municipalityName = "Inexstenzithausen";
		municipality.cantonAbbreviation = "XY";

		Assert.assertEquals(0, changeCount);

		field.setObject(municipality);
		Component visibleComponent = getVisibleComponent();
		Assert.assertTrue("Textfeld sollte sichtbar sein", visibleComponent instanceof JTextComponent);
		JTextComponent textComponent = (JTextComponent) visibleComponent;
		Assert.assertEquals("Inexstenzithausen", textComponent.getText());
		
		Assert.assertEquals(1, changeCount);
		
		JPopupMenuFixture popupMenu = window.textBox().showPopupMenu();
		popupMenu.menuItem(new ActionMatcher("MunicipalitySelectAction")).click();
	
		visibleComponent = getVisibleComponent();
		Assert.assertTrue("ComboBox sollte sichtbar sein", visibleComponent instanceof JComboBox);
		JComboBox comoBox = (JComboBox) visibleComponent;
		Assert.assertEquals(null, comoBox.getSelectedItem());
		
		Assert.assertEquals(2, changeCount);
		
		window.comboBox().selectItem(2);
		Assert.assertNotNull(comoBox.getSelectedItem());

		Assert.assertEquals(3, changeCount);

		municipality = new MunicipalityIdentification();
		field.setObject(municipality);
		Assert.assertNull(field.getObject());
		Assert.assertEquals(4, changeCount);

		field.setObject(null);
		Assert.assertNull(field.getObject());
		Assert.assertEquals(5, changeCount);
	}

	public void wait2s() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void stateChanged(ChangeEvent e) {
		changeCount++;
	}
}
