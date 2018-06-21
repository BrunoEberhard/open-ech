package ch.ech.ech0108;

import java.util.List;

import org.minimalj.model.Keys;

// handmade
public class UidregInformation {
	public static final UidregInformation $ = Keys.of(UidregInformation.class);

	public Object id;
	public UidregStatusEnterpriseDetail uidregStatusEnterpriseDetail;
	public UidregPublicStatus uidregPublicStatus;
	public UidregOrganisationType uidregOrganisationType;
	public UidregLiquidationReason uidregLiquidationReason;
	public List<UidRegSource> uidregSource;
	public ch.openech.xml.UidStructure uidReplacement;
	public Boolean uidregUidService;
	
	// in version 4 nicht mehr vorhanden
	
	public ch.openech.xml.UidStructure uidHeadQuarter;
	
}