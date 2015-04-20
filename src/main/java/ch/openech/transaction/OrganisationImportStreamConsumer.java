package ch.openech.transaction;

import java.io.InputStream;

import org.minimalj.backend.Backend;
import org.minimalj.transaction.StreamConsumer;

import ch.openech.xml.read.StaxEch0148;

public class OrganisationImportStreamConsumer implements StreamConsumer<Integer> {
	private static final long serialVersionUID = 1L;

	@Override
	public Integer consume(Backend backend, InputStream stream) {
		try {
			StaxEch0148 stax = new StaxEch0148(backend);
			stax.process(stream, null);
			return 0; // count
		} catch (Exception x) {
			x.printStackTrace();
			return 0; // count
		}
	}
	
}