package ch.openech.xml;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class NamedId {
	public static final NamedId $ = Keys.of(NamedId.class);

	@NotEmpty
	@Size(20)
	public String idCategory;
	@NotEmpty
	@Size(36)
	public String id;

	public String getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(String idCategory) {
		this.idCategory = idCategory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonIdCategory() {
		return idCategory;
	}

	public void setPersonIdCategory(String personIdCategory) {
		this.idCategory = personIdCategory;
	}

	public String getPersonId() {
		return id;
	}

	public void setPersonId(String personId) {
		this.id = personId;
	}

	public String getOrganisationIdCategory() {
		return idCategory;
	}

	public void setOrganisationIdCategory(String organisationIdCategory) {
		this.idCategory = organisationIdCategory;
	}

	public String getOrganisationId() {
		return id;
	}

	public void setOrganisationId(String organisationId) {
		this.id = organisationId;
	}

}