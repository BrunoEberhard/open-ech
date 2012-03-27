package ch.openech.client.e11;

import static ch.openech.dm.person.Foreign.FOREIGN;
import ch.openech.client.ewk.event.EchFormPanel;
import ch.openech.dm.person.Foreign;
import ch.openech.xml.write.EchNamespaceContext;

// Ausländer
public class ForeignPanel extends EchFormPanel<Foreign> {

	public ForeignPanel(EchNamespaceContext namespaceContext) {
		super(namespaceContext);
		
		line(FOREIGN.residencePermit);
		line(FOREIGN.residencePermitTill);
		line(FOREIGN.nameOnPassport);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 2 * super.getColumnWidthPercentage();
	}

}
