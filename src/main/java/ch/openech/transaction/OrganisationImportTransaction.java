package ch.openech.transaction;

import java.io.InputStream;

import org.minimalj.transaction.InputStreamTransaction;

import ch.openech.xml.read.StaxEch0148;

public class OrganisationImportTransaction extends InputStreamTransaction<Integer> {
	private static final long serialVersionUID = 1L;

	public OrganisationImportTransaction(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public Integer execute() {
		try {
			StaxEch0148 stax = new StaxEch0148();
			stax.process(getStream(), null);
			return 0; // count
		} catch (Exception x) {
			x.printStackTrace();
			return 0; // count
		}
	}
	
}
