package ch.openech.xml;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class NamedId {
	public static final NamedId $ = Keys.of(NamedId.class);

	@NotEmpty
	@Size(20)
	public String namedIdCategory;
	@NotEmpty
	@Size(36)
	public String namedId;

	public String getIdCategory() {
		return namedIdCategory;
	}

	public void setIdCategory(String idCategory) {
		this.namedIdCategory = idCategory;
	}

	public String getId() {
		return namedId;
	}

	public void setId(String id) {
		this.namedId = id;
	}

	public String getPersonIdCategory() {
		return namedIdCategory;
	}

	public void setPersonIdCategory(String personIdCategory) {
		this.namedIdCategory = personIdCategory;
	}

	public String getPersonId() {
		return namedId;
	}

	public void setPersonId(String personId) {
		this.namedId = personId;
	}

	public String getOrganisationIdCategory() {
		return namedIdCategory;
	}

	public void setOrganisationIdCategory(String organisationIdCategory) {
		this.namedIdCategory = organisationIdCategory;
	}

	public String getOrganisationId() {
		return namedId;
	}

	public void setOrganisationId(String organisationId) {
		this.namedId = organisationId;
	}

}