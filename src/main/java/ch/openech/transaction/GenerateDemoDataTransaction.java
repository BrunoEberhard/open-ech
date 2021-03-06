package ch.openech.transaction;

import java.io.Serializable;

import org.minimalj.transaction.Role;
import org.minimalj.transaction.Transaction;

import ch.openech.OpenEchRoles;

@Role(OpenEchRoles.importExport)
public class GenerateDemoDataTransaction implements Transaction<Serializable> {
	private static final long serialVersionUID = 1L;
	
	private final int personCount;
	private final int organisationCount;
	
	public GenerateDemoDataTransaction(int personCount, int organisationCount) {
		this.personCount = personCount;
		this.organisationCount = organisationCount;
	}

	@Override
	public Serializable execute() {
		generateDemoPersons(personCount);
		generateDemoOrganisations(organisationCount);
		return null;
	}

	private void generateDemoPersons(final int number) {
//		final EchSchema ewkNamespaceContext = EchSchema.getNamespaceContext(20, "2.2");
//		
//		final int parallel = 10;
//		final AtomicInteger success = new AtomicInteger();
//		final AtomicInteger fail = new AtomicInteger();
//		for (int i = 0; i<parallel; i++) {
//			new Thread(new Runnable() {
//				public void run() {
//					final WriterEch0020 writerEch0020 = new WriterEch0020(ewkNamespaceContext);
//					for (int saveProgress = 0; saveProgress<number / parallel; saveProgress++) {
//						try {
//							String xml = writerEch0020.moveIn(DataGenerator.person());
//							new PersonTransaction(xml).execute();
//							int value = success.addAndGet(1);
//							if (value % 10 == 0) {
//								System.out.println(success);
//							}
//						} catch (Exception x) {
//							fail.addAndGet(1);
//							System.out.println(fail);
//						}
//						// progress(saveProgress, count);
//					}
//				}
//			}).start();
//		}
	}
	
	private void generateDemoOrganisations(int number) {
//		final EchSchema echSchema = EchSchema.getNamespaceContext(148, "1.0");
//		final WriterEch0148 writer = new WriterEch0148(echSchema);
//		for (int i = 0; i<number; i++) {
//			try {
//				String xml = writer.moveIn(DataGenerator.organisation());
//				new OrganisationTransaction(xml).execute();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}	
	
}
