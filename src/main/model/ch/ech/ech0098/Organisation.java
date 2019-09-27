package ch.ech.ech0098;

import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;
import org.minimalj.util.StringUtils;

// handmade
public class Organisation {
	public static final Organisation $ = Keys.of(Organisation.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0097.OrganisationIdentification organisationIdentification;
	@Size(500)
	public String uidBrancheText;
	@Size(6)
	public String NOGACode;
	public Foundation foundation;
	public Liquidation liquidation;
	public List<OrganisationAddress> address;
	public ch.ech.ech0046.Contact contact;
	@Size(255) // unknown
	public String languageOfCorrespondance;
	
	//
	
	public String getNogaCode() {
		return NOGACode;
	}
	
	public void setNogaCode(String nogaCode) {
		this.NOGACode = nogaCode;
	}

	public void render(StringBuilder s) {
		if (organisationIdentification != null && !StringUtils.isEmpty(organisationIdentification.organisationName)) {
			s.append(organisationIdentification.organisationName);
			if (address != null && address.size() > 0) {
				String town = address.get(0).town;
				if (!StringUtils.isEmpty(town)) {
					s.append(", ").append(town);
				}
			}
			s.append('\n');
		}
	}
}