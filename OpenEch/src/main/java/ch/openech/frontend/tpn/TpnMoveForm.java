package ch.openech.frontend.tpn;

import static  ch.openech.model.tpn.ThirdPartyMove.*;
import ch.openech.frontend.ewk.event.EchForm;
import  ch.openech.model.tpn.ThirdPartyMove;

public class TpnMoveForm extends EchForm<ThirdPartyMove> {

	private final MoveDirection direction;
	
	public TpnMoveForm(MoveDirection direction) {
		super(4);
		this.direction = direction;
		createContent();
	}

	private void createContent() {
		addTitle("Person");
		line($.officialName);
		line($.callName);
		line($.sex, $.dateOfBirth, $.nationality);
		line(direction == MoveDirection.IN ? $.comesFrom : $.goesTo);
		
		addTitle("Vertrag");
		line($.beginOfContract, $.endOfContract);
		
		addTitle("Vermieter");
		line($.contractor);
		
		addTitle("Gebäude");
		line($.EGID);
		line($.address);
		
		addTitle("Wohnung");
		line($.EWID);
		line($.administrativeDwellingId, $.floor, $.positionOnFloor, $.roomCount);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 70;
	}

}
