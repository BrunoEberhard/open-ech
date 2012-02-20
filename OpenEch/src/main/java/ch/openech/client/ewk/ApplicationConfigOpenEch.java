package ch.openech.client.ewk;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;

import ch.openech.mj.application.ApplicationConfig;
import ch.openech.mj.application.WindowConfig;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.xml.write.EchNamespaceContext;

public class ApplicationConfigOpenEch extends ApplicationConfig {

	@Override
	public ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("ch.openech.resources.OpenEch");
	}
	
	@Override
	public WindowConfig getInitialWindowConfig() {
		return new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "2.2"));
	}
	
	@Override
	public WindowConfig[] getWindowConfigs() {
		return new WindowConfig[]{ //
				new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "2.2")), //
				new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "2.1")), //
				new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "2.0")), //
				new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "1.1")), //
				new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, "1.0")), //
				new WindowConfigOrg(), //
				new WindowConfigTpn(), //
		};
	}

	public static class OpenFrameEwkAction extends AbstractAction {
		private final PageContext parentPageContext;
		private final String version;
		
		public OpenFrameEwkAction(PageContext pageContext, String version) {
			super("EWK - Schema Version " + version);
			this.parentPageContext = pageContext;
			this.version = version;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ClientToolkit.getToolkit().openPageContext(parentPageContext, new WindowConfigEwk(EchNamespaceContext.getNamespaceContext(20, version)));
		}
	}

	public static class OpenFrameOrgAction extends AbstractAction {
		private final PageContext parentPageContext;
		
		public OpenFrameOrgAction(PageContext pageContext) {
			super("Organisationsverwaltung");
			this.parentPageContext = pageContext;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ClientToolkit.getToolkit().openPageContext(parentPageContext, new WindowConfigOrg());
		}
	}

	public static class OpenFrameTpnAction extends AbstractAction {
		private final PageContext parentPageContext;
		
		public OpenFrameTpnAction(PageContext pageContext) {
			super("Drittmeldepflicht");
			this.parentPageContext = pageContext;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ClientToolkit.getToolkit().openPageContext(parentPageContext, new WindowConfigTpn());
		}
	}

}
