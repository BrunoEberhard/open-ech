package ch.openech.transaction;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.minimalj.backend.Persistence;
import org.minimalj.transaction.PersistenceTransaction;
import org.minimalj.transaction.Role;

import ch.openech.datagenerator.DataGenerator;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0020;
import ch.openech.xml.write.WriterEch0148;

@Role("su")
public class GenerateDemoDataTransaction implements PersistenceTransaction<Serializable> {
	private static final long serialVersionUID = 1L;
	
	private final int personCount;
	private final int organisationCount;
	
	public GenerateDemoDataTransaction(int personCount, int organisationCount) {
		this.personCount = personCount;
		this.organisationCount = organisationCount;
	}

	@Override
	public Serializable execute(Persistence persistence) {
		generateDemoPersons(persistence, personCount);
		generateDemoOrganisations(persistence, organisationCount);
		return null;
	}

	private void generateDemoPersons(final Persistence persistence, final int number) {
		final EchSchema ewkNamespaceContext = EchSchema.getNamespaceContext(20, "2.2");
		
		final int parallel = 10;
		final AtomicInteger success = new AtomicInteger();
		final AtomicInteger fail = new AtomicInteger();
		for (int i = 0; i<parallel; i++) {
			new Thread(new Runnable() {
				public void run() {
					final WriterEch0020 writerEch0020 = new WriterEch0020(ewkNamespaceContext);
					for (int saveProgress = 0; saveProgress<number / parallel; saveProgress++) {
						try {
							String xml = writerEch0020.moveIn(DataGenerator.person());
							new PersonTransaction(xml).execute(persistence);
							int value = success.addAndGet(1);
							if (value % 10 == 0) {
								System.out.println(success);
							}
						} catch (Exception x) {
							fail.addAndGet(1);
							System.out.println(fail);
						}
						// progress(saveProgress, count);
					}
				}
			}).start();
		}
	}
	
	private void generateDemoOrganisations(final Persistence persistence, int number) {
		final EchSchema echSchema = EchSchema.getNamespaceContext(148, "1.0");
		final WriterEch0148 writer = new WriterEch0148(echSchema);
		for (int i = 0; i<number; i++) {
			try {
				String xml = writer.moveIn(DataGenerator.organisation());
				new OrganisationTransaction(xml).execute(persistence);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
}
