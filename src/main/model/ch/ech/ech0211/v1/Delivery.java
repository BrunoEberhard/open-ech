package ch.ech.ech0211.v1;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.474260")
public class Delivery {
	public static final Delivery $ = Keys.of(Delivery.class);

	@NotEmpty
	public ch.ech.ech0058.Header deliveryHeader;
	public EventBaseDelivery eventBaseDelivery;
	public EventSubmitPlanningPermissionApplication eventSubmitPlanningPermissionApplication;
	public EventChangeContact eventChangeContact;
	public EventRequest eventRequest;
	public EventAccompanyingReport eventAccompanyingReport;
	public EventCloseArchiveDossier eventCloseArchiveDossier;
	public EventKindOfProceedings eventKindOfProceedings;
	public EventNoticeInvolvedParty eventNoticeInvolvedParty;
	public EventNotice eventNotice;
	public EventStatusNotification eventStatusNotification;
	public EventChangeResponsibility eventChangeResponsibility;
}