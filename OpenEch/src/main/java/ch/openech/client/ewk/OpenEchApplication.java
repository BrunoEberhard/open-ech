/*
	Copyright (C) 2010, Bruno Eberhard, bruno.eberhard@pop.ch

	This file is part of Open-eCH.

    Open-eCH is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Open-eCH is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Open-eCH.  If not, see <http://www.gnu.org/licenses/>.
*/
package ch.openech.client.ewk;

import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import ch.openech.client.ApplicationConfigOpenEch;
import ch.openech.mj.swing.FrameManager;
import ch.openech.mj.swing.GraphicalLogHandler;
import ch.openech.mj.swing.PreferencesHelper;
import ch.openech.mj.swing.SwingApplication;
import ch.openech.server.EchPersistence;
import ch.openech.server.EchServer;

public class OpenEchApplication extends SwingApplication {
	
	private final String[] args;
	private GraphicalLogHandler graphicalLogHandler;
	
	public OpenEchApplication(String... args) {
		this.args = args;
		Locale.setDefault(Locale.GERMAN);
	}
	
	@Override
	public void run() {
		PreferencesHelper.setPreferences(Preferences.userNodeForPackage(OpenEchApplication.class));
		FrameManager.setSystemLookAndFeel();
		
		PreferencesHelper.preferences().putBoolean("dbMemory", true);
//		if (args != null && args.length >= 3) {
//			// EchServer server = new EchServer(args[0], args[1], args[2]);
//			checkDbVersion();
//		} else {
//			DerbyOptionsDialogController.showOptions();
//		}
		// Initialize Server
		EchServer.getInstance();
		
		graphicalLogHandler = new GraphicalLogHandler();
		graphicalLogHandler.addFilterButton("SQL", null, EchPersistence.logger.getName());
		graphicalLogHandler.addFilterButton("XML ->", null, EchServer.logger.getName());
	
		FrameManager.getInstance().openNavigationFrame();
	}

	private void checkDbVersion() {
		if (!EchServer.getInstance().getPersistence().isValidDbVersion()) {
			JOptionPane.showMessageDialog(null, "Die Datenbank wurde mit einer älteren\nVersion erstellt und kann nicht verwendet werden.\n",
					"Hinweis", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	/*
	 * MAIN
	 */
	public static void main(final String[] args) {
		SwingApplication.launch(new OpenEchApplication(args), new ApplicationConfigOpenEch());
	}

}
