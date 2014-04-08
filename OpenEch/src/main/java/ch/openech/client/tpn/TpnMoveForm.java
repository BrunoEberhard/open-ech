package ch.openech.client.tpn;

import static ch.openech.dm.tpn.ThirdPartyMove.*;
import ch.openech.client.ewk.event.EchForm;
import ch.openech.dm.tpn.ThirdPartyMove;

public class TpnMoveForm extends EchForm<ThirdPartyMove> {

	private final MoveDirection direction;
	
	public TpnMoveForm(MoveDirection direction) {
		super(4);
		this.direction = direction;
		createContent();
	}

	private void createContent() {
		addTitle("Person");
		line(THIRD_PARTY_MOVE.officialName);
		line(THIRD_PARTY_MOVE.callName);
		line(THIRD_PARTY_MOVE.sex, THIRD_PARTY_MOVE.dateOfBirth, THIRD_PARTY_MOVE.nationality);
		line(direction == MoveDirection.IN ? THIRD_PARTY_MOVE.comesFrom : THIRD_PARTY_MOVE.goesTo);
		
		addTitle("Vertrag");
		line(THIRD_PARTY_MOVE.beginOfContract, THIRD_PARTY_MOVE.endOfContract);
		
		addTitle("Vermieter");
		line(THIRD_PARTY_MOVE.contractor);
		
		addTitle("Gebäude");
		line(THIRD_PARTY_MOVE.EGID);
		line(THIRD_PARTY_MOVE.address);
		
		addTitle("Wohnung");
		line(THIRD_PARTY_MOVE.EWID);
		line(THIRD_PARTY_MOVE.administrativeDwellingId, THIRD_PARTY_MOVE.floor, THIRD_PARTY_MOVE.positionOnFloor, THIRD_PARTY_MOVE.roomCount);
	}

	@Override
	protected int getColumnWidthPercentage() {
		return 70;
	}

}
