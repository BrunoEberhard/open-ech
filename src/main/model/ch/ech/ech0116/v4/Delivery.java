package ch.ech.ech0116.v4;

import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:02.378")
public class Delivery {
	public static final Delivery $ = Keys.of(Delivery.class);

	@NotEmpty
	public ch.ech.ech0058.Header deliveryHeader;
	public EventFederalRegisterBaseDelivery FederalRegisterBaseDelivery;
	public EventRequestNewOrganisation requestNewOrganisation;
	public EventRequestRegisterModification requestRegisterModification;
	public EventRequestRegisterDeletion requestRegisterDeletion;
	public EventRequestOrganisationReactivation requestOrganisationReactivation;
	public EventRequestRegistration requestRegistration;
	public EventRequestDeregistration requestDeregistration;
	public EventRegisterRequest registerRequest;
	public EventInfoRegisterModification registerModification;
}