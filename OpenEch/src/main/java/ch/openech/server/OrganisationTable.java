package ch.openech.server;

import static ch.openech.dm.organisation.Organisation.ORGANISATION;

import java.sql.SQLException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import ch.openech.dm.organisation.Organisation;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.SearchableTable;
import ch.openech.mj.db.model.ColumnAccess;
import ch.openech.mj.edit.value.PropertyAccessor;
import ch.openech.mj.util.StringUtils;

public class OrganisationTable extends SearchableTable<Organisation> {

	private static final String[] INDEX_FIELDS = {
		ORGANISATION.technicalIds.localId.personId, 
		ORGANISATION.organisationName, //
	};
	
	public OrganisationTable(DbPersistence dbPersistence) throws SQLException {
		super(dbPersistence, Organisation.class, INDEX_FIELDS);
	}
	
	@Override
	public int insert(Organisation organisation) throws SQLException {
		if (organisation.getId() == null) {
			organisation.technicalIds.localId.setOpenEch();
		}
		return super.insert(organisation);
	}

	public Organisation getByLocalOrganisationId(String organisationId) {
		List<Organisation> organisations = find(organisationId, new String[]{ORGANISATION.technicalIds.localId.personId});
		if (organisations.size() != 1) {
			throw new IllegalArgumentException(organisationId);
		} else {
			return organisations.get(0);
		}
	}
	
	public Organisation getByName(String organisationName) {
		List<Organisation> organisations = find(organisationName);
		if (organisations.size() > 1) {
			throw new IllegalArgumentException("Zuweisung über Namen nicht eindeutig für: " + organisationName);
		}
		if (!organisations.isEmpty()) {
			return organisations.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	protected Field getField(String fieldName, Organisation organisation) {
		String string = (String)PropertyAccessor.get(organisation, fieldName);
		if (string != null) {
			Field.Index index = Field.Index.ANALYZED;
			if (fieldName.toLowerCase().contains("date") || StringUtils.equals(fieldName, ORGANISATION.technicalIds.localId.personId)) {
				index = Field.Index.NOT_ANALYZED;
			}
			return new Field(fieldName, string,	Field.Store.YES, index);
		} else {
			return null;
		}
	}

	@Override
	protected Organisation createResultObject() {
		return new Organisation();
	}

	@Override
	protected Organisation documentToObject(Document document) {
		int id = Integer.parseInt(document.get("id"));
		try {
			return read(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void setField(Organisation result, String fieldName, String value) {
		ColumnAccess.setValue(result, fieldName, value);
	}

}