package ch.openech.transaction;

import java.io.InputStream;

import org.minimalj.backend.Persistence;
import org.minimalj.transaction.StreamConsumer;

import ch.openech.xml.read.StaxEch0148;

public class OrganisationImportStreamConsumer extends StreamConsumer<Integer> {
	private static final long serialVersionUID = 1L;

	public OrganisationImportStreamConsumer(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public Integer execute(Persistence persistence) {
		try {
			StaxEch0148 stax = new StaxEch0148(persistence);
			stax.process(getStream(), null);
			return 0; // count
		} catch (Exception x) {
			x.printStackTrace();
			return 0; // count
		}
	}
	
}
