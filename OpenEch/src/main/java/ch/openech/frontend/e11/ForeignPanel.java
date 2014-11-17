package ch.openech.frontend.e11;

import static  ch.openech.model.person.Foreign.$;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.person.Foreign;
import ch.openech.xml.write.EchSchema;

// Ausländer
public class ForeignPanel extends EchForm<Foreign> {

	public ForeignPanel(EchSchema echSchema) {
		super(echSchema);
		
		line($.residencePermit);
		line($.residencePermitTill);
		line($.nameOnPassport);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 2 * super.getColumnWidthPercentage();
	}

}
