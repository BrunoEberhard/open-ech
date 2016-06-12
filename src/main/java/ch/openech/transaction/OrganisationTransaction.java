package ch.openech.transaction;

import java.util.Collections;
import java.util.List;

import org.minimalj.transaction.Transaction;

import ch.openech.model.organisation.Organisation;
import ch.openech.xml.read.StaxEch0148;

public class OrganisationTransaction implements Transaction<Organisation> {
	private static final long serialVersionUID = 1L;

	private final List<String> xmls;
	
	public OrganisationTransaction(List<String> xmls) {
		this.xmls = xmls;
	}

	public OrganisationTransaction(String xml) {
		this.xmls = Collections.singletonList(xml);
	}

	@Override
	public Organisation execute() {
		Organisation changedOrganisation = null;
		StaxEch0148 stax = new StaxEch0148();
		for (String xml : xmls) {
			try {
				stax.process(xml);
				if (changedOrganisation == null) {
					changedOrganisation = stax.getChangedOrganisation();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return changedOrganisation;
	}
	
}
