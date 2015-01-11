package ch.openech.frontend.ewk.event;

import java.io.File;
import java.io.FileWriter;

import ch.openech.model.Envelope;
import ch.openech.xml.write.WriterEch0090;

public class SedexOutputGenerator {

	
	public static boolean sedexOutputDirectoryAvailable() {
		String directoryName = null; // TODO PreferencesHelper.preferences().get("sedexOutput", null);
		if (directoryName != null) {
			File directory = new File(directoryName);
			return directory.isDirectory() && directory.canWrite();
		} else {
			return false;
		}
	}
	
	public static void generateSedex(String xml, Envelope envelope) {
		try {
			String xmlEnvelope = new WriterEch0090().envelope(envelope);
			String directoryName = null; // TODO = PreferencesHelper.preferences().get("sedexOutput", null);
			File directory = new File(directoryName);
			FileWriter fileWriter = new FileWriter(new File(directory, "envl_" + envelope.messageId +".xml"));
			fileWriter.write(xmlEnvelope);
			fileWriter.flush();
			fileWriter.close();
			
			fileWriter = new FileWriter(new File(directory, "data_" + envelope.messageId +".xml"));
			fileWriter.write(xml);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
}
